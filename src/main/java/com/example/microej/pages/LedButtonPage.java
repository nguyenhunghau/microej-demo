package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.NativeDriverIntegration;
import com.example.microej.Page;
import ej.bon.Timer;
import ej.bon.TimerTask;
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

public class LedButtonPage implements Page {

    private static final int SECTION    = 3600;
    private static final int INFO       = 3601;
    private static final int ACTION_BTN = 3602;
    private static final int RESULT     = 3603;
    private static final int STATUS_ON  = 3604;
    private static final int STATUS_OFF = 3605;

    private Label resultLabel;
    private Label ledStatusLabel;
    private Label buttonStatusLabel;
    private boolean ledOn;
    private boolean simButton;

    @Override public String getName()        { return "LED & Button"; }
    @Override public String getDescription() { return "GPIO digital output (LED) and input (user button) via HAL"; }
    @Override public int    getAccentColor() { return AppStyle.ORANGE; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.ORANGE);
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(5));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(340, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.ORANGE, 1));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(STATUS_ON));
        s.setColor(AppStyle.GREEN);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.GREEN, 1));
        s.setPadding(new UniformOutline(6));

        s = ss.getSelectorStyle(new ClassSelector(STATUS_OFF));
        s.setColor(AppStyle.TEXT_GRAY);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));
        s.setPadding(new UniformOutline(6));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "\u25b6  LED  \u2192  GPIO Output  \u2192  NXP SDK (board.h)");
        info(main, "Port 9  Pin 3  |  Real board: USER_LED");
        info(main, "NativeDriverIntegration.setLedState()  \u2192  SNI  \u2192  C");

        this.ledStatusLabel = new Label("LED: OFF");
        this.ledStatusLabel.addClassSelector(STATUS_OFF);
        main.addChild(this.ledStatusLabel);

        Button toggleBtn = new Button("Toggle Real LED On / Off");
        toggleBtn.addClassSelector(ACTION_BTN);
        toggleBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { toggleLed(); }
        });
        main.addChild(toggleBtn);

        Button blinkBtn = new Button("Blink LED 5x  (Timer 300 ms)");
        blinkBtn.addClassSelector(ACTION_BTN);
        blinkBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { blinkLed(); }
        });
        main.addChild(blinkBtn);

        section(main, "\u25b6  User Button  \u2192  GPIO Input  \u2192  NXP SDK (board.h)");
        info(main, "Port 13 Pin 0  |  Real board: SW7 USER_BTN");

        this.buttonStatusLabel = new Label("Button: not polled");
        this.buttonStatusLabel.addClassSelector(STATUS_OFF);
        main.addChild(this.buttonStatusLabel);

        Button readBtn = new Button("Poll Button State");
        readBtn.addClassSelector(ACTION_BTN);
        readBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { readButton(); }
        });
        main.addChild(readBtn);

        this.resultLabel = new Label("Tap above to control GPIO");
        this.resultLabel.addClassSelector(RESULT);
        main.addChild(this.resultLabel);
        toggleLed();
        return main;
    }

    private void toggleLed() {
        this.ledOn = !this.ledOn;
        try {
            NativeDriverIntegration.setLedState(this.ledOn);
            this.ledStatusLabel.setText("LED: " + (this.ledOn ? "\u2605 ON  (Hardware)" : "OFF  (Hardware)"));
            result("setLedState(" + this.ledOn + ")  \u2192  SNI OK");
        } catch (Throwable t) {
            this.ledStatusLabel.setText("LED: " + (this.ledOn ? "\u2605 ON  (Simulated)" : "OFF  (Simulated)"));
            result("setLedState failed: " + t.getClass().getSimpleName() + " (using simulator)");
        }
        this.ledStatusLabel.removeClassSelector(this.ledOn ? STATUS_OFF : STATUS_ON);
        this.ledStatusLabel.addClassSelector(this.ledOn ? STATUS_ON : STATUS_OFF);
        this.ledStatusLabel.requestRender();
    }

    private void blinkLed() {
        result("Blinking LED 5x @ 300 ms...");
        new Timer().schedule(new TimerTask() {
            private int count;
            @Override public void run() {
                if (count >= 10) {
                    cancel();
                    ledStatusLabel.setText("LED: OFF  (blink done)");
                    ledStatusLabel.requestRender();
                    return;
                }
                boolean on = (count % 2 == 0);
                try {
                    NativeDriverIntegration.setLedState(on);
                } catch (Throwable ignored) {}
                ledStatusLabel.setText("LED: " + (on ? "\u2605 ON" : "OFF") + "  [" + (count / 2 + 1) + "/5]");
                ledStatusLabel.requestRender();
                count++;
            }
        }, 0, 300);
    }

    private void readButton() {
        try {
            this.simButton = NativeDriverIntegration.getButtonState();
            this.buttonStatusLabel.setText("Button: " + (this.simButton ? "\u25cf PRESSED" : "RELEASED") + "  (Hardware)");
            result("getButtonState()  \u2192  SNI OK  = " + this.simButton);
        } catch (Throwable t) {
            this.simButton = !this.simButton;
            this.buttonStatusLabel.setText("Button: " + (this.simButton ? "\u25cf PRESSED" : "RELEASED") + "  (Simulated)");
            result("getButtonState failed: " + t.getClass().getSimpleName() + " (using simulator)");
        }
        this.buttonStatusLabel.removeClassSelector(this.simButton ? STATUS_OFF : STATUS_ON);
        this.buttonStatusLabel.addClassSelector(this.simButton ? STATUS_ON : STATUS_OFF);
        this.buttonStatusLabel.requestRender();
    }

    private void result(String t) { this.resultLabel.setText(t); this.resultLabel.requestRender(); }
    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
