package com.example.microej;

import com.example.microej.bench.StartupProbe;
import com.example.microej.pages.*;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.mwt.Desktop;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.NoBackground;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.CachedStylesheet;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.stylesheet.selector.TypeSelector;
import ej.mwt.util.Size;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.Grid;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;
import ej.widget.container.SimpleDock;

public class DemoApp {

    private static final int CARD_BTN     = 110;
    private static final int BACK_BTN     = 120;
    private static final int PAGE_TITLE   = 121;
    private static final int PAGE_TOP     = 122;

  /**
   * Set to a page index (0..PAGES.length-1) to auto-open a page after showing the menu.
   * Keep -1 to stay on the menu.
   */
  private static final int AUTO_START_PAGE = -1;

    private static final Page[] PAGES = {
            new DeviceInfoPage(),
            new DisplayHardwarePage(),
            new TouchHardwarePage(),
           new McuTempPage(),
            new VectorGraphicsPage(),
            new NetworkPage(),
            new AnimatedMascotPage(),
            new BenchmarkPage(),
    };

    private DemoApp() {}

    public static void showMenu() {
        CascadingStylesheet ss = new CascadingStylesheet();
        styleMenu(ss);

        HeaderWidget header = new HeaderWidget();

        Grid grid = new Grid(LayoutOrientation.HORIZONTAL, 2);
        for (int i = 0; i < PAGES.length; i++) {
            grid.addChild(makeCard(i));
        }

        SimpleDock root = new SimpleDock(LayoutOrientation.VERTICAL);
        root.setFirstChild(header);
        root.setCenterChild(grid);

        Desktop desktop = new Desktop();
        desktop.setStylesheet(new CachedStylesheet(ss));
        desktop.setWidget(root);
        Display.getDisplay().requestShow(desktop);
        try {
			StartupProbe.markUiFirstScreenShown();
			System.out.println("[App] First screen requested (menu)");
		} catch (Throwable ignored) {
			// ignore
		}

    if (AUTO_START_PAGE >= 0 && AUTO_START_PAGE < PAGES.length) {
      showPage(AUTO_START_PAGE);
    }
    }

