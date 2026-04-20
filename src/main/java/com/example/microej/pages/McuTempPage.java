package com.example.microej.pages;

import com.example.microej.AppFonts;
import com.example.microej.AppStyle;
import com.example.microej.NativeDriverIntegration;
import com.example.microej.Page;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Colors;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.mwt.Widget;
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

public class McuTempPage implements Page {

    private static final int SECTION    = 3700;
    private static final int INFO       = 3701;
    private static final int ACTION_BTN = 3702;
    private static final int RESULT     = 3703;
    private static final int GAUGE      = 3704;

    private Label resultLabel;
    private TempGauge tempGauge;

    @Override public String getName()        { return "MCU Temp Sensor"; }
    @Override public String getDescription() { return "On-die TMPSNS (NXP fsl_tempsensor) via app SNI"; }
    @Override public int    getAccentColor() { return AppStyle.RED; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.RED);
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(5));

        s = ss.getSelectorStyle(new ClassSelector(GAUGE));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(340, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.RED, 1));
        s.setPadding(new UniformOutline(8));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "\u25b6  MCU on-die temperature  (TMPSNS)");
        info(main, "Reads ANADIG TMPSNS through MCUXpresso driver fsl_tempsensor (OTP trim).");
        info(main, "Java: NativeDriverIntegration.getMcuTempCentiCelsius()  \u2192  SNI  \u2192  TMPSNS");

        this.tempGauge = new TempGauge();
        this.tempGauge.addClassSelector(GAUGE);
        main.addChild(this.tempGauge);

        this.resultLabel = new Label("Tap Read to measure temperature");
        this.resultLabel.addClassSelector(RESULT);
        main.addChild(this.resultLabel);

        Button readOnce = new Button("Read MCU Temperature  (single)");
        readOnce.addClassSelector(ACTION_BTN);
        readOnce.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { readTemp(); }
        });
        main.addChild(readOnce);

        Button readCont = new Button("Continuous Read  \u2192  1 s interval");
        readCont.addClassSelector(ACTION_BTN);
        readCont.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                result("Continuous read started...");
                new Timer().schedule(new TimerTask() {
                    @Override public void run() { readTemp(); }
                }, 0, 1000);
            }
        });
        main.addChild(readCont);

        section(main, "\u25b6  Call stack");
        info(main, "TMPSNS_Init / StartMeasure / GetCurrentTemperature  (see native_driver_integration.c)");

        return main;
    }

    private void readTemp() {
        try {
            int centi = NativeDriverIntegration.getMcuTempCentiCelsius();
            if (centi == NativeDriverIntegration.MCU_TEMP_READ_INVALID) {
                result("Could not read MCU temperature (TMPSNS invalid or out of range).");
                return;
            }
            float tempC = centi / 100f;
            String label = (tempC < 30) ? " [COOL]" :
                           (tempC < 50) ? " [NORMAL]" :
                           (tempC < 70) ? " [WARM]" : " [HOT!]";
            result("Temp: " + formatCelsius(tempC) + " \u00b0C" + label + "  (hardware)");
            this.tempGauge.setTemp(tempC);
            this.tempGauge.requestRender();
        } catch (Throwable t) {
            result("Temperature read failed: " + t.getClass().getSimpleName());
        }
    }

    private static String formatCelsius(float tempC) {
        int t10 = (int)(tempC * 10f + (tempC >= 0f ? 0.5f : -0.5f));
        int whole = t10 / 10;
        int frac = Math.abs(t10 % 10);
        return whole + "." + frac;
    }

    private void result(String t) { this.resultLabel.setText(t); this.resultLabel.requestRender(); }
    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }

    static class TempGauge extends Widget {
        private float temp = 28f;

        void setTemp(float t) { this.temp = t; }

        private Font getSafeFont() {
            Font f = null;
            try {
                f = getStyle() != null ? getStyle().getFont() : null;
            } catch (Throwable t) {
                // ignore
            }
            if (f == null) {
                try {
                    f = AppFonts.getUiFont();
                } catch (Throwable t) {
                    // ignore
                }
            }
            if (f == null) {
                f = Font.getDefaultFont();
            }
            return f;
        }

        @Override protected void computeContentOptimalSize(Size size) { size.setSize(620, 80); }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            Font font = getSafeFont();

            // Background track
            g.setColor(AppStyle.VALUE_BG);
            Painter.fillRectangle(g, 0, 24, w, 32);
            g.setColor(AppStyle.DIVIDER);
            Painter.drawRectangle(g, 0, 24, w - 1, 31);

            // Fill bar
            float ratio = Math.max(0f, Math.min(1f, this.temp / 100f));
            int bw = (int)(w * ratio);
            int color = this.temp < 30 ? AppStyle.BLUE
                      : this.temp < 50 ? AppStyle.GREEN
                      : this.temp < 70 ? AppStyle.ORANGE : AppStyle.RED;
            if (bw > 0) {
                g.setColor(color);
                Painter.fillRectangle(g, 0, 24, bw, 32);
            }

            // Tick labels
            g.setColor(AppStyle.TEXT_GRAY);
            Painter.drawString(g, "0\u00b0", font, 2, 58);
            Painter.drawString(g, "25\u00b0", font, w / 4 - 10, 58);
            Painter.drawString(g, "50\u00b0", font, w / 2 - 10, 58);
            Painter.drawString(g, "75\u00b0", font, w * 3 / 4 - 10, 58);
            Painter.drawString(g, "100\u00b0", font, w - 28, 58);

            // Tick marks
            g.setColor(AppStyle.DIVIDER);
            Painter.drawVerticalLine(g, w / 4, 22, 6);
            Painter.drawVerticalLine(g, w / 2, 22, 6);
            Painter.drawVerticalLine(g, w * 3 / 4, 22, 6);

            // Current value label at top
            g.setColor(color);
            Painter.drawString(g, McuTempPage.formatCelsius(this.temp) + " \u00b0C", font, 4, 4);
        }
    }
}
