package com.microej.demo.showcase.pages;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.hal.gpio.GPIO;
import ej.microui.display.Colors;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.stylesheet.selector.StateSelector;
import ej.mwt.stylesheet.selector.combinator.AndCombinator;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class LedButtonPage implements Page {

	private static final int SECTION = 3600;
	private static final int INFO = 3601;
	private static final int ACTION_BTN = 3602;
	private static final int RESULT = 3603;
	private static final int STATUS = 3604;

	// STM32F7508-DK board-specific pin mapping
	// LED1 (green) = PI1 (Port I = 8, Pin 1)
	private static final int LED_PORT = 8;
	private static final int LED_PIN = 1;

	// User Button (blue) = PI11 (Port I = 8, Pin 11)
	private static final int BUTTON_PORT = 8;
	private static final int BUTTON_PIN = 11;

	private Label resultLabel;
	private Label ledStatusLabel;
	private Label buttonStatusLabel;
	private boolean ledOn;
	private Timer pollTimer;

	@Override
	public String getName() {
		return "LED & User Button";
	}

	@Override
	public void populateStylesheet(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(SECTION));
		style.setColor(AppStyle.CYAN);
		style.setPadding(new UniformOutline(3));

		style = stylesheet.getSelectorStyle(new ClassSelector(INFO));
		style.setColor(AppStyle.TEXT_GRAY);
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new ClassSelector(ACTION_BTN));
		style.setDimension(new FixedDimension(180, 22));
		style.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
		style.setColor(Colors.WHITE);
		style.setPadding(new UniformOutline(4));

		style = stylesheet.getSelectorStyle(
				new AndCombinator(new ClassSelector(ACTION_BTN), new StateSelector(StateSelector.ACTIVE)));
		style.setBackground(new RectangularBackground(AppStyle.PURPLE));

		style = stylesheet.getSelectorStyle(new ClassSelector(RESULT));
		style.setColor(AppStyle.YELLOW);
		style.setPadding(new UniformOutline(4));
		style.setBackground(new RectangularBackground(AppStyle.BG_CARD));
		style.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

		style = stylesheet.getSelectorStyle(new ClassSelector(STATUS));
		style.setColor(AppStyle.GREEN);
		style.setPadding(new UniformOutline(3));
		style.setBackground(new RectangularBackground(AppStyle.BG_CARD));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		// --- LED Section ---
		addSection(main, "LED (GPIO Output) -> HAL_GPIO_WritePin()");
		addInfo(main, "STM32F7508-DK: LED1 (green) = Port I, Pin 1");

		this.ledStatusLabel = new Label("LED: OFF");
		this.ledStatusLabel.addClassSelector(STATUS);
		main.addChild(this.ledStatusLabel);

		Button toggleBtn = new Button("Toggle LED (GPIO.setDigitalValue)");
		toggleBtn.addClassSelector(ACTION_BTN);
		toggleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				toggleLed();
			}
		});
		main.addChild(toggleBtn);

		Button blinkBtn = new Button("Blink LED 5x (Timer + GPIO)");
		blinkBtn.addClassSelector(ACTION_BTN);
		blinkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				blinkLed();
			}
		});
		main.addChild(blinkBtn);

		// --- Button Section ---
		addSection(main, "User Button (GPIO Input) -> HAL_GPIO_ReadPin()");
		addInfo(main, "STM32F7508-DK: Blue button = Port I, Pin 11");

		this.buttonStatusLabel = new Label("Button: not polled yet");
		this.buttonStatusLabel.addClassSelector(STATUS);
		main.addChild(this.buttonStatusLabel);

		Button readBtn = new Button("Read Button (GPIO.getDigitalValue)");
		readBtn.addClassSelector(ACTION_BTN);
		readBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				readButton();
			}
		});
		main.addChild(readBtn);

		// Result
		this.resultLabel = new Label("Tap buttons to control board LED / read board button");
		this.resultLabel.addClassSelector(RESULT);
		main.addChild(this.resultLabel);

		return main;
	}

	private void toggleLed() {
		this.ledOn = !this.ledOn;
		try {
			GPIO.setMode(LED_PORT, LED_PIN, GPIO.Mode.DIGITAL_OUTPUT);
			GPIO.setDigitalValue(LED_PORT, LED_PIN, this.ledOn);
			this.ledStatusLabel.setText("LED: " + (this.ledOn ? "ON" : "OFF"));
			this.ledStatusLabel.requestRender();
			setResult("GPIO.setDigitalValue(" + LED_PORT + "," + LED_PIN + "," + this.ledOn + ")  OK");
		} catch (Throwable t) {
			setResult("LED: " + t.getClass().getName() + " (no BSP on simulator)");
		}
	}

	private void blinkLed() {
		try {
			GPIO.setMode(LED_PORT, LED_PIN, GPIO.Mode.DIGITAL_OUTPUT);
			setResult("Blinking LED 5 times...");
			Timer blinkTimer = new Timer();
			blinkTimer.schedule(new TimerTask() {
				private int count;

				@Override
				public void run() {
					if (this.count >= 10) {
						cancel();
						ledStatusLabel.setText("LED: OFF (blink done)");
						ledStatusLabel.requestRender();
						return;
					}
					boolean on = (this.count % 2 == 0);
					try {
						GPIO.setDigitalValue(LED_PORT, LED_PIN, on);
					} catch (Throwable t) {
						// Simulator - ignore
					}
					ledStatusLabel.setText("LED: " + (on ? "ON" : "OFF") + " [" + (this.count / 2 + 1) + "/5]");
					ledStatusLabel.requestRender();
					this.count++;
				}
			}, 0, 300);
		} catch (Throwable t) {
			setResult("Blink: " + t.getClass().getName() + " (no BSP on simulator)");
		}
	}

	private void readButton() {
		try {
			GPIO.setMode(BUTTON_PORT, BUTTON_PIN, GPIO.Mode.DIGITAL_INPUT);
			boolean pressed = GPIO.getDigitalValue(BUTTON_PORT, BUTTON_PIN);
			this.buttonStatusLabel.setText("Button: " + (pressed ? "PRESSED" : "RELEASED"));
			this.buttonStatusLabel.requestRender();
			setResult("GPIO.getDigitalValue(" + BUTTON_PORT + "," + BUTTON_PIN + ") = " + pressed);
		} catch (Throwable t) {
			setResult("Button: " + t.getClass().getName() + " (no BSP on simulator)");
		}
	}

	private void setResult(String text) {
		this.resultLabel.setText(text);
		this.resultLabel.requestRender();
	}

	private void addSection(List parent, String title) {
		Label l = new Label(title);
		l.addClassSelector(SECTION);
		parent.addChild(l);
	}

	private void addInfo(List parent, String text) {
		Label l = new Label(text);
		l.addClassSelector(INFO);
		parent.addChild(l);
	}
}
