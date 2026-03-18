package com.microej.demo.showcase.pages;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.hal.gpio.GPIO;
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
import ej.mwt.stylesheet.selector.StateSelector;
import ej.mwt.stylesheet.selector.combinator.AndCombinator;
import ej.mwt.util.Size;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class McuTempPage implements Page {

	private static final int SECTION = 3700;
	private static final int INFO = 3701;
	private static final int ACTION_BTN = 3702;
	private static final int RESULT = 3703;
	private static final int GAUGE = 3704;

	// STM32F750 internal temperature sensor
	// Connected to ADC1 Channel 18
	// Port 0 = ADC port in VEE Port HAL mapping
	private static final int ADC_PORT = 0;
	private static final int TEMP_CHANNEL = 18;

	private Label resultLabel;
	private Label tempLabel;
	private TempGauge tempGauge;

	@Override
	public String getName() {
		return "MCU Temp Sensor";
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
		style.setDimension(new FixedDimension(200, 22));
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

		style = stylesheet.getSelectorStyle(new ClassSelector(GAUGE));
		style.setPadding(new UniformOutline(3));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		addSection(main, "MCU Internal Temp Sensor -> ADC -> HAL_ADC_GetValue()");
		addInfo(main, "STM32F750: Internal temp sensor on ADC1 Channel 18");
		addInfo(main, "Formula: Temp(C) = (V_sense - V25) / Avg_Slope + 25");
		addInfo(main, "API: GPIO.getAnalogValue(port, channel) -> raw ADC value");

		// Temp gauge
		this.tempGauge = new TempGauge();
		this.tempGauge.addClassSelector(GAUGE);
		main.addChild(this.tempGauge);

		this.tempLabel = new Label("Temperature: -- (tap Read to measure)");
		this.tempLabel.addClassSelector(RESULT);
		main.addChild(this.tempLabel);

		// Read once
		Button readBtn = new Button("Read MCU Temperature (ADC)");
		readBtn.addClassSelector(ACTION_BTN);
		readBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				readTemp();
			}
		});
		main.addChild(readBtn);

		// Continuous read
		Button contBtn = new Button("Continuous Read (every 1s)");
		contBtn.addClassSelector(ACTION_BTN);
		contBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				startContinuousRead();
			}
		});
		main.addChild(contBtn);

		this.resultLabel = new Label("GPIO.getAnalogValue() -> SNI -> HAL_ADC_GetValue()");
		this.resultLabel.addClassSelector(INFO);
		main.addChild(this.resultLabel);

		return main;
	}

	private void readTemp() {
		try {
			GPIO.setMode(ADC_PORT, TEMP_CHANNEL, GPIO.Mode.ANALOG_INPUT);
			int rawAdc = GPIO.getAnalogValue(ADC_PORT, TEMP_CHANNEL);
			// STM32F7 ADC: 12-bit (0-4095), Vref=3.3V
			// Internal temp sensor: V25=0.76V, Avg_Slope=2.5mV/C
			float voltage = (rawAdc / 4095.0f) * 3.3f;
			float tempC = ((voltage - 0.76f) / 0.0025f) + 25.0f;
			this.tempLabel.setText("Temp: " + ((int) (tempC * 10)) / 10.0f + " C (raw ADC: " + rawAdc + ")");
			this.tempLabel.requestRender();
			this.tempGauge.setTemperature(tempC);
			this.tempGauge.requestRender();
			setResult("ADC raw: " + rawAdc + " | Voltage: " + ((int) (voltage * 1000)) + "mV");
		} catch (Throwable t) {
			setResult("ADC: " + t.getClass().getName() + " (no BSP on simulator)");
			this.tempLabel.setText("Temperature: unavailable on simulator");
			this.tempLabel.requestRender();
		}
	}

	private void startContinuousRead() {
		setResult("Continuous read started (1s interval)...");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				readTemp();
			}
		}, 0, 1000);
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

	static class TempGauge extends Widget {
		private float temperature = 25.0f;

		void setTemperature(float temp) {
			this.temperature = temp;
		}

		@Override
		protected void computeContentOptimalSize(Size size) {
			size.setSize(440, 50);
		}

		@Override
		protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
			// Background bar
			g.setColor(AppStyle.BG_CARD);
			Painter.fillRectangle(g, 0, 10, contentWidth, 30);

			// Temperature range: 0-100 C
			float ratio = Math.max(0, Math.min(1, this.temperature / 100.0f));
			int barWidth = (int) (contentWidth * ratio);

			// Color based on temp
			int color;
			if (this.temperature < 30) {
				color = AppStyle.BLUE;
			} else if (this.temperature < 50) {
				color = AppStyle.GREEN;
			} else if (this.temperature < 70) {
				color = AppStyle.ORANGE;
			} else {
				color = AppStyle.RED;
			}
			g.setColor(color);
			Painter.fillRectangle(g, 0, 10, barWidth, 30);

			// Scale markers
			g.setColor(AppStyle.TEXT_GRAY);
			Painter.drawString(g, "0C", getStyle().getFont(), 2, 0);
			Painter.drawString(g, "50C", getStyle().getFont(), contentWidth / 2 - 10, 0);
			Painter.drawString(g, "100C", getStyle().getFont(), contentWidth - 30, 0);

			// Current value
			g.setColor(AppStyle.TEXT_WHITE);
			String val = ((int) (this.temperature * 10)) / 10.0f + " C";
			Painter.drawString(g, val, getStyle().getFont(), barWidth + 5, 18);
		}
	}
}
