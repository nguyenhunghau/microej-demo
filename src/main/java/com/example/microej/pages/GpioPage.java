package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.UiClickLog;
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

public class GpioPage implements Page {

    private static final int SECTION    = 3200;
    private static final int INFO       = 3201;
    private static final int ACTION_BTN = 3203;
    private static final int RESULT     = 3204;

    private Label resultLabel;
    private boolean simDigital;
    private int simAnalog;

    @Override public String getName()        { return "GPIO"; }
    @Override public String getDescription() { return "Digital read/write and analog ADC via GPIO pack"; }
    @Override public int    getAccentColor() { return AppStyle.PURPLE; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.PURPLE); s.setPadding(new UniformOutline(6)); s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT); s.setPadding(new UniformOutline(4)); s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(320, 44));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE); s.setPadding(new UniformOutline(8));
        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW); s.setPadding(new UniformOutline(6)); s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.PURPLE, 1));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "> GPIO API -> com.nxp.api:gpio -> HAL");
        info(main, "Real board: GPIO.setMode / getDigitalValue / setDigitalValue / getAnalogValue");
        info(main, "Simulator: HAL pack not available - simulated responses shown");

        Button setModeBtn = new Button("setMode(PORT, PIN, OUTPUT)");
        setModeBtn.addClassSelector(ACTION_BTN);
        setModeBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("GpioPage", "setMode(PORT, PIN, OUTPUT)", "GPIO.setMode");
                result("GPIO.setMode(2,3,OUTPUT) -> HAL_GPIO_Init() OK");
            }
        });
        main.addChild(setModeBtn);

        Button readBtn = new Button("getDigitalValue()");
        readBtn.addClassSelector(ACTION_BTN);
        readBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("GpioPage", "getDigitalValue()", "GPIO.getDigitalValue");
                result("GPIO.getDigitalValue(2,3) = " + simDigital + " (sim)");
            }
        });
        main.addChild(readBtn);

        Button writeBtn = new Button("toggleDigitalValue()");
        writeBtn.addClassSelector(ACTION_BTN);
        writeBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("GpioPage", "toggleDigitalValue()", "GPIO.setDigitalValue");
                simDigital = !simDigital;
                result("GPIO.setDigitalValue(2,3," + (simDigital ? 1 : 0) + ") (sim)");
            }
        });
        main.addChild(writeBtn);

        Button analogBtn = new Button("getAnalogValue() - ADC 12-bit");
        analogBtn.addClassSelector(ACTION_BTN);
        analogBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("GpioPage", "getAnalogValue()", "GPIO.getAnalogValue");
                simAnalog = (simAnalog + 137) % 4096;
                result("GPIO.getAnalogValue(1,0) = " + simAnalog + " (0-4095) (sim)");
            }
        });
        main.addChild(analogBtn);

        this.resultLabel = new Label("Tap a button to simulate GPIO call");
        this.resultLabel.addClassSelector(RESULT); main.addChild(this.resultLabel);
        return main;
    }

    private void result(String t) { this.resultLabel.setText(t); this.resultLabel.requestRender(); }
    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t) { Label l = new Label(t); l.addClassSelector(INFO); p.addChild(l); }
}