    private static Widget makeCard(final int idx) {
        Page page = PAGES[idx];
        CardWidget card = new CardWidget(page.getName(), page.getDescription(), page.getAccentColor());
        card.addClassSelector(CARD_BTN);
        card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                showPage(idx);
            }
        });
        return card;
    }

    public static void showPage(final int idx) {
        Page page = PAGES[idx];
        CascadingStylesheet ss = new CascadingStylesheet();
        stylePage(ss, page.getAccentColor());
        page.populateStylesheet(ss);

        Button backBtn = new Button("< Menu");
        backBtn.addClassSelector(BACK_BTN);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                System.out.println("[App] Back to menu");
                showMenu();
            }
        });

        Label title = new Label(page.getName());
        title.addClassSelector(PAGE_TITLE);

        // Top bar layout: keep back button visible, keep stats visible on small panels.
        // SimpleDock(HORIZONTAL) lets the center shrink (title) instead of clipping the right side.
        SimpleDock topBar = new SimpleDock(LayoutOrientation.HORIZONTAL);
        topBar.addClassSelector(PAGE_TOP);
        topBar.setFirstChild(backBtn);
        topBar.setCenterChild(title);
        topBar.setLastChild(new SystemStatsWidget());

        Widget content = page.getContentWidget();

        SimpleDock root = new SimpleDock(LayoutOrientation.VERTICAL);
        root.setFirstChild(topBar);
        root.setCenterChild(content);

        Desktop desktop = new Desktop();
        desktop.setStylesheet(new CachedStylesheet(ss));
        desktop.setWidget(root);
        Display.getDisplay().requestShow(desktop);
        try {
			// If the app boots straight into a page, or if menu wasn't shown for some reason,
			// we still want a stable startup mark.
			StartupProbe.markUiFirstScreenShown();
		} catch (Throwable ignored) {
			// ignore
		}
    }

    private static void styleMenu(CascadingStylesheet ss) {
        EditableStyle s;

        s = ss.getDefaultStyle();
        s.setFont(Font.getFont("/fonts/SourceSansPro_22px-400.ejf"));

        s = ss.getSelectorStyle(new TypeSelector(SimpleDock.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new TypeSelector(Grid.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new TypeSelector(Label.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setColor(AppStyle.TEXT_LIGHT);

        s = ss.getSelectorStyle(new TypeSelector(Button.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new ClassSelector(CARD_BTN));
        s.setBackground(NoBackground.NO_BACKGROUND);
        s.setDimension(new FixedDimension(348, 200));
        s.setMargin(new UniformOutline(4));
    }

    private static void stylePage(CascadingStylesheet ss, int accent) {
        EditableStyle s;

        s = ss.getDefaultStyle();
        s.setFont(Font.getFont("/fonts/SourceSansPro_22px-400.ejf"));

        // Global dark background for all container/widget types — prevents MWT default white
        s = ss.getSelectorStyle(new TypeSelector(SimpleDock.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new TypeSelector(List.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));

        s = ss.getSelectorStyle(new TypeSelector(Button.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setColor(AppStyle.TEXT_LIGHT);

        s = ss.getSelectorStyle(new TypeSelector(Label.class));
        s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
        s.setColor(AppStyle.TEXT_LIGHT);

        // Top bar
        s = ss.getSelectorStyle(new ClassSelector(PAGE_TOP));
        s.setBackground(new RectangularBackground(AppStyle.BG_HEADER));
        s.setPadding(new UniformOutline(8));
        s.setBorder(new RectangularBorder(accent, 2));

        s = ss.getSelectorStyle(new ClassSelector(BACK_BTN));
        s.setDimension(new FixedDimension(110, 44));
        s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
        s.setColor(Colors.WHITE);
        s.setPadding(new UniformOutline(8));

        s = ss.getSelectorStyle(new ClassSelector(PAGE_TITLE));
        s.setBackground(new RectangularBackground(AppStyle.BG_HEADER));
        s.setColor(AppStyle.TEXT_WHITE);
        s.setPadding(new UniformOutline(10));
    }

    // Animated rainbow header bar
    static class HeaderWidget extends Widget implements Animation {
        private long startTime;
        private int frameTick;
        private static final int[] PALETTE = {
                AppStyle.CYAN, AppStyle.BLUE, AppStyle.PURPLE, AppStyle.PINK,
                AppStyle.ORANGE, AppStyle.YELLOW, AppStyle.GREEN, AppStyle.TEAL
        };

        @Override
        protected void computeContentOptimalSize(Size size) {
            size.setSize(720, AppStyle.HEADER_H);
        }

        @Override
        protected void onShown() {
            this.startTime = ej.bon.Util.platformTimeMillis();
            getDesktop().getAnimator().startAnimation(this);
        }

        @Override
        protected void onHidden() {
            getDesktop().getAnimator().stopAnimation(this);
        }

        @Override
        public boolean tick(long now) {
            this.frameTick++;
            if (this.frameTick % 2 == 0) requestRender();
            return true;
        }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            g.setColor(AppStyle.BG_HEADER);
            Painter.fillRectangle(g, 0, 0, w, h);

            long elapsed = ej.bon.Util.platformTimeMillis() - this.startTime;
            int offset = (int)(elapsed / 15) % w;
            int segW = w / PALETTE.length + 1;
            for (int i = 0; i < PALETTE.length + 1; i++) {
                int x = ((i * segW) - offset + w * 2) % w;
                g.setColor(PALETTE[i % PALETTE.length]);
                Painter.fillRectangle(g, x, h - 5, segW, 5);
            }

            g.setColor(AppStyle.TEXT_WHITE);
            Painter.drawString(g, "i.MX RT1170 EVK  -  MicroEJ Showcase", getStyle().getFont(), AppStyle.PADDING, 10);
            g.setColor(AppStyle.TEXT_DIM);
            Painter.drawString(g, "Java -> SNI -> BSP   " + PAGES.length + " feature demos   720 x 1280", getStyle().getFont(), AppStyle.PADDING, 46);
            g.setColor(AppStyle.CYAN);
            Painter.drawString(g, "Cortex-M7  1 GHz   MicroEJ VM   FreeRTOS", getStyle().getFont(), AppStyle.PADDING, 82);
        }
    }

    // Card button with colored stripe, title, description
    static class CardWidget extends Button {
        private final String cardTitle;
        private final String cardDesc;
        private final int accent;

        CardWidget(String title, String desc, int accent) {
            super("");
            this.cardTitle = title;
            this.cardDesc  = desc;
            this.accent    = accent;
        }

        @Override
        protected void renderContent(GraphicsContext g, int w, int h) {
            boolean pressed = isInState(ACTIVE);
            g.setColor(pressed ? AppStyle.BG_ACCENT : 0x1C1C30);
            Painter.fillRectangle(g, 0, 0, w, h);

            // Left accent stripe
            g.setColor(this.accent);
            Painter.fillRectangle(g, 0, 0, 6, h);

            // Card border
            g.setColor(AppStyle.CARD_STROKE);
            Painter.drawRectangle(g, 0, 0, w - 1, h - 1);

            // Accent dot top-right
            g.setColor(this.accent);
            Painter.fillCircle(g, w - 28, 8, 16);

            // Title
            g.setColor(AppStyle.TEXT_WHITE);
            Painter.drawString(g, this.cardTitle, getStyle().getFont(), 16, 12);

            // Divider
            g.setColor(AppStyle.DIVIDER);
            Painter.drawHorizontalLine(g, 16, 46, w - 30);

            // Description (word-wrap)
            g.setColor(AppStyle.TEXT_LIGHT);
            String[] lines = wrap(this.cardDesc, 22);
            for (int i = 0; i < lines.length && i < 4; i++) {
                if (lines[i] != null) {
                    Painter.drawString(g, lines[i], getStyle().getFont(), 16, 54 + i * 28);
                }
            }

            // CTA
            g.setColor(this.accent);
            Painter.drawString(g, "EXPLORE >", getStyle().getFont(), 16, h - 30);
        }

        private static String[] wrap(String text, int max) {
            String[] out = new String[4];
            int line = 0;
            while (text.length() > max && line < 3) {
                int cut = text.lastIndexOf(' ', max);
                if (cut < 1) cut = max;
                out[line++] = text.substring(0, cut).trim();
                text = text.substring(cut).trim();
            }
            if (line < 4) out[line] = text;
            return out;
        }
    }
}
