package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.UiClickLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class FileSystemPage implements Page {

    private static final int SECTION    = 4200;
    private static final int INFO       = 4201;
    private static final int ACTION_BTN = 4202;
    private static final int RESULT     = 4203;
    private static final int LOG        = 4204;

    private Label resultLabel;
    private List  logList;
    private int   logCount;
    private int   writeCount;

    private static final int LOG_MAX_LINES = 200;

    /** Enable/disable console debug for this page. */
    private static final boolean DEBUG_CONSOLE = true;

    @Override public String getName()        { return "File System"; }

    @Override
    public String getDescription() {
        return "File IO demo (simulated if java.io is unavailable in this VEE Port)";
    }

    @Override
    public int getAccentColor() {
        return AppStyle.YELLOW;
    }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.YELLOW);
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
        s.setBorder(new RectangularBorder(AppStyle.YELLOW, 1));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(LOG));
        s.setColor(AppStyle.TEAL);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(4));
    }

    @Override
    public Widget getContentWidget() {
        debugPrint("getContentWidget() enter");
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "\u25b6  File System");
        info(main, "Real device file I/O using MicroEJ FS (ej.io.*) backed by LLFS -> FatFS.");

        // Ensure /tmp exists (ignore error if already exists)
        try {
            File tmp = new File("/tmp");
            debugPrint("/tmp exists=" + tmp.exists());
            if (!tmp.exists()) {
                boolean ok = tmp.mkdir();
                debugPrint("mkdir(/tmp) -> " + ok);
                log("[FS] mkdir /tmp -> " + ok);
            }
        } catch (Throwable t) {
            debugException("[FS] mkdir /tmp", t);
        }

        Button writeBtn = new Button("Write File (/tmp/demo_N.txt)");
        writeBtn.addClassSelector(ACTION_BTN);
        writeBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("FileSystemPage", "Write File", "doWrite");
                debugPrint("click: Write");
                doWrite();
            }
        });
        main.addChild(writeBtn);

        Button readBtn = new Button("Read Last File");
        readBtn.addClassSelector(ACTION_BTN);
        readBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("FileSystemPage", "Read Last File", "doReadLast");
                debugPrint("click: Read");
                doReadLast();
            }
        });
        main.addChild(readBtn);

        Button listBtn = new Button("List /tmp");
        listBtn.addClassSelector(ACTION_BTN);
        listBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("FileSystemPage", "List /tmp", "doListTmp");
                debugPrint("click: List /tmp");
                doListTmp();
            }
        });
        main.addChild(listBtn);

        Button spaceBtn = new Button("Space (free/total) on /");
        spaceBtn.addClassSelector(ACTION_BTN);
        spaceBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("FileSystemPage", "Space on /", "doSpaceRoot");
                debugPrint("click: Space /");
                doSpaceRoot();
            }
        });
        main.addChild(spaceBtn);

        this.resultLabel = new Label("Ready");
        this.resultLabel.addClassSelector(RESULT);
        main.addChild(this.resultLabel);

        section(main, "\u25b6  Call Log");
        this.logList = new List(LayoutOrientation.VERTICAL);
        main.addChild(this.logList);

        // Auto show current state
        try {
            debugPrint("auto: doListTmp/doSpaceRoot/doWrite/doReadLast");
            doListTmp();
            doSpaceRoot();
            doWrite();
            doReadLast();
        } catch (Throwable t) {
            debugException("[FS] auto", t);
        }

        debugPrint("getContentWidget() exit");
        return main;
    }

    private void doWrite() {
        debugPrint("doWrite() enter");
        this.writeCount++;
        String path = "/tmp/demo_" + this.writeCount + ".txt";
        String data = "MicroEJ FS write #" + this.writeCount + " @ " + System.currentTimeMillis() + "\n";

        log("[FS] FileOutputStream(\"" + path + "\")");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            byte[] bytes = data.getBytes();
            out.write(bytes, 0, bytes.length);
            out.flush();
            result("Wrote " + bytes.length + " bytes -> " + path);
            debugPrint("doWrite() ok: " + path);
        } catch (IOException e) {
            debugException("[FS] write IOException", e);
            result("Write failed: " + e.getMessage());
        } catch (Throwable t) {
            debugException("[FS] write Exception", t);
            result("Write failed: " + t.getClass().getSimpleName());
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException ignored) { /* ignore */ }
            }
        }
    }

    private void doReadLast() {
        debugPrint("doReadLast() enter writeCount=" + this.writeCount);
        if (this.writeCount <= 0) {
            result("No file written yet");
            return;
        }
        String path = "/tmp/demo_" + this.writeCount + ".txt";
        log("[FS] FileInputStream(\"" + path + "\")");

        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            byte[] buf = new byte[256];
            int n = in.read(buf, 0, buf.length);
            if (n < 0) {
                result("Read EOF");
                return;
            }
            String preview = new String(buf, 0, n);
            if (preview.length() > 80) {
                preview = preview.substring(0, 77) + "...";
            }
            log("[FS] read " + n + " bytes: " + preview.replace('\n', ' '));
            result("Read OK: " + n + " bytes");
        } catch (IOException e) {
            debugException("[FS] read IOException", e);
            result("Read failed: " + e.getMessage());
        } catch (Throwable t) {
            debugException("[FS] read Exception", t);
            result("Read failed: " + t.getClass().getSimpleName());
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException ignored) { /* ignore */ }
            }
        }
    }

    private void doListTmp() {
        debugPrint("doListTmp() enter");
        log("[FS] new File(\"/tmp\").listFiles()");
        try {
            File dir = new File("/tmp");
            if (!dir.exists()) {
                result("/tmp does not exist");
                return;
            }
            File[] files = dir.listFiles();
            if (files == null) {
                result("listFiles() returned null");
                return;
            }
            log("[FS] /tmp entries: " + files.length);
            for (int i = 0; i < files.length && i < 20; i++) {
                File f = files[i];
                log("[FS]  - " + f.getPath() + (f.isDirectory() ? "/" : "") + "  (" + f.length() + " B)");
            }
            result("/tmp entries: " + files.length);
        } catch (Throwable t) {
            debugException("[FS] list Exception", t);
            result("List failed: " + t.getClass().getSimpleName());
        }
    }

    private void doSpaceRoot() {
        debugPrint("doSpaceRoot() enter");
        log("[FS] space on /  (getFreeSpace/getTotalSpace)");
        try {
            File root = new File("/");
            long free = root.getFreeSpace();
            long total = root.getTotalSpace();
            log("[FS] free=" + free + "  total=" + total);
            result("Space: free=" + free + " / total=" + total);
        } catch (Throwable t) {
            debugException("[FS] space Exception", t);
            result("Space failed: " + t.getClass().getSimpleName());
        }
    }

    private void debugPrint(String msg) {
        if (!DEBUG_CONSOLE) {
            return;
        }
        try {
            System.out.println("[FileSystemPage] " + msg);
        } catch (Throwable ignored) {
            // ignore
        }
    }

    private void debugException(String prefix, Throwable t) {
        try {
            System.out.println("[FileSystemPage] " + prefix + ": " + t);
            t.printStackTrace();
        } catch (Throwable ignored) {
            // ignore
        }
        log(prefix + ": " + t.getClass().getSimpleName() + (t.getMessage() != null ? (" - " + t.getMessage()) : ""));
    }

    private void result(String t) {
        // Avoid NPE if something calls result() before widget creation finishes.
        if (this.resultLabel != null) {
            this.resultLabel.setText(t != null ? t : "");
            this.resultLabel.requestRender();
        } else {
            debugPrint("result() before resultLabel init: " + t);
        }
    }

    private void log(String t) {
        // Avoid NPE if an exception happens before logList init.
        if (this.logList == null) {
            debugPrint("log() before logList init: " + t);
            return;
        }
        if (this.logCount++ >= LOG_MAX_LINES) {
            if (this.logCount == LOG_MAX_LINES + 1) {
                Label l = new Label("[LOG] (truncated after " + LOG_MAX_LINES + " lines)");
                l.addClassSelector(LOG);
                this.logList.addChild(l);
                this.logList.requestRender();
            }
            return;
        }
        Label l = new Label(t);
        l.addClassSelector(LOG);
        this.logList.addChild(l);
        this.logList.requestRender();
    }

    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
