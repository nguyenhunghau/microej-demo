package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.NativeDriverIntegration;
import com.example.microej.Page;
import com.example.microej.UiClickLog;
import ej.bon.Util;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.util.Size;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class DisplayHardwarePage implements Page {

    private static final int SECTION    = 3100;
    private static final int INFO       = 3101;
    private static final int ACTION_BTN = 3102;
    private static final int RESULT     = 3103;

    private BenchWidget bench;
    private Button toggleBtn;

    @Override public String getName()        { return "Display"; }
    @Override public String getDescription() { return "Panel size, pixel format, backlight, BSP bridge, FPS bench"; }
    @Override public int    getAccentColor() { return AppStyle.BLUE; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.BLUE);
        s.setPadding(new UniformOutline(6));
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setPadding(new UniformOutline(4));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(320, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(AppStyle.TEXT_WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.BLUE, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.BLUE, 1));
        s.setPadding(new UniformOutline(8));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        // Controls
        this.toggleBtn = new Button("Start Benchmark");
        this.toggleBtn.addClassSelector(ACTION_BTN);
        main.addChild(this.toggleBtn);

        Button resetBtn = new Button("Reset Stats");
        resetBtn.addClassSelector(ACTION_BTN);
        resetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                UiClickLog.click("DisplayHardwarePage", "Reset Stats", "bench.reset");
                DisplayHardwarePage.this.bench.reset();
            }
        });
        main.addChild(resetBtn);

        // Benchmark widget
        this.bench = new BenchWidget();
        main.addChild(this.bench);

        this.toggleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                if (DisplayHardwarePage.this.bench.running) {
                    UiClickLog.click("DisplayHardwarePage", "Stop Benchmark", "bench.stop");
                    DisplayHardwarePage.this.bench.stop();
                    DisplayHardwarePage.this.toggleBtn.setText("Start Benchmark");
                } else {
                    UiClickLog.click("DisplayHardwarePage", "Start Benchmark", "bench.start");
                    DisplayHardwarePage.this.bench.start();
                    DisplayHardwarePage.this.toggleBtn.setText("Stop Benchmark");
                }
                DisplayHardwarePage.this.toggleBtn.requestRender();
            }
        });

        // Display info (values come from the VEE port / LLUI + panel driver at runtime)
        Label s1 = new Label("> Panel & MicroUI Display  (runtime API)");
        s1.addClassSelector(SECTION);
        main.addChild(s1);

        Display d = Display.getDisplay();
        int w = d.getWidth();
        int h = d.getHeight();
        info(main, "Width x height:  " + w + " x " + h + " px");
        info(main, "Total pixels:    " + (long) w * h);
        info(main, "Pixel depth:     " + d.getPixelDepth() + " bpp (MicroUI getPixelDepth)");
        info(main, "Color display:   " + d.isColor() + "   |   reported colors: " + d.getNumberOfColors());
        info(main, "Double-buffered: " + d.isDoubleBuffered());

        if (d.hasBacklight()) {
            try {
                info(main, "Backlight:       level " + d.getBacklight() + " (driver reports dimmable)");
            } catch (Throwable t) {
                info(main, "Backlight:       present, level [n/a: " + t.getClass().getSimpleName() + "]");
            }
        } else {
            info(main, "Backlight:       not exposed by this LLUI port");
        }

        try {
            info(main, "Contrast:        " + d.getContrast());
        } catch (Throwable t) {
            info(main, "Contrast:        [n/a: " + t.getClass().getSimpleName() + "]");
        }

        Label sBsp = new Label("> BSP / Java native bridge  (SNI)");
        sBsp.addClassSelector(SECTION);
        main.addChild(sBsp);

        try {
            int panelId = NativeDriverIntegration.getBspPanelId();
            info(main, "BSP panel (DEMO_PANEL):  " + NativeDriverIntegration.describeBspPanel(panelId));
            info(main, "BSP panel id (raw):      " + panelId);
        } catch (Throwable t) {
            info(main, "BSP panel:                 [n/a: " + t.getClass().getSimpleName() + "]");
        }

        try {
            int ver = NativeDriverIntegration.getDriverVersion();
            if (ver >= 0) {
                info(main, "NativeDriverIntegration:  v" + formatBspVersionCode(ver) + "  (raw " + ver + ")");
            } else {
                info(main, "NativeDriverIntegration:  error code " + ver);
            }
        } catch (Throwable t) {
            info(main, "NativeDriverIntegration:  [n/a: " + t.getClass().getSimpleName() + "]");
        }

        String arch;
        try {
            arch = ej.util.Device.getArchitecture();
        } catch (Throwable t) {
            arch = "[n/a: " + t.getClass().getSimpleName() + "]";
        }
        info(main, "Device.getArchitecture(): " + arch);

        Label s2 = new Label("> Framebuffer benchmark  ->  fillRect throughput");
        s2.addClassSelector(SECTION);
        main.addChild(s2);

        info(main, "Tap 'Start Benchmark' to measure real-time FPS and draw ops");


        return main;
    }

    private void info(List p, String t) {
        Label l = new Label(t);
        l.addClassSelector(INFO);
        p.addChild(l);
    }

    /**
     * Maps {@code NativeDriverIntegration#getDriverVersion()} encoding (e.g. 100 -> 1.0.0) for display only.
     */
    private static String formatBspVersionCode(int v) {
        int major = v / 100;
        int minor = (v % 100) / 10;
        int patch = v % 10;
        return major + "." + minor + "." + patch;
    }

    static class BenchWidget extends Widget implements Animation {
        boolean running = false;
        private int frames, fps, drawOps;
        private long startTime, fpsTime;

        @Override
        protected void computeContentOptimalSize(Size size) { size.setSize(680, 320); }

        void start() {
            this.running = true;
            this.startTime = Util.platformTimeMillis();
            this.fpsTime = this.startTime;
            getDesktop().getAnimator().startAnimation(this);
        }

        void stop() {
            this.running = false;
            getDesktop().getAnimator().stopAnimation(this);
            requestRender();
        }

        void reset() {
            this.frames = 0;
            this.fps = 0;
            this.drawOps = 0;
            requestRender();
        }

        @Override
        protected void onHidden() {
            if (this.running) {
                getDesktop().getAnimator().stopAnimation(this);
                this.running = false;
            }
        }

        @Override
        public boolean tick(long now) {
            this.frames++;
            if (now - this.fpsTime >= 1000) {
                this.fps = this.frames;
                this.frames = 0;
                this.fpsTime = now;
            }
            requestRender();
            return true;
        }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            g.setColor(AppStyle.BG_DARK);
            Painter.fillRectangle(g, 0, 0, w, h);

            if (!this.running) {
                g.setColor(AppStyle.TEXT_DIM);
                Painter.drawString(g, "-- tap Start Benchmark --", getStyle().getFont(), w / 2 - 100, h / 2);
                return;
            }

            long elapsed = Util.platformTimeMillis() - this.startTime;
            int off = (int) (elapsed / 40) % 24;
            int[] colors = { AppStyle.RED, AppStyle.ORANGE, AppStyle.YELLOW, AppStyle.GREEN,
                    AppStyle.CYAN, AppStyle.BLUE, AppStyle.PURPLE, AppStyle.PINK };
            int bh = h - 50;
            this.drawOps = 0;
            for (int y = 0; y < bh; y += 24) {
                for (int x = 0; x < w; x += 24) {
                    int idx = ((x / 24 + y / 24 + off) % colors.length);
                    g.setColor(colors[idx]);
                    Painter.fillRectangle(g, x, y, 23, 23);
                    this.drawOps++;
                }
            }
            g.setColor(0x000000);
            Painter.fillRectangle(g, 0, bh, w, 50);
            g.setColor(AppStyle.YELLOW);
            Painter.drawString(g, "FPS: " + this.fps + "   Draw ops/frame: " + this.drawOps, getStyle().getFont(), 8, bh + 10);
            g.setColor(AppStyle.TEXT_LIGHT);
            Painter.drawString(g, "Each fillRect() = 1 native framebuffer write call", getStyle().getFont(), 8, bh + 30);
        }
    }
}
