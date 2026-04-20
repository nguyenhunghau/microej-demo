package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import ej.bon.Util;
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

public class VectorGraphicsPage implements Page {

    private static final int SECTION    = 4000;
    private static final int INFO       = 4001;
    private static final int ACTION_BTN = 4002;

    private VGWidget vgWidget;
    private Button pauseBtn;

    @Override public String getName()        { return "Vector Graphics"; }
    @Override public String getDescription() { return "MicroVG animated arcs, Drawing API arcs & ellipses"; }
    @Override public int    getAccentColor() { return AppStyle.INDIGO; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.INDIGO);
        s.setPadding(new UniformOutline(6));
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setPadding(new UniformOutline(4));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(200, 44));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(AppStyle.TEXT_WHITE);
        s.setPadding(new UniformOutline(8));
        s.setBorder(new RectangularBorder(AppStyle.INDIGO, 1));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        Label s1 = new Label("> Drawing API  ->  native GPU / framebuffer");
        s1.addClassSelector(SECTION);
        main.addChild(s1);

        info(main, "drawCircleArc()  ->  VGLite GPU / software renderer");
        info(main, "Animation: MWT Animator tick()  ->  requestRender()  ->  60 FPS");

        // Control row
        List row = new List(LayoutOrientation.HORIZONTAL);

        this.pauseBtn = new Button("Pause");
        this.pauseBtn.addClassSelector(ACTION_BTN);
        row.addChild(this.pauseBtn);

        Button resetBtn = new Button("Reset");
        resetBtn.addClassSelector(ACTION_BTN);
        row.addChild(resetBtn);

        main.addChild(row);

        this.vgWidget = new VGWidget();
        main.addChild(this.vgWidget);

        this.pauseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                if (VectorGraphicsPage.this.vgWidget.paused) {
                    VectorGraphicsPage.this.vgWidget.resume();
                    VectorGraphicsPage.this.pauseBtn.setText("Pause");
                } else {
                    VectorGraphicsPage.this.vgWidget.pause();
                    VectorGraphicsPage.this.pauseBtn.setText("Resume");
                }
                VectorGraphicsPage.this.pauseBtn.requestRender();
            }
        });

        resetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                VectorGraphicsPage.this.vgWidget.reset();
                if (VectorGraphicsPage.this.vgWidget.paused) {
                    VectorGraphicsPage.this.vgWidget.resume();
                    VectorGraphicsPage.this.pauseBtn.setText("Pause");
                    VectorGraphicsPage.this.pauseBtn.requestRender();
                }
            }
        });

        return main;
    }

    private void info(List p, String t) {
        Label l = new Label(t);
        l.addClassSelector(INFO);
        p.addChild(l);
    }

    static class VGWidget extends Widget implements Animation {
        private long startTime;
        private long pauseOffset;
        boolean paused = false;

        @Override protected void computeContentOptimalSize(Size size) { size.setSize(680, 500); }

        @Override
        protected void onShown() {
            this.startTime = Util.platformTimeMillis();
            getDesktop().getAnimator().startAnimation(this);
        }

        @Override
        protected void onHidden() {
            getDesktop().getAnimator().stopAnimation(this);
        }

        void pause() {
            this.paused = true;
            this.pauseOffset = Util.platformTimeMillis() - this.startTime;
            getDesktop().getAnimator().stopAnimation(this);
        }

        void resume() {
            this.paused = false;
            this.startTime = Util.platformTimeMillis() - this.pauseOffset;
            getDesktop().getAnimator().startAnimation(this);
        }

        void reset() {
            this.startTime = Util.platformTimeMillis();
            this.pauseOffset = 0;
            requestRender();
        }

        @Override
        public boolean tick(long now) { requestRender(); return true; }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            g.setColor(AppStyle.BG_DARK);
            Painter.fillRectangle(g, 0, 0, w, h);

            long elapsed = this.paused ? this.pauseOffset : Util.platformTimeMillis() - this.startTime;
            float t = elapsed / 1000f;

            int cx = w / 2, cy = h / 2 - 10;
            int[] colors = { AppStyle.INDIGO, AppStyle.PURPLE, AppStyle.CYAN, AppStyle.BLUE,
                             AppStyle.TEAL, AppStyle.GREEN, AppStyle.YELLOW, AppStyle.ORANGE, AppStyle.RED, AppStyle.PINK };

            for (int i = 0; i < colors.length; i++) {
                int r = 28 + i * 20;
                float speed = 60f + i * 30f;
                int startAngle = (int) ((t * speed) % 360);
                int arcAngle   = 90 + i * 18;
                g.setColor(colors[i]);
                Painter.drawCircleArc(g, cx - r, cy - r, r * 2, startAngle, arcAngle);
                int startAngle2 = (int) ((-t * speed * 0.7f + 180) % 360 + 360) % 360;
                g.setColor(dimColor(colors[i]));
                Painter.drawCircleArc(g, cx - r + 4, cy - r + 4, r * 2 - 8, startAngle2, arcAngle + 30);
            }

            float pulse = (float) Math.sin(t * 3) * 0.5f + 0.5f;
            int dotR = (int) (8 + pulse * 12);
            g.setColor(AppStyle.TEXT_WHITE);
            Painter.fillCircle(g, cx - dotR, cy - dotR, dotR * 2);

            g.setColor(this.paused ? AppStyle.ORANGE : AppStyle.TEXT_LIGHT);
            Painter.drawString(g, this.paused ? "[ PAUSED ]" : "drawCircleArc() x " + (colors.length * 2) + "  ->  native renderer",
                    getStyle().getFont(), 10, h - 30);
            g.setColor(AppStyle.INDIGO);
            Painter.drawString(g, "t = " + (int) (t * 10) / 10f + " s", getStyle().getFont(), 10, h - 50);
        }

        private static int dimColor(int color) {
            int r  = ((color >> 16) & 0xFF) / 3;
            int gg = ((color >> 8)  & 0xFF) / 3;
            int b  = (color & 0xFF) / 3;
            return (r << 16) | (gg << 8) | b;
        }
    }
}
