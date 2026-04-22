package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.NativeDriverIntegration;
import com.example.microej.Page;
import ej.microui.display.Colors;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Uses {@code java.net} / {@code javax.net.ssl} against the VEE port lwIP + mbedTLS stack (real device).
 * Wi-Fi AP scan is not wired here (needs {@code ej.api:ecom-wifi} + STA driver); see on-screen note.
 */
public class NetworkPage implements Page {

    private static final int SECTION    = 4100;
    private static final int INFO       = 4101;
    private static final int ACTION_BTN = 4102;
    private static final int RESULT     = 4103;
    private static final int LOG        = 4104;

    /** Host used for DNS / TCP / HTTP demos (port 80). */
    private static final String DEMO_HOST = "microej.com";
    /** Target URL to test: automationpractice.com */
    private static final String TARGET_HOST = "automationpractice.com";
    private static final String TARGET_PATH = "/index.php";
    /** HTTPS demo (port 443). */
    private static final int HTTPS_PORT = 443;
    private static final int SOCKET_TIMEOUT_MS = 15000;
    private static final int HTTP_READ_MAX = 512;
    private static final int LOG_MAX_LINES = 200;

    private Label resultLabel;
    private Label ipLabel;
    private List  logList;
    private int   logCount;

    /** True when we're running the auto sequence (so we don't wipe the log between steps). */
    private boolean autoMode;

    /**
     * Step status for auto-run: set by each diagnostic method.
     *  0 = OK, negative = error (LWIP / NET / IO), positive = warning/partial.
     */
    private int lastStepStatus;

    /**
     * If true, on-screen log lines are cleaned (no "[TAG]" prefixes).
     * Debug output (stdout) still keeps tags.
     */
    private static final boolean CLEAN_UI_LOG = true;

    @Override public String getName()        { return "Network / WiFi"; }
    @Override public String getDescription() { return "DNS, TCP, HTTP, TLS via lwIP; local interfaces"; }
    @Override public int    getAccentColor() { return AppStyle.GREEN; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.GREEN);
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(5));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(380, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.GREEN, 1));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(LOG));
        s.setColor(AppStyle.CYAN);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(4));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "\u25b6  Network Cape  -  Board IP Address");
        info(main, "Java Sockets  \u2192  LLNET  \u2192  lwIP on this VEE port");

        this.ipLabel = new Label("Detecting IP address...");
        this.ipLabel.addClassSelector(RESULT);
        main.addChild(this.ipLabel);

        Button ifBtn = new Button("Refresh  -  List network interfaces");
        ifBtn.addClassSelector(ACTION_BTN);
        ifBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { listInterfaces(); }
        });
        main.addChild(ifBtn);

        section(main, "\u25b6  HTTP GET  \u2192  " + TARGET_HOST);
        info(main, "Target: http://" + TARGET_HOST + TARGET_PATH);

        Button targetBtn = new Button("Connect: http://" + TARGET_HOST + TARGET_PATH);
        targetBtn.addClassSelector(ACTION_BTN);
        targetBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { doHttpGetTarget(); }
        });
        main.addChild(targetBtn);

        this.resultLabel = new Label("Press button or wait for auto-run...");
        this.resultLabel.addClassSelector(RESULT);
        main.addChild(this.resultLabel);

        section(main, "\u25b6  Call log");
        this.logList = new List(LayoutOrientation.VERTICAL);
        main.addChild(this.logList);

        listInterfaces();

        doHttpGetTarget();

