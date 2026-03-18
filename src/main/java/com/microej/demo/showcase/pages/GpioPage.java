package com.microej.demo.showcase.pages;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

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

public class GpioPage implements Page {

	private static final int SECTION = 3200;
	private static final int KEY = 3201;
	private static final int VALUE = 3202;
	private static final int ACTION_BTN = 3203;
	private static final int RESULT = 3204;

	private Label resultLabel;

	@Override
	public String getName() {
		return "GPIO (HAL)";
	}

	@Override
	public void populateStylesheet(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(SECTION));
		style.setColor(AppStyle.CYAN);
		style.setPadding(new UniformOutline(3));

		style = stylesheet.getSelectorStyle(new ClassSelector(KEY));
		style.setColor(AppStyle.TEXT_GRAY);
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new ClassSelector(VALUE));
		style.setColor(AppStyle.TEXT_WHITE);
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new ClassSelector(ACTION_BTN));
		style.setDimension(new FixedDimension(160, 22));
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
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		addSection(main, "GPIO API -> native HAL -> BSP pin driver");
		addInfo(main, "API: ej.hal.gpio.GPIO");
		addInfo(main, "Methods: setMode(), getDigitalValue(), setDigitalValue(), getAnalogValue()");
		addInfo(main, "Each call goes through SNI to C HAL_GPIO_* functions");

		addSection(main, "Test GPIO calls (Port 0, Pin 0):");

		// Set mode button
		Button setModeBtn = new Button("GPIO.setMode(0,0,OUTPUT)");
		setModeBtn.addClassSelector(ACTION_BTN);
		setModeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					GPIO.setMode(0, 0, GPIO.Mode.DIGITAL_OUTPUT);
					setResult("setMode OK: Port 0, Pin 0 -> DIGITAL_OUTPUT");
				} catch (Throwable t) {
					setResult("setMode: " + t.getClass().getName() + " (no BSP on simulator)");
				}
			}
		});
		main.addChild(setModeBtn);

		// Read digital button
		Button readBtn = new Button("GPIO.getDigitalValue(0,0)");
		readBtn.addClassSelector(ACTION_BTN);
		readBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					boolean val = GPIO.getDigitalValue(0, 0);
					setResult("getDigitalValue: " + val);
				} catch (Throwable t) {
					setResult("getDigitalValue: " + t.getClass().getName() + " (no BSP on simulator)");
				}
			}
		});
		main.addChild(readBtn);

		// Write digital button
		Button writeBtn = new Button("GPIO.setDigitalValue(0,0,true)");
		writeBtn.addClassSelector(ACTION_BTN);
		writeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					GPIO.setDigitalValue(0, 0, true);
					setResult("setDigitalValue OK: Pin 0 -> HIGH");
				} catch (Throwable t) {
					setResult("setDigitalValue: " + t.getClass().getName() + " (no BSP on simulator)");
				}
			}
		});
		main.addChild(writeBtn);

		// Read analog button
		Button analogBtn = new Button("GPIO.getAnalogValue(0,0)");
		analogBtn.addClassSelector(ACTION_BTN);
		analogBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					int val = GPIO.getAnalogValue(0, 0);
					setResult("getAnalogValue: " + val + " (ADC raw value)");
				} catch (Throwable t) {
					setResult("getAnalogValue: " + t.getClass().getName() + " (no BSP on simulator)");
				}
			}
		});
		main.addChild(analogBtn);

		// Result label
		this.resultLabel = new Label("Tap a button to call GPIO native functions");
		this.resultLabel.addClassSelector(RESULT);
		main.addChild(this.resultLabel);

		return main;
	}

	private void setResult(String text) {
		this.resultLabel.setText(text);
		this.resultLabel.requestRender();
	}

	private void addSection(List parent, String title) {
		Label label = new Label(title);
		label.addClassSelector(SECTION);
		parent.addChild(label);
	}

	private void addInfo(List parent, String text) {
		Label label = new Label(text);
		label.addClassSelector(KEY);
		parent.addChild(label);
	}
}
