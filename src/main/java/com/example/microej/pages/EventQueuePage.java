package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.UiClickLog;
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

public class EventQueuePage implements Page {

    private static final int SECTION    = 4500;
    private static final int INFO       = 4501;
    private static final int ACTION_BTN = 4502;
    private static final int RESULT     = 4503;
    private static final int EVT_CYAN   = 4504;
    private static final int EVT_ORANGE = 4505;

    private List  evtList;
    private Label resultLabel;
    private int   evtCount;
    private Timer contTimer;

    @Override public String getName()        { return "Event Queue"; }
    @Override public String getDescription() { return "Publish/subscribe events from BSP ISR to Java"; }
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
        s.setDimension(new FixedDimension(380, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.ORANGE, 1));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(EVT_CYAN));
        s.setColor(AppStyle.CYAN);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(5));

        s = ss.getSelectorStyle(new ClassSelector(EVT_ORANGE));
        s.setColor(AppStyle.ORANGE);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setPadding(new UniformOutline(5));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "\u25b6  Event Queue  \u2192  ej.api:event  \u2192  BSP ISR");
        info(main, "EventGenerator  \u2192  LLEVENT_IMPL  \u2192  LLMJVM_sendTaskActivation()");
        info(main, "C ISR  \u2192  LLEVENT_IMPL_sendEvent(type, data)  \u2192  Java wakes up");

        Button btn1 = new Button("Publish  USER_INPUT  event");
        btn1.addClassSelector(ACTION_BTN);
        btn1.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("EventQueuePage", "Publish USER_INPUT event", "publishEvent(USER_INPUT)");
                publishEvent("USER_INPUT", "touch x=360 y=640");
            }
        });
        main.addChild(btn1);

        Button btn2 = new Button("Publish  GPIO_IRQ  event  (port=8 pin=1)");
        btn2.addClassSelector(ACTION_BTN);
        btn2.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("EventQueuePage", "Publish GPIO_IRQ event", "publishEvent(GPIO_IRQ)");
                publishEvent("GPIO_IRQ", "port=8 pin=1 val=1");
            }
        });
        main.addChild(btn2);

        Button btn3 = new Button("Publish  TIMER_OVF  event  (ch=2)");
        btn3.addClassSelector(ACTION_BTN);
        btn3.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("EventQueuePage", "Publish TIMER_OVF event", "publishEvent(TIMER_OVF)");
                publishEvent("TIMER_OVF", "channel=2 period=10ms");
            }
        });
        main.addChild(btn3);

        Button btn4 = new Button("Publish  ADC_DONE  event  (ch=16)");
        btn4.addClassSelector(ACTION_BTN);
        btn4.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                UiClickLog.click("EventQueuePage", "Publish ADC_DONE event", "publishEvent(ADC_DONE)");
                publishEvent("ADC_DONE", "ch=16 raw=2048 T=35.2C");
            }
        });
        main.addChild(btn4);

        Button burstBtn = new Button("Burst 10 events/s  (tap again to stop)");
        burstBtn.addClassSelector(ACTION_BTN);
        burstBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                if (contTimer != null) {
                    UiClickLog.click("EventQueuePage", "Burst (stop)", "burstStop");
                    contTimer.cancel(); contTimer = null;
                    result("Burst stopped  (" + evtCount + " events total)");
                    return;
                }
                UiClickLog.click("EventQueuePage", "Burst (start)", "burstStart");
                contTimer = new Timer();
                contTimer.schedule(new TimerTask() {
                    @Override public void run() { publishEvent("BURST_EVT", "seq=" + evtCount); }
                }, 0, 100);
                result("Burst started  \u2192  10 events/s  (tap to stop)");
            }
        });
        main.addChild(burstBtn);

        this.resultLabel = new Label("Tap to publish events into the queue");
        this.resultLabel.addClassSelector(RESULT);
        main.addChild(this.resultLabel);

        section(main, "\u25b6  Event Log");
        this.evtList = new List(LayoutOrientation.VERTICAL);
        main.addChild(this.evtList);

        return main;
    }

    private void publishEvent(String type, String data) {
        this.evtCount++;
        if (this.evtCount > 30) { this.evtList.removeAllChildren(); this.evtCount = 1; }
        String msg = "[#" + this.evtCount + "]  " + type + "  " + data;
        Label l = new Label(msg);
        l.addClassSelector(this.evtCount % 2 == 0 ? EVT_CYAN : EVT_ORANGE);
        this.evtList.addChild(l);
        this.evtList.requestRender();
        result("Published #" + this.evtCount + ": " + type);
    }

    private void result(String t) { this.resultLabel.setText(t); this.resultLabel.requestRender(); }
    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
