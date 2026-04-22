/*
 * Animated Mascot page — wraps the NXP animatedMascot demo as an MWT Page.
 * Displays halo_green.png (PNG) and mascot.xml (VectorImage) with touch animations.
 */
package com.example.microej.pages;

import java.util.ArrayList;

import com.example.microej.AppStyle;
import com.example.microej.Page;

import ej.annotation.Nullable;
import ej.bon.Util;
import ej.microui.display.Colors;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.microui.event.Event;
import ej.microui.event.EventHandler;
import ej.microui.event.generator.Buttons;
import ej.microui.event.generator.Pointer;
import ej.microvg.Matrix;
import ej.microvg.VectorGraphicsPainter;
import ej.microvg.VectorImage;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.util.Size;

/**
 * Page that shows the animated MicroEJ mascot with a PNG halo and touch-ripple effects.
 */
public class AnimatedMascotPage implements Page {

    @Override
    public String getName() { return "Animated Mascot"; }

    @Override
    public String getDescription() { return "PNG image + VectorImage animation with touch effects"; }

    @Override
    public int getAccentColor() { return AppStyle.GREEN; }

    @Override
    public void populateStylesheet(CascadingStylesheet stylesheet) {
        // No custom selectors needed — widget draws everything directly.
    }

    @Override
    public Widget getContentWidget() {
        return new MascotWidget();
    }

    // -------------------------------------------------------------------------
    // Inner widget
    // -------------------------------------------------------------------------

    static class MascotWidget extends Widget implements Animation, EventHandler {

        private static final String MASCOT_PATH           = "/images/mascot.xml";           //$NON-NLS-1$
        private static final String TOUCH_ANIM_PATH       = "/images/touchAnimation.xml";   //$NON-NLS-1$
        private static final String HALO_PNG_PATH         = "/images/halo_green.png";       //$NON-NLS-1$

        @Nullable private VectorImage mascot;
        @Nullable private VectorImage touchImage;
        @Nullable private Image       haloImage;

        private long startTime;
        private final Matrix mascotMatrix = new Matrix();

        /** Running touch-ripple animations. */
        private final ArrayList<long[]> touchAnims = new ArrayList<>();
        // Each entry: { startTimeMillis, touchX_fp1000, touchY_fp1000, scale_fp1000 }

        // ---- Widget lifecycle -----------------------------------------------

        @Override
        protected void computeContentOptimalSize(Size size) {
            // Fill whatever space the container gives us.
            size.setSize(680, 500);
        }

        @Override
        protected void onShown() {
            // Load resources lazily so they are released when page is hidden.
            this.mascot     = VectorImage.getImage(MASCOT_PATH);
            this.touchImage = VectorImage.getImage(TOUCH_ANIM_PATH);
            this.haloImage  = ResourceImage.loadImage(HALO_PNG_PATH);

            this.startTime = Util.platformTimeMillis();
            buildMascotMatrix(getWidth(), getHeight());

            getDesktop().getAnimator().startAnimation(this);

            // Register as pointer event handler.
            ej.microui.event.EventGenerator[] gens =
                    ej.microui.event.EventGenerator.get(Pointer.class);
            for (ej.microui.event.EventGenerator gen : gens) {
                gen.setEventHandler(this);
            }
        }

        @Override
        protected void onHidden() {
            getDesktop().getAnimator().stopAnimation(this);

            // Remove our pointer handler.
            ej.microui.event.EventGenerator[] gens =
                    ej.microui.event.EventGenerator.get(Pointer.class);
            for (ej.microui.event.EventGenerator gen : gens) {
                gen.setEventHandler(null);
            }

            // Close resources.
            if (this.haloImage instanceof ResourceImage) {
                ((ResourceImage) this.haloImage).close();
            }
            this.mascot     = null;
            this.touchImage = null;
            this.haloImage  = null;
            this.touchAnims.clear();
        }

        // ---- Animation tick -------------------------------------------------

        @Override
        public boolean tick(long currentTimeMillis) {
            requestRender();
            return true;
        }

        // ---- Rendering ------------------------------------------------------

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            VectorImage m  = this.mascot;
            VectorImage ti = this.touchImage;
            Image       hi = this.haloImage;

            if (m == null || ti == null || hi == null) {
                return;
            }

            long now = Util.platformTimeMillis();

            // Background
            g.setColor(Colors.WHITE);
            Painter.fillRectangle(g, 0, 0, w, h);

            // Halo PNG — centred horizontally, placed at ~85 % of height
            int haloX = (w - hi.getWidth())  / 2;
            int haloY = (int) (h * 0.85f) - hi.getHeight() / 2;
            Painter.drawImage(g, hi, haloX, haloY);

            // Mascot vector animation
            int elapsed = (int) (now - this.startTime);
            VectorGraphicsPainter.drawAnimatedImage(g, m, this.mascotMatrix, elapsed);

            // Replay mascot animation once it finishes
            if (m.getDuration() < elapsed) {
                this.startTime = now;
            }

            // Touch ripple animations
            int i = 0;
            while (i < this.touchAnims.size()) {
                long[] a = this.touchAnims.get(i);
                int elapsedTouch = (int) (now - a[0]);
                if (ti.getDuration() < elapsedTouch) {
                    this.touchAnims.remove(i);
                } else {
                    Matrix mtx = buildTouchMatrix(a[1], a[2], a[3], ti);
                    VectorGraphicsPainter.drawAnimatedImage(g, ti, mtx, elapsedTouch);
                    i++;
                }
            }
        }

        // ---- Pointer events -------------------------------------------------

        @Override
        public boolean handleEvent(int event) {
            int type = Event.getType(event);
            if (type == Pointer.EVENT_TYPE) {
                int action = Buttons.getAction(event);
                if (action == Buttons.PRESSED) {
                    Pointer pointer = (Pointer) Event.getGenerator(event);
                    addTouchAnimation(pointer.getX(), pointer.getY());
                }
            }
            return true;
        }

        // ---- Helpers --------------------------------------------------------

        private void buildMascotMatrix(int widgetW, int widgetH) {
            VectorImage m = this.mascot;
            if (m == null) return;
            float scaleX = widgetW  / m.getWidth();
            float scaleY = widgetH  / m.getHeight();
            float scale  = Math.min(scaleX, scaleY);
            float tx = (widgetW  - m.getWidth()  * scale) / 2f;
            float ty = (widgetH  - m.getHeight() * scale) / 2f;
            this.mascotMatrix.setScale(scale, scale);
            this.mascotMatrix.postTranslate(tx, ty);
        }

        private void addTouchAnimation(int x, int y) {
            int h = getHeight();
            // Scale decreases as y increases (same formula as original TouchAnimation)
            float scale = 1.4f - (y / (float) h);
            // Store as fixed-point *1000 longs to avoid boxing
            this.touchAnims.add(new long[]{
                    Util.platformTimeMillis(),
                    x,
                    y,
                    (long) (scale * 1000)
            });
        }

        private static Matrix buildTouchMatrix(long xL, long yL, long scaleL, VectorImage image) {
            float x     = xL;
            float y     = yL;
            float scale = scaleL / 1000f;
            Matrix m = new Matrix();
            m.setScale(scale, scale);
            m.postTranslate(x - scale * image.getWidth()  / 2f,
                            y - scale * image.getHeight() / 2f);
            return m;
        }
    }
}
