package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import ej.bon.Util;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.event.Event;
import ej.microui.event.generator.Buttons;
import ej.microui.event.generator.Pointer;
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

public class TouchHardwarePage implements Page {

    private static final int CANVAS     = 3500;
    private static final int INFO       = 3501;
    private static final int ACTION_BTN = 3502;

    private TouchCanvas canvas;

    @Override public String getName()        { return "Touch"; }
    @Override public String getDescription() { return "Raw pointer events from touch IC via I2C native driver"; }
    @Override public int    getAccentColor() { return AppStyle.TEAL; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(CANVAS));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEAL);
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s.setPadding(new UniformOutline(6));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(300, 44));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(AppStyle.TEXT_WHITE);
        s.setPadding(new UniformOutline(8));
        s.setBorder(new RectangularBorder(AppStyle.TEAL, 1));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        Label hint = new Label("Touch / drag inside the box below  ->  raw XY coords & event stats update live");
        hint.addClassSelector(INFO);
        main.addChild(hint);

        Button clearBtn = new Button("Clear Stats");
        clearBtn.addClassSelector(ACTION_BTN);
        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                TouchHardwarePage.this.canvas.clearStats();
            }
        });
        main.addChild(clearBtn);

        this.canvas = new TouchCanvas();
        this.canvas.addClassSelector(CANVAS);
        main.addChild(this.canvas);

        return main;
    }

    static class TouchCanvas extends Widget implements Animation {
        private int rawX = -1, rawY = -1;
        private boolean touching;
        private int totalEvts, presses, drags, releases;
        private String lastAction = "NONE";
        private int eps, epsCounter;
        private long epsTime;

        TouchCanvas() { super(true); }

        void clearStats() {
            this.rawX = -1; this.rawY = -1;
            this.touching = false;
            this.totalEvts = 0; this.presses = 0; this.drags = 0; this.releases = 0;
            this.eps = 0; this.epsCounter = 0;
            this.lastAction = "NONE";
            requestRender();
        }

        @Override protected void computeContentOptimalSize(Size size) { size.setSize(680, 520); }

        @Override protected void onShown()  { this.epsTime = Util.platformTimeMillis(); getDesktop().getAnimator().startAnimation(this); }
        @Override protected void onHidden() { getDesktop().getAnimator().stopAnimation(this); }

        @Override
        public boolean tick(long now) {
            if (now - this.epsTime >= 1000) {
                this.eps = this.epsCounter;
                this.epsCounter = 0;
                this.epsTime = now;
                requestRender();
            }
            return true;
        }

        @Override
        public boolean handleEvent(int event) {
            if (Event.getType(event) == Pointer.EVENT_TYPE) {
                Pointer ptr = (Pointer) Event.getGenerator(event);
                this.rawX = ptr.getX(); this.rawY = ptr.getY();
                int action = Buttons.getAction(event);
                this.totalEvts++; this.epsCounter++;
                if      (action == Buttons.PRESSED)  { this.touching = true;  this.presses++;  this.lastAction = "PRESSED";  }
                else if (action == Pointer.DRAGGED)  { this.drags++;           this.lastAction = "DRAGGED";  }
                else if (action == Buttons.RELEASED) { this.touching = false; this.releases++; this.lastAction = "RELEASED"; }
                requestRender();
                return true;
            }
            return super.handleEvent(event);
        }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            g.setColor(AppStyle.BG_DARK);
            Painter.fillRectangle(g, 0, 0, w, h);

            int aW = 380, aH = 430, aX = 10, aY = 10;
            g.setColor(AppStyle.DIVIDER);
            Painter.drawRectangle(g, aX, aY, aW, aH);

            // Grid
            g.setColor(0x1A1A38);
            for (int x = aX; x <= aX + aW; x += 38) Painter.drawVerticalLine(g, x, aY, aH);
            for (int y = aY; y <= aY + aH; y += 43) Painter.drawHorizontalLine(g, aX, y, aW);

            g.setColor(AppStyle.TEAL);
            Painter.drawString(g, "[ Touch Area ]", getStyle().getFont(), aX + 10, aY + 8);

            if (this.rawX >= 0) {
                int lx = this.rawX - getAbsoluteX();
                int ly = this.rawY - getAbsoluteY();
                g.setColor(AppStyle.YELLOW);
                Painter.drawHorizontalLine(g, aX, ly, aW);
                Painter.drawVerticalLine(g, lx, aY, aH);
                g.setColor(this.touching ? AppStyle.RED : AppStyle.GREEN);
                Painter.fillCircle(g, lx - 10, ly - 10, 20);
            }

            // Stats panel (right)
            int px = aW + 30, py = 10, lh = 26;
            g.setColor(AppStyle.TEAL);
            Painter.drawString(g, "Raw Pointer Data", getStyle().getFont(), px, py); py += lh;
            g.setColor(AppStyle.TEXT_WHITE);
            Painter.drawString(g, "rawX: " + this.rawX, getStyle().getFont(), px, py); py += lh;
            Painter.drawString(g, "rawY: " + this.rawY, getStyle().getFont(), px, py); py += lh;
            g.setColor(this.touching ? AppStyle.RED : AppStyle.GREEN);
            Painter.drawString(g, "State: " + this.lastAction, getStyle().getFont(), px, py); py += lh + 10;

            g.setColor(AppStyle.TEAL);
            Painter.drawString(g, "Event Statistics", getStyle().getFont(), px, py); py += lh;
            g.setColor(AppStyle.TEXT_WHITE);
            Painter.drawString(g, "PRESSED:  " + this.presses,   getStyle().getFont(), px, py); py += lh;
            Painter.drawString(g, "DRAGGED:  " + this.drags,     getStyle().getFont(), px, py); py += lh;
            Painter.drawString(g, "RELEASED: " + this.releases,  getStyle().getFont(), px, py); py += lh;
            Painter.drawString(g, "Total:    " + this.totalEvts, getStyle().getFont(), px, py); py += lh;
            g.setColor(AppStyle.YELLOW);
            Painter.drawString(g, "Evt/sec:  " + this.eps,       getStyle().getFont(), px, py);
        }
    }
}