//        Button localBtn = new Button("Local host  (InetAddress.getLocalHost)");
//        localBtn.addClassSelector(ACTION_BTN);
//        localBtn.setOnClickListener(new OnClickListener() {
//            @Override public void onClick() { readLocalHost(); }
//        });
//        main.addChild(localBtn);
//
//        section(main, "\u25b6  TCP / DNS  (" + DEMO_HOST + ")");
//        info(main, "InetAddress.getByName  \u2192  DNS via lwIP");
//
//        Button dnsBtn = new Button("DNS lookup  (getByName)");
//        dnsBtn.addClassSelector(ACTION_BTN);
//        dnsBtn.setOnClickListener(new OnClickListener() {
//            @Override public void onClick() { doDnsLookup(); }
//        });
//        main.addChild(dnsBtn);
//
//        Button tcpBtn = new Button("TCP connect  port 80");
//        tcpBtn.addClassSelector(ACTION_BTN);
//        tcpBtn.setOnClickListener(new OnClickListener() {
//            @Override public void onClick() { doTcpConnect(); }
//        });
//        main.addChild(tcpBtn);
//
//        Button httpBtn = new Button("HTTP GET  /  (minimal request)");
//        httpBtn.addClassSelector(ACTION_BTN);
//        httpBtn.setOnClickListener(new OnClickListener() {
//            @Override public void onClick() { doHttpGet(); }
//        });
//        main.addChild(httpBtn);
//
//        section(main, "\u25b6  TLS  (" + DEMO_HOST + ":" + HTTPS_PORT + ")");
//        info(main, "SSLSocket  \u2192  LLNET_SSL  \u2192  mbedTLS");
//
//        Button tlsBtn = new Button("TLS handshake  (HTTPS port)");
//        tlsBtn.addClassSelector(ACTION_BTN);
//        tlsBtn.setOnClickListener(new OnClickListener() {
//            @Override public void onClick() { doTlsHandshake(); }
//        });
//        main.addChild(tlsBtn);
//
//        section(main, "\u25b6  Wi-Fi (ECOM)");
//        info(main, "AP scan / join needs ej.api:ecom-wifi + WLAN on the board; not called from this page.");
//        Button wifiNote = new Button("Wi-Fi scan  (see Foundation doc)");
//        wifiNote.addClassSelector(ACTION_BTN);
//        wifiNote.setOnClickListener(new OnClickListener() {
//            @Override public void onClick() {
//                log("[WiFi] Use ECOM-WIFI API when STA is configured (ej.api:ecom-wifi).");
//                result("Wi-Fi: add ecom-wifi calls or native WPL_Scan from BSP when needed.");
//            }
//        });
//        main.addChild(wifiNote);


        // ── Auto-run: show all info by default (non-blocking sequence) ──
        // Wrapped in try/catch so any UI-thread exception is visible on device.
        try {
            autoRunNetworkDiagnostics();
        } catch (Throwable t) {
            debugException("[AUTO] failed to start", t);
            result("Auto-run start failed: " + t.getClass().getSimpleName());
        }

        return main;
    }

    /**
     * Runs the basic network diagnostics automatically so the page is populated without user clicks.
     * Uses a Timer to avoid blocking UI thread for DNS/TCP/HTTP/TLS.
     */
    private void autoRunNetworkDiagnostics() {
        this.autoMode = true;
        this.lastStepStatus = 0;
        clearLog();
        log("Collecting network info...");

        // Fast / local calls first (should be quick)
//        safeStep("LOCAL", new Step() {
//            @Override public void run() { readLocalHost(false /*clear*/); }
//        });
//
//        // Remote calls may block.
//        safeStep("DNS", new Step() {
//            @Override public void run() { doDnsLookup(false /*clear*/); }
//        });
//        safeStep("TCP", new Step() {
//            @Override public void run() { doTcpConnect(false /*clear*/); }
//        });
//        safeStep("HTTP", new Step() {
//            @Override public void run() { doHttpGet(false /*clear*/); }
//        });
//        safeStep("TLS", new Step() {
//            @Override public void run() { doTlsHandshake(false /*clear*/); }
//        });
//        safeStep("TARGET", new Step() {
//            @Override public void run() { doHttpGetTarget(false /*clear*/); }
//        });

        result("Network check complete.");
        debugPrint("[AUTO] complete");
        autoMode = false;
    }

    /** Simple runnable compatible with older source levels. */
    private interface Step { void run(); }

    /**
     * Runs a step, logs start/end, and prints exceptions both on-screen and to stdout.
     */
    private void safeStep(String name, Step step) {
        debugPrint("[AUTO] >>> " + name);

        // Reset status and let the step set it.
        this.lastStepStatus = 0;

        try {
            step.run();

            boolean ok = (this.lastStepStatus >= 0);

            // Keep the UI output clean and user-focused.
            if (ok) {
                log(name + ": OK");
                debugPrint("[AUTO] <<< " + name + " (OK)");
            } else {
                log(name + ": FAIL (" + this.lastStepStatus + ")");
                debugPrint("[AUTO] <<< " + name + " (FAIL:" + this.lastStepStatus + ")");
                // Make failure very visible.
                result(name + " failed (code " + this.lastStepStatus + ")");
            }
        } catch (Throwable t) {
            this.lastStepStatus = -999;
            log(name + ": FAIL (" + t.getClass().getSimpleName() + ")");
            debugException("[AUTO] step " + name + " failed", t);
            result(name + " failed: " + t.getClass().getSimpleName());
        }
    }

    private void debugPrint(String msg) {
        // On real device, this goes to serial console (and also shows in simulator console).
        try {
            System.out.println("[NetworkPage] " + msg);
        } catch (Throwable ignored) {
            // If stdout isn't available, ignore.
        }
    }

    private void debugException(String prefix, Throwable t) {
        try {
            System.out.println("[NetworkPage] " + prefix + ": " + t);
            t.printStackTrace();
        } catch (Throwable ignored) {
            // ignore
        }
        // Also show something in the on-screen log.
        log(prefix + ": " + t.getClass().getSimpleName() + (t.getMessage() != null ? (" - " + t.getMessage()) : ""));
    }

    // ---- Updated diagnostic methods: optional clearLog() ----

    private void listInterfaces() { listInterfaces(true); }

    private void listInterfaces(boolean clear) {
        this.lastStepStatus = 0;

        // ── 1. Native path: link state + IP + DHCP state from lwIP ──────────────
        String nativeIp = null;
        int dhcpState = -1;
        boolean linkUp = false;
        // Retry up to 4 times (0, 500ms, 1000ms, 1500ms) to allow PHY to detect link
        for (int attempt = 0; attempt <= 3; attempt++) {
            if (attempt > 0) {
                try { Thread.sleep(500); } catch (Throwable ignored) {}
                log("[IF] retry #" + attempt + "...");
            }
            try {
                linkUp = NativeDriverIntegration.isNetworkLinkUp() != 0;
                int flags = NativeDriverIntegration.getNetifFlags();
                int num   = NativeDriverIntegration.getNetifNum();
                dhcpState = NativeDriverIntegration.getDhcpState();
                int ipInt = NativeDriverIntegration.getNetworkIpInt();
                int linkMask = NativeDriverIntegration.getAllNetifLinkMask();
                String mac = NativeDriverIntegration.readNetifMac();

                log("[IF] netif" + num
                        + " flags=" + NativeDriverIntegration.describeNetifFlags(flags)
                        + " link=" + (linkUp ? "UP" : "DOWN"));
                log("[IF] DHCP: " + NativeDriverIntegration.describeDhcpState(dhcpState));
                log("[IF] IP=0x" + Integer.toHexString(ipInt)
                        + " allLinkMask=0x" + Integer.toHexString(linkMask));
                log("[IF] MAC=" + mac);
                debugPrint("[IF] netif" + num + " flags=0x" + Integer.toHexString(flags)
                        + " link=" + linkUp + " dhcp=" + dhcpState
                        + " ip=0x" + Integer.toHexString(ipInt)
                        + " allLinkMask=0x" + Integer.toHexString(linkMask)
                        + " mac=" + mac);

                if (ipInt != 0) {
                    nativeIp = NativeDriverIntegration.formatIpInt(ipInt);
                    log("[IF] IP: " + nativeIp);
                }
            } catch (Throwable t) {
                log("[IF] native call failed: " + t.getClass().getSimpleName());
                debugException("[IF] native", t);
            }
            if (linkUp || nativeIp != null) {
                break; // link is up, no need to retry
            }
        }

        // ── 2. Java fallback: NetworkInterface.getNetworkInterfaces() ────────────
        log("[IF] NetworkInterface.getNetworkInterfaces()");
        String javaIp = null;
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                String name = ni.getName();
                String upStr;
                try { upStr = ni.isUp() ? "up" : "down"; } catch (Throwable t) { upStr = "?"; }
                log("[IF] " + name + " (" + upStr + ")");
                Enumeration<InetAddress> ads = ni.getInetAddresses();
                while (ads.hasMoreElements()) {
                    InetAddress a = ads.nextElement();
                    String addr = a.getHostAddress();
                    log("[IF]   addr: " + addr);
                    boolean isUnspecified = a.isAnyLocalAddress()
                            || "0.0.0.0".equals(addr) || addr.startsWith("0.");
                    if (javaIp == null && !a.isLoopbackAddress() && !isUnspecified) {
                        javaIp = addr;
                    }
                }
            }
        } catch (SocketException e) {
            this.lastStepStatus = -1;
            log("[IF] Java scan ERROR: " + e.getMessage());
            debugException("[IF] SocketException", e);
        } catch (Throwable t) {
            this.lastStepStatus = -2;
            log("[IF] Java scan ERROR: " + t.getClass().getSimpleName());
            debugException("[IF] Exception", t);
        }

        // ── 3. Display based on real link + DHCP state ────────────────────────────
        int linkMaskForDisplay = -1;
        int netifNumForDisplay = -1;
        try {
            linkMaskForDisplay = NativeDriverIntegration.getAllNetifLinkMask();
            netifNumForDisplay = NativeDriverIntegration.getNetifNum();
        } catch (Throwable ignored) {}

        // Which port hint for user: num 0 = ENET0 100M (J4), num 1 = ENET1 1G (J43)
        String portHint = netifNumForDisplay == 0 ? " [plug cable into J4  (100M)]"
                        : netifNumForDisplay == 1 ? " [plug cable into J43 (1G)]" : "";

        if (nativeIp != null) {
            setIp("IP: " + nativeIp + "  [" + NativeDriverIntegration.describeDhcpState(dhcpState) + "]");
            debugPrint("[IF] Board IP (native): " + nativeIp);
        } else if (javaIp != null) {
            setIp("IP: " + javaIp + "  [Java stack]");
            debugPrint("[IF] Board IP (java): " + javaIp);
        } else if (!linkUp) {
            // Tell user exactly which port the firmware expects
            setIp("Link DOWN" + portHint + "  -  check cable / cape");
            debugPrint("[IF] Link physically DOWN. allLinkMask=0x"
                    + Integer.toHexString(linkMaskForDisplay)
                    + " netifNum=" + netifNumForDisplay);
        } else if (dhcpState == NativeDriverIntegration.DHCP_TIMEOUT) {
            setIp("Link UP  |  DHCP timeout  -  no DHCP server found");
        } else if (dhcpState == NativeDriverIntegration.DHCP_LINK_DOWN) {
            setIp("Link UP (LEDs) but lwIP sees link down  -  replug cable");
        } else {
            setIp("Link UP" + portHint + "  |  "
                    + NativeDriverIntegration.describeDhcpState(dhcpState)
                    + "  (press Refresh)");
        }
    }

    private void readLocalHost() { readLocalHost(true); }

    private void readLocalHost(boolean clear) {
        if (clear && !this.autoMode) {
            clearLog();
        }
        this.lastStepStatus = 0;
        log("[LOCAL] InetAddress.getLocalHost()");
        try {
            InetAddress lh = InetAddress.getLocalHost();
            log("[LOCAL] host: " + lh.getHostName());
            log("[LOCAL] addr: " + lh.getHostAddress());
            result("Local host: " + lh.getHostAddress());
        } catch (UnknownHostException e) {
            this.lastStepStatus = -10;
            log("[LOCAL] ERROR: " + e.getMessage());
            debugException("[LOCAL] UnknownHostException", e);
            result("getLocalHost failed: " + e.getMessage());
        } catch (Throwable t) {
            this.lastStepStatus = -11;
            log("[LOCAL] ERROR: " + t.getClass().getSimpleName());
            debugException("[LOCAL] Exception", t);
            result("getLocalHost failed: " + t.getClass().getSimpleName());
        }
    }

    private void doDnsLookup() { doDnsLookup(true); }

    private void doDnsLookup(boolean clear) {
        if (clear && !this.autoMode) {
            clearLog();
        }
        this.lastStepStatus = 0;
        log("[DNS] InetAddress.getByName(\"" + DEMO_HOST + "\")");
        try {
            InetAddress[] all = InetAddress.getAllByName(DEMO_HOST);
            for (int i = 0; i < all.length; i++) {
                log("[DNS] #" + (i + 1) + " " + all[i].getHostAddress());
            }
            if (all.length > 0) {
                result("DNS OK  \u2192  " + all[0].getHostAddress());
            } else {
                this.lastStepStatus = -21;
                result("DNS returned no addresses");
            }
        } catch (UnknownHostException e) {
            // In your log, lwIP returns NET error code -21 for DNS.
            this.lastStepStatus = -21;
            log("[DNS] ERROR: " + e.getMessage());
            debugException("[DNS] UnknownHostException", e);
            result("DNS failed: " + e.getMessage());
        } catch (Throwable t) {
            this.lastStepStatus = -22;
            log("[DNS] ERROR: " + t.getClass().getSimpleName());
            debugException("[DNS] Exception", t);
            result("DNS failed: " + t.getClass().getSimpleName());
        }
    }

    private void doTcpConnect() { doTcpConnect(true); }

    private void doTcpConnect(boolean clear) {
        if (clear && !this.autoMode) {
            clearLog();
        }
        this.lastStepStatus = 0;
        log("[TCP] new Socket(\"" + DEMO_HOST + "\", 80)");
        Socket s = null;
        try {
            s = new Socket(DEMO_HOST, 80);
            s.setSoTimeout(SOCKET_TIMEOUT_MS);
            log("[TCP] local: " + s.getLocalSocketAddress());
            log("[TCP] remote: " + s.getRemoteSocketAddress());
            log("[TCP] connected=" + s.isConnected());
            result("TCP OK  \u2192  " + s.getInetAddress().getHostAddress() + ":80");
        } catch (IOException e) {
            this.lastStepStatus = -30;
            log("[TCP] ERROR: " + e.getMessage());
            debugException("[TCP] IOException", e);
            result("TCP failed: " + e.getMessage());
        } catch (Throwable t) {
            this.lastStepStatus = -31;
            log("[TCP] ERROR: " + t.getClass().getSimpleName());
            debugException("[TCP] Exception", t);
            result("TCP failed: " + t.getClass().getSimpleName());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }

    private void doHttpGet() { doHttpGet(true); }

    private void doHttpGet(boolean clear) {
        if (clear && !this.autoMode) {
            clearLog();
        }
        this.lastStepStatus = 0;
        log("[HTTP] GET / HTTP/1.1  Host: " + DEMO_HOST);
        Socket s = null;
        try {
            s = new Socket(DEMO_HOST, 80);
            s.setSoTimeout(SOCKET_TIMEOUT_MS);
            OutputStream os = s.getOutputStream();
            String req = "GET / HTTP/1.1\r\nHost: " + DEMO_HOST + "\r\nConnection: close\r\n\r\n";
            os.write(req.getBytes());
            os.flush();

            InputStream in = s.getInputStream();
            byte[] buf = new byte[128];
            int total = 0;
            StringBuilder head = new StringBuilder();
            while (total < HTTP_READ_MAX) {
                int n = in.read(buf);
                if (n <= 0) {
                    break;
                }
                total += n;
                for (int i = 0; i < n && head.length() < 200; i++) {
                    char c = (char)(buf[i] & 0xFF);
                    if (c == '\r') {
                        continue;
                    }
                    head.append(c);
                }
            }
            String preview = head.toString().replace('\n', ' ');
            if (preview.length() > 120) {
                preview = preview.substring(0, 117) + "...";
            }
            log("[HTTP] read " + total + " B");
            log("[HTTP] " + preview);
            String status = extractStatusLine(head.toString());
            result("HTTP  \u2192  " + (status != null ? status : "response received"));
        } catch (IOException e) {
            this.lastStepStatus = -40;
            log("[HTTP] ERROR: " + e.getMessage());
            debugException("[HTTP] IOException", e);
            result("HTTP failed: " + e.getMessage());
        } catch (Throwable t) {
            this.lastStepStatus = -41;
            log("[HTTP] ERROR: " + t.getClass().getSimpleName());
            debugException("[HTTP] Exception", t);
            result("HTTP failed: " + t.getClass().getSimpleName());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }

    private void doHttpGetTarget() { doHttpGetTarget(true); }

    private void doHttpGetTarget(boolean clear) {
        if (clear && !this.autoMode) {
            clearLog();
        }
        this.lastStepStatus = 0;
        log("[TARGET] HTTP GET  http://" + TARGET_HOST + TARGET_PATH);
        Socket s = null;
        try {
            log("[TARGET] Resolving " + TARGET_HOST + "...");
            InetAddress addr = InetAddress.getByName(TARGET_HOST);
            log("[TARGET] Resolved  \u2192  " + addr.getHostAddress());

            s = new Socket(addr, 80);
            s.setSoTimeout(SOCKET_TIMEOUT_MS);
            log("[TARGET] Connected  \u2192  " + s.getRemoteSocketAddress());

            OutputStream os = s.getOutputStream();
            String req = "GET " + TARGET_PATH + " HTTP/1.1\r\n"
                    + "Host: " + TARGET_HOST + "\r\n"
                    + "Connection: close\r\n"
                    + "\r\n";
            os.write(req.getBytes());
            os.flush();
            log("[TARGET] Request sent");

            InputStream in = s.getInputStream();
            byte[] buf = new byte[128];
            int total = 0;
            StringBuilder head = new StringBuilder();
            while (total < HTTP_READ_MAX) {
                int n = in.read(buf);
                if (n <= 0) {
                    break;
                }
                total += n;
                for (int i = 0; i < n && head.length() < 200; i++) {
                    char c = (char)(buf[i] & 0xFF);
                    if (c == '\r') {
                        continue;
                    }
                    head.append(c);
                }
            }
            String preview = head.toString().replace('\n', ' ');
            if (preview.length() > 120) {
                preview = preview.substring(0, 117) + "...";
            }
            log("[TARGET] Read " + total + " bytes");
            log("[TARGET] " + preview);
            String status = extractStatusLine(head.toString());
            result("TARGET OK  \u2192  " + (status != null ? status : "response received"));
            debugPrint("[TARGET] status: " + status);
        } catch (UnknownHostException e) {
            this.lastStepStatus = -60;
            log("[TARGET] DNS FAIL: " + e.getMessage());
            debugException("[TARGET] DNS failed", e);
            result("TARGET DNS failed: " + e.getMessage());
        } catch (IOException e) {
            this.lastStepStatus = -61;
            log("[TARGET] ERROR: " + e.getMessage());
            debugException("[TARGET] IOException", e);
            result("TARGET failed: " + e.getMessage());
        } catch (Throwable t) {
            this.lastStepStatus = -62;
            log("[TARGET] ERROR: " + t.getClass().getSimpleName());
            debugException("[TARGET] Exception", t);
            result("TARGET failed: " + t.getClass().getSimpleName());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }

    private static String extractStatusLine(String raw) {
        int end = raw.indexOf('\n');
        if (end < 0) {
            end = raw.length();
        }
        if (end <= 0) {
            return null;
        }
        String line = raw.substring(0, end).trim();
        return line.length() > 0 ? line : null;
    }

    private void doTlsHandshake() { doTlsHandshake(true); }

    private void doTlsHandshake(boolean clear) {
        if (clear && !this.autoMode) {
            clearLog();
        }
        this.lastStepStatus = 0;
        log("TLS handshake: " + DEMO_HOST + ":" + HTTPS_PORT);
        SSLSocket sock = null;
        try {
            SocketFactory fac = SSLSocketFactory.getDefault();
            Socket raw = fac.createSocket(DEMO_HOST, HTTPS_PORT);
            if (!(raw instanceof SSLSocket)) {
                this.lastStepStatus = -50;
                raw.close();
                result("TLS: factory did not return SSLSocket");
                return;
            }
            sock = (SSLSocket) raw;
            sock.setSoTimeout(SOCKET_TIMEOUT_MS);
            log("Starting handshake...");
            sock.startHandshake();
            log("Handshake complete");
            result("TLS: OK");
        } catch (IOException e) {
            // Common on devices without any configured trust store.
            if (isEmptyTrustAnchors(e)) {
                // Mark as warning/partial rather than hard failure.
                this.lastStepStatus = 1;
                log("TLS skipped: no trust store (trustAnchors empty)");
                result("TLS: no trust store configured");
            } else {
                this.lastStepStatus = -51;
                log("TLS error: " + e.getMessage());
                result("TLS failed: " + e.getMessage());
            }
            debugException("[TLS] IOException", e);
        } catch (Throwable t) {
            this.lastStepStatus = -52;
            log("TLS error: " + t.getClass().getSimpleName());
            debugException("[TLS] Exception", t);
            result("TLS failed: " + t.getClass().getSimpleName());
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }

    private static boolean isEmptyTrustAnchors(Throwable t) {
        // MicroEJ wraps native TLS issues, message often contains:
        // "InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty"
        while (t != null) {
            String m = t.getMessage();
            if (m != null && m.indexOf("trustAnchors") >= 0 && m.indexOf("non-empty") >= 0) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private static String cleanUiLogLine(String t) {
        if (!CLEAN_UI_LOG || t == null) {
            return t;
        }
        // Strip leading "[TAG] " prefixes.
        // Examples: "[AUTO] foo" -> "foo", "[DNS] #1 1.2.3.4" -> "#1 1.2.3.4"
        int len = t.length();
        int i = 0;
        while (i < len) {
            // Skip spaces
            while (i < len && t.charAt(i) == ' ') {
                i++;
            }
            if (i < len && t.charAt(i) == '[') {
                int end = t.indexOf(']', i + 1);
                if (end > i) {
                    i = end + 1;
                    // Skip a single space after the tag
                    if (i < len && t.charAt(i) == ' ') {
                        i++;
                    }
                    // Continue stripping consecutive tags if any
                    continue;
                }
            }
            break;
        }
        return t.substring(i);
    }

    private void log(String t) {
        // Keep more lines so device exceptions aren't lost.
        if (this.logCount++ >= LOG_MAX_LINES) {
            // Stop adding to avoid unbounded growth on device.
            if (this.logCount == LOG_MAX_LINES + 1) {
                Label l = new Label(cleanUiLogLine("[LOG] (truncated after " + LOG_MAX_LINES + " lines)"));
                l.addClassSelector(LOG);
                this.logList.addChild(l);
                this.logList.requestRender();
            }
            return;
        }

        String line = cleanUiLogLine(t);
        Label l = new Label(line);
        l.addClassSelector(LOG);
        this.logList.addChild(l);
        this.logList.requestRender();
    }

    /** Updates the highlighted result banner. */
    private void result(String t) {
        if (this.resultLabel != null) {
            this.resultLabel.setText(t != null ? t : "");
            this.resultLabel.requestRender();
        }
    }

    /** Updates the IP address banner. */
    private void setIp(String t) {
        if (this.ipLabel != null) {
            this.ipLabel.setText(t != null ? t : "");
            this.ipLabel.requestRender();
        }
    }

    /** Clears the on-screen log list. */
    private void clearLog() {
        this.logCount = 0;
        if (this.logList != null) {
            this.logList.removeAllChildren();
            this.logList.requestRender();
        }
    }

    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
