package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.NativeDeviceInfo;
import com.example.microej.Page;
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
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;
import ej.widget.basic.OnClickListener;

public class DeviceInfoPage implements Page {

    private static final int SECTION    = 3000;
    private static final int KEY        = 3001;
    private static final int VALUE      = 3002;
    private static final int ACTION_BTN = 3003;
    private static final int RESULT     = 3004;

    // Dynamic labels — updated in-place, never removed
    private Label freeLabel;
    private Label totalLabel;
    private Label usedLabel;
    private Label timeLabel;
    private Label gcResult;

    @Override public String getName()        { return "Device & System"; }
    @Override public String getDescription() { return "Device ID, architecture, JVM memory, system props"; }
    @Override public int    getAccentColor() { return AppStyle.CYAN; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.CYAN);
        s.setPadding(new UniformOutline(6));
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));

        s = ss.getSelectorStyle(new ClassSelector(KEY));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setPadding(new UniformOutline(4));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new ClassSelector(VALUE));
        s.setColor(AppStyle.YELLOW);
        s.setPadding(new UniformOutline(4));
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(320, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(AppStyle.TEXT_WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.CYAN, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.CYAN);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.CYAN, 1));
        s.setPadding(new UniformOutline(8));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        // ── Static: Device API ──
        section(main, "> Device API  ->  SNI  ->  BSP");

        String arch;
        try { arch = ej.util.Device.getArchitecture(); }
        catch (Throwable t) { arch = "[n/a: " + t.getClass().getSimpleName() + "]"; }
        row(main, "Device.getArchitecture():", arch);

        String deviceId;
        try {
            byte[] id = ej.util.Device.getId();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(id.length, 8); i++) {
                if (i > 0) sb.append(':');
                String h = Integer.toHexString(id[i] & 0xFF);
                if (h.length() == 1) sb.append('0');
                sb.append(h.toUpperCase());
            }
            deviceId = sb.toString();
        } catch (Throwable t) { deviceId = "[n/a: " + t.getClass().getSimpleName() + "]"; }
        row(main, "Device.getId():", deviceId);

        // ── Dynamic: JVM Memory ──
        section(main, "> JVM Runtime  ->  native heap  (live)");

        Runtime rt = Runtime.getRuntime();
        this.freeLabel  = rowLive(main, "freeMemory():",  rt.freeMemory()  + " bytes");
        this.totalLabel = rowLive(main, "totalMemory():", rt.totalMemory() + " bytes");
        this.usedLabel  = rowLive(main, "usedMemory():",  (rt.totalMemory() - rt.freeMemory()) + " bytes");

        // ── Dynamic: System time ──
        section(main, "> System Properties");
        this.timeLabel = rowLive(main, "currentTimeMillis():", System.currentTimeMillis() + " ms");
        row(main, "file.encoding:",   prop("file.encoding"));
        row(main, "runtime.version:", prop("com.microej.runtime.version"));
        row(main, "os.name:",         prop("os.name"));

        // ── Buttons ──
        Button refreshBtn = new Button("Refresh Memory Stats");
        refreshBtn.addClassSelector(ACTION_BTN);
        refreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                Runtime r = Runtime.getRuntime();
                long free  = r.freeMemory();
                long total = r.totalMemory();
                DeviceInfoPage.this.freeLabel.setText(free  + " bytes");
                DeviceInfoPage.this.totalLabel.setText(total + " bytes");
                DeviceInfoPage.this.usedLabel.setText((total - free) + " bytes");
                DeviceInfoPage.this.timeLabel.setText(System.currentTimeMillis() + " ms");
                DeviceInfoPage.this.freeLabel.requestRender();
                DeviceInfoPage.this.totalLabel.requestRender();
                DeviceInfoPage.this.usedLabel.requestRender();
                DeviceInfoPage.this.timeLabel.requestRender();
            }
        });
        main.addChild(refreshBtn);

        Button gcBtn = new Button("Run GC  (System.gc())");
        gcBtn.addClassSelector(ACTION_BTN);
        gcBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                long before = Runtime.getRuntime().freeMemory();
                System.gc();
                long after  = Runtime.getRuntime().freeMemory();
                long freed  = after - before;
                DeviceInfoPage.this.gcResult
                        .setText("GC freed: " + freed + " bytes  |  free now: " + after + " bytes");
                DeviceInfoPage.this.gcResult.requestRender();
                // Also refresh memory rows
                Runtime r = Runtime.getRuntime();
                long total = r.totalMemory();
                long free  = r.freeMemory();
                DeviceInfoPage.this.freeLabel.setText(free  + " bytes");
                DeviceInfoPage.this.totalLabel.setText(total + " bytes");
                DeviceInfoPage.this.usedLabel.setText((total - free) + " bytes");
                DeviceInfoPage.this.freeLabel.requestRender();
                DeviceInfoPage.this.totalLabel.requestRender();
                DeviceInfoPage.this.usedLabel.requestRender();
            }
        });
        main.addChild(gcBtn);

        this.gcResult = new Label("-- press 'Run GC' to see freed bytes --");
        this.gcResult.addClassSelector(RESULT);
        main.addChild(this.gcResult);

        return main;
    }

    private static String prop(String k) {
        String v = System.getProperty(k);
        if (v == null) {
            try {
                switch (k) {
                    case "file.encoding":
                        v = NativeDeviceInfo.getFileEncoding(); // Use native method
                        break;
                    case "com.microej.runtime.version":
                        v = NativeDeviceInfo.getRuntimeVersion(); // Use native method
                        break;
                    case "os.name":
                        v = NativeDeviceInfo.getOSName(); // Use native method
                        break;
                    default:
                        v = "null";
                }
            } catch (Throwable t) {
                // Fallback to default values if native methods fail
                switch (k) {
                    case "file.encoding":
                        v = "UTF-8";
                        break;
                    case "com.microej.runtime.version":
                        v = "Unknown Runtime Version";
                        break;
                    case "os.name":
                        v = "Unknown OS";
                        break;
                    default:
                        v = "null";
                }
            }
        }
        return v;
    }

    private void section(List p, String t) {
        Label l = new Label(t);
        l.addClassSelector(SECTION);
        p.addChild(l);
    }

    // Static key-value row
    private void row(List p, String k, String v) {
        Label lk = new Label(k); lk.addClassSelector(KEY);
        Label lv = new Label(v); lv.addClassSelector(VALUE);
        List row = new List(LayoutOrientation.HORIZONTAL);
        row.addChild(lk); row.addChild(lv);
        p.addChild(row);
    }

    // Live key-value row — returns the value Label so caller can setText() it later
    private Label rowLive(List p, String k, String v) {
        Label lk = new Label(k); lk.addClassSelector(KEY);
        Label lv = new Label(v); lv.addClassSelector(VALUE);
        List row = new List(LayoutOrientation.HORIZONTAL);
        row.addChild(lk); row.addChild(lv);
        p.addChild(row);
        return lv;
    }
}
