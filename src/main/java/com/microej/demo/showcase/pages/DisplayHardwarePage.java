package com.microej.demo.showcase.pages;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

import ej.bon.Util;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.util.Size;
import ej.widget.basic.Label;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class DisplayHardwarePage implements Page {

	private static final int SECTION = 3100;
	private static final int INFO = 3101;
	private static final int BENCHMARK = 3102;

	@Override
	public String getName() {
		return "Display Hardware";
	}

	@Override
	public void populateStylesheet(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(SECTION));
		style.setColor(AppStyle.CYAN);
		style.setPadding(new UniformOutline(3));

		style = stylesheet.getSelectorStyle(new ClassSelector(INFO));
		style.setColor(AppStyle.TEXT_WHITE);
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new ClassSelector(BENCHMARK));
		style.setPadding(new UniformOutline(2));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		// Display hardware info (native calls to display driver)
		Label section1 = new Label("Display API -> native LCD driver");
		section1.addClassSelector(SECTION);
		main.addChild(section1);

		Display display = Display.getDisplay();
		addInfo(main, "Display.getWidth(): " + display.getWidth() + " px");
		addInfo(main, "Display.getHeight(): " + display.getHeight() + " px");
		addInfo(main, "Total pixels: " + (display.getWidth() * display.getHeight()));
		addInfo(main, "Display object: " + display.getClass().getName());

		// Render benchmark (tests actual framebuffer throughput)
		Label section2 = new Label("Render Benchmark (native framebuffer)");
		section2.addClassSelector(SECTION);
		main.addChild(section2);

		BenchmarkWidget benchmark = new BenchmarkWidget();
		benchmark.addClassSelector(BENCHMARK);
		main.addChild(benchmark);

		return main;
	}

	private void addInfo(List parent, String text) {
		Label label = new Label(text);
		label.addClassSelector(INFO);
		parent.addChild(label);
	}

	static class BenchmarkWidget extends Widget implements Animation {
		private int frameCount;
		private long startTime;
		private long lastFpsTime;
		private int fps;
		private int drawOps;

		@Override
		protected void computeContentOptimalSize(Size size) {
			size.setSize(440, 120);
		}

		@Override
		protected void onShown() {
			this.startTime = Util.platformTimeMillis();
			this.lastFpsTime = this.startTime;
			getDesktop().getAnimator().startAnimation(this);
		}

		@Override
		protected void onHidden() {
			getDesktop().getAnimator().stopAnimation(this);
		}

		@Override
		public boolean tick(long platformTimeMillis) {
			this.frameCount++;

			if (platformTimeMillis - this.lastFpsTime >= 1000) {
				this.fps = this.frameCount;
				this.frameCount = 0;
				this.lastFpsTime = platformTimeMillis;
			}

			requestRender();
			return true;
		}

		@Override
		protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
			this.drawOps = 0;

			// Benchmark: fill rectangles (tests framebuffer write speed)
			int boxSize = 20;
			long elapsed = Util.platformTimeMillis() - this.startTime;
			int offset = (int) (elapsed / 50) % boxSize;

			for (int y = 0; y < contentHeight - 30; y += boxSize) {
				for (int x = 0; x < contentWidth; x += boxSize) {
					int idx = (x / boxSize + y / boxSize + offset) % 8;
					int[] colors = { AppStyle.RED, AppStyle.ORANGE, AppStyle.YELLOW, AppStyle.GREEN,
							AppStyle.CYAN, AppStyle.BLUE, AppStyle.PURPLE, AppStyle.PINK };
					g.setColor(colors[idx]);
					Painter.fillRectangle(g, x, y, boxSize - 1, boxSize - 1);
					this.drawOps++;
				}
			}

			// FPS counter
			g.setColor(AppStyle.BG_DARK);
			Painter.fillRectangle(g, 0, contentHeight - 25, contentWidth, 25);
			g.setColor(AppStyle.YELLOW);
			String fpsText = "FPS: " + this.fps + " | Draw ops/frame: " + this.drawOps
					+ " | Each fillRect -> native framebuffer write";
			Painter.drawString(g, fpsText, getStyle().getFont(), 5, contentHeight - 20);
		}
	}
}
