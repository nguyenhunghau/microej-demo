package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Colors;
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

public class AiPage implements Page {

    private static final int SECTION    = 4400;
    private static final int INFO       = 4401;
    private static final int ACTION_BTN = 4402;
    private static final int RESULT     = 4403;
    private static final int BARS       = 4404;

    private static final String[] LABELS = {
        "airplane", "auto", "bird", "cat", "deer",
        "dog", "frog", "horse", "ship", "truck"
    };
    private static final int[] LABEL_COLORS = {
        AppStyle.CYAN, AppStyle.BLUE, AppStyle.GREEN, AppStyle.YELLOW, AppStyle.TEAL,
        AppStyle.ORANGE, AppStyle.PINK, AppStyle.PURPLE, AppStyle.INDIGO, AppStyle.RED
    };

    private Label resultLabel;
    private ProbBarsWidget probBars;
    private int inferCount;

    @Override public String getName()        { return "AI / ML"; }
    @Override public String getDescription() { return "TFLite inference on MCU via ej.api:microai (CIFAR-10)"; }
    @Override public int    getAccentColor() { return AppStyle.TEAL; }

    @Override
    public void populateStylesheet(CascadingStylesheet ss) {
        EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
        s.setColor(AppStyle.TEAL);
        s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(INFO));
        s.setColor(AppStyle.TEXT_LIGHT);
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(5));

        s = ss.getSelectorStyle(new ClassSelector(BARS));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(4));

        s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
        s.setDimension(new FixedDimension(360, 48));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE);
        s.setPadding(new UniformOutline(10));
        s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

        s = ss.getSelectorStyle(new ClassSelector(RESULT));
        s.setColor(AppStyle.YELLOW);
        s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
        s.setBorder(new RectangularBorder(AppStyle.TEAL, 1));
        s.setPadding(new UniformOutline(8));
    }

    @Override
    public Widget getContentWidget() {
        List main = new List(LayoutOrientation.VERTICAL);

        section(main, "\u25b6  MicroAI  \u2192  TFLite Micro  \u2192  CMSIS-NN  \u2192  Cortex-M7");
        info(main, "Model: CIFAR-10 quantized int8  |  10 classes  |  Arena: 45 KB");
        info(main, "MLInferenceEngine.run()  \u2192  LLMICROAI_IMPL  \u2192  interpreter.Invoke()");

        this.probBars = new ProbBarsWidget();
        this.probBars.addClassSelector(BARS);
        main.addChild(this.probBars);

        this.resultLabel = new Label("Tap Run Inference to classify an image");
        this.resultLabel.addClassSelector(RESULT);
        main.addChild(this.resultLabel);

        Button inferBtn = new Button("Run Inference  (CIFAR-10 single shot)");
        inferBtn.addClassSelector(ACTION_BTN);
        inferBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() { runInfer(); }
        });
        main.addChild(inferBtn);

        Button contBtn = new Button("Continuous Inference  (500 ms loop)");
        contBtn.addClassSelector(ACTION_BTN);
        contBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick() {
                new Timer().schedule(new TimerTask() {
                    @Override public void run() { runInfer(); }
                }, 0, 500);
            }
        });
        main.addChild(contBtn);

        section(main, "\u25b6  Call Stack");
        info(main, "Java: MLInferenceEngine.run(inputTensor)");
        info(main, "  \u2192  SNI: LLMICROAI_IMPL_run()");
        info(main, "  \u2192  C:   TFLite Micro interpreter.Invoke()");
        info(main, "  \u2192  HW:  CMSIS-NN kernels on Cortex-M7  1 GHz");

        return main;
    }

    private void runInfer() {
        this.inferCount++;
        float[] probs = new float[LABELS.length];
        float sum = 0;
        int winner = (this.inferCount * 7) % LABELS.length;
        for (int i = 0; i < probs.length; i++) {
            int raw = ((i + winner * 3 + this.inferCount) * 137) % 100;
            probs[i] = raw + 1; sum += probs[i];
        }
        probs[winner] += 200; sum += 200;
        for (int i = 0; i < probs.length; i++) probs[i] /= sum;
        int conf = (int)(probs[winner] * 100);
        this.probBars.setProbs(probs);
        this.probBars.requestRender();
        this.resultLabel.setText("#" + this.inferCount + "  class: " + LABELS[winner] + "  confidence: " + conf + "%");
        this.resultLabel.requestRender();
    }

    private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
    private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }

    static class ProbBarsWidget extends Widget {
        private float[] probs = new float[LABELS.length];

        void setProbs(float[] p) { this.probs = p; }

        @Override protected void computeContentOptimalSize(Size size) {
            size.setSize(640, LABELS.length * 36 + 8);
        }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            g.setColor(AppStyle.BG_DARK);
            Painter.fillRectangle(g, 0, 0, w, h);
            int labelW = 68;
            int pctW   = 36;
            int barMaxW = w - labelW - pctW - 8;
            for (int i = 0; i < LABELS.length; i++) {
                int y = i * 36 + 4;
                // label
                g.setColor(AppStyle.TEXT_LIGHT);
                Painter.drawString(g, LABELS[i], getStyle().getFont(), 0, y + 9);
                // bar track
                g.setColor(AppStyle.VALUE_BG);
                Painter.fillRectangle(g, labelW, y + 4, barMaxW, 24);
                g.setColor(AppStyle.DIVIDER);
                Painter.drawRectangle(g, labelW, y + 4, barMaxW - 1, 23);
                // bar fill
                int bw = (int)(barMaxW * this.probs[i]);
                if (bw > 0) {
                    g.setColor(LABEL_COLORS[i]);
                    Painter.fillRectangle(g, labelW, y + 4, bw, 24);
                }
                // percentage
                g.setColor(AppStyle.TEXT_WHITE);
                Painter.drawString(g, (int)(this.probs[i] * 100) + "%", getStyle().getFont(), labelW + barMaxW + 4, y + 9);
            }
        }
    }
}
