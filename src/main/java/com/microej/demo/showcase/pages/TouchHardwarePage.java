package com.microej.demo.showcase.pages;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

import ej.bon.Util;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.event.Event;
import ej.microui.event.generator.Buttons;
import ej.microui.event.generator.Pointer;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.util.Size;

public class TouchHardwarePage implements Page {

	private static final int CANVAS = 3500;

	@Override
	public String getName() {
		return "Touch Hardware";
	}

	@Override
	public void populateStylesheet(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(CANVAS));
		style.setPadding(new UniformOutline(2));
	}

	@Override
	public Widget getContentWidget() {
		TouchCanvas canvas = new TouchCanvas();
		canvas.addClassSelector(CANVAS);
		return canvas;
	}

	static class TouchCanvas extends Widget implements Animation {
		private int rawX = -1;
		private int rawY = -1;
		private boolean touching;
		private int eventCount;
		private int pressCount;
		private int dragCount;
		private int releaseCount;
		private long lastEventTime;
		private String lastAction = "NONE";
		private int eventsPerSecond;
		private int epsCounter;
		private long epsLastTime;

		TouchCanvas() {
			super(true);
		}

		@Override
		protected void computeContentOptimalSize(Size size) {
			size.setSize(460, 230);
		}

		@Override
		protected void onShown() {
			this.epsLastTime = Util.platformTimeMillis();
			getDesktop().getAnimator().startAnimation(this);
		}

		@Override
		protected void onHidden() {
			getDesktop().getAnimator().stopAnimation(this);
		}

		@Override
		public boolean tick(long platformTimeMillis) {
			if (platformTimeMillis - this.epsLastTime >= 1000) {
				this.eventsPerSecond = this.epsCounter;
				this.epsCounter = 0;
				this.epsLastTime = platformTimeMillis;
				requestRender();
			}
			return true;
		}

		@Override
		public boolean handleEvent(int event) {
			int eventType = Event.getType(event);
			if (eventType == Pointer.EVENT_TYPE) {
				Pointer pointer = (Pointer) Event.getGenerator(event);
				this.rawX = pointer.getX();
				this.rawY = pointer.getY();
				int action = Buttons.getAction(event);
				this.eventCount++;
				this.epsCounter++;
				this.lastEventTime = Util.platformTimeMillis();

				if (action == Buttons.PRESSED) {
					this.touching = true;
					this.pressCount++;
					this.lastAction = "PRESSED";
				} else if (action == Pointer.DRAGGED) {
					this.dragCount++;
					this.lastAction = "DRAGGED";
				} else if (action == Buttons.RELEASED) {
					this.touching = false;
					this.releaseCount++;
					this.lastAction = "RELEASED";
				}

				requestRender();
				return true;
			}
			return super.handleEvent(event);
		}

		@Override
		protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
			g.setColor(AppStyle.BG_DARK);
			Painter.fillRectangle(g, 0, 0, contentWidth, contentHeight);

			// Title
			g.setColor(AppStyle.CYAN);
			Painter.drawString(g, "Touch IC -> native Pointer driver -> Java Event", getStyle().getFont(), 5, 5);

			// Touch area with crosshair
			int areaX = 5;
			int areaY = 22;
			int areaW = 200;
			int areaH = 160;

			g.setColor(AppStyle.DIVIDER);
			Painter.drawRectangle(g, areaX, areaY, areaW, areaH);

			// Grid
			g.setColor(0x222244);
			for (int x = areaX; x < areaX + areaW; x += 25) {
				Painter.drawVerticalLine(g, x, areaY, areaH);
			}
			for (int y = areaY; y < areaY + areaH; y += 25) {
				Painter.drawHorizontalLine(g, areaX, y, areaW);
			}

			// Crosshair if touching
			int localX = this.rawX - getAbsoluteX();
			int localY = this.rawY - getAbsoluteY();
			if (this.rawX >= 0) {
				g.setColor(AppStyle.YELLOW);
				Painter.drawHorizontalLine(g, areaX, localY, areaW);
				Painter.drawVerticalLine(g, localX, areaY, areaH);

				g.setColor(this.touching ? AppStyle.RED : AppStyle.GREEN);
				Painter.fillCircle(g, localX - 6, localY - 6, 12);
			}

			// Stats panel (right side)
			int px = 220;
			int py = 25;
			int lineH = 17;

			g.setColor(AppStyle.CYAN);
			Painter.drawString(g, "Raw Pointer Data:", getStyle().getFont(), px, py);
			py += lineH;

			g.setColor(AppStyle.TEXT_WHITE);
			Painter.drawString(g, "rawX (from touch IC): " + this.rawX, getStyle().getFont(), px, py);
			py += lineH;
			Painter.drawString(g, "rawY (from touch IC): " + this.rawY, getStyle().getFont(), px, py);
			py += lineH;

			g.setColor(this.touching ? AppStyle.RED : AppStyle.GREEN);
			Painter.drawString(g, "State: " + this.lastAction, getStyle().getFont(), px, py);
			py += lineH + 5;

			g.setColor(AppStyle.CYAN);
			Painter.drawString(g, "Event Stats:", getStyle().getFont(), px, py);
			py += lineH;

			g.setColor(AppStyle.TEXT_WHITE);
			Painter.drawString(g, "PRESSED:  " + this.pressCount, getStyle().getFont(), px, py);
			py += lineH;
			Painter.drawString(g, "DRAGGED:  " + this.dragCount, getStyle().getFont(), px, py);
			py += lineH;
			Painter.drawString(g, "RELEASED: " + this.releaseCount, getStyle().getFont(), px, py);
			py += lineH;
			Painter.drawString(g, "Total events: " + this.eventCount, getStyle().getFont(), px, py);
			py += lineH;

			g.setColor(AppStyle.YELLOW);
			Painter.drawString(g, "Events/sec: " + this.eventsPerSecond, getStyle().getFont(), px, py);

			// Bottom info
			g.setColor(AppStyle.TEXT_GRAY);
			Painter.drawString(g, "Pointer.getX/Y() reads raw coords from native touch IC driver (I2C/SPI)",
					getStyle().getFont(), 5, contentHeight - 12);
		}
	}
}
