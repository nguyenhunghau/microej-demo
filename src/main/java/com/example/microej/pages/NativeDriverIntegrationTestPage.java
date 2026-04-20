package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.NativeDriverIntegration;
import com.example.microej.Page;
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

/**
 * Test scenario to validate Java -> SNI -> BSP/driver integration.
 * <p>
 * PASS criteria:
 * <ul>
 * <li>Native methods can be invoked without {@link UnsatisfiedLinkError}.</li>
 * <li>{@link NativeDriverIntegration#getDriverVersion()} returns a positive version.</li>
 * <li>{@link NativeDriverIntegration#selfTest()} returns 0.</li>
 * <li>(Optional) {@link NativeDriverIntegration#blinkLed(int, int)} returns 0 and visible LED blinks.</li>
 * </ul>
 */
public class NativeDriverIntegrationTestPage implements Page {

	private static final int SECTION = 5000;
	private static final int INFO = 5001;
	private static final int ACTION_BTN = 5002;
	private static final int RESULT = 5003;

	private Label resultLabel;

	@Override
	public String getName() {
		return "Driver integration test";
	}

	@Override
	public String getDescription() {
		return "Validate Java -> SNI -> BSP driver calls (version, self-test, blink).";
	}

	@Override
	public int getAccentColor() {
		return AppStyle.CYAN;
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
		style.setDimension(new FixedDimension(220, 22));
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

		addSection(main, "Native driver integration test");
		addInfo(main, "This page calls app-owned native methods (SNI) implemented in the VEE Port.");
		addInfo(main, "Use it to validate Java -> SNI -> BSP/driver is wired correctly.");

		Button runBtn = new Button("Run self-test");
		runBtn.addClassSelector(ACTION_BTN);
		runBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				runSelfTest();
			}
		});
		main.addChild(runBtn);

		Button versionBtn = new Button("Get driver version");
		versionBtn.addClassSelector(ACTION_BTN);
		versionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				readVersion();
			}
		});
		main.addChild(versionBtn);

		Button blinkBtn = new Button("Blink LED (native)");
		blinkBtn.addClassSelector(ACTION_BTN);
		blinkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				blinkLed();
			}
		});
		main.addChild(blinkBtn);

		this.resultLabel = new Label("Tap a button to invoke native driver functions");
		this.resultLabel.addClassSelector(RESULT);
		main.addChild(this.resultLabel);

		return main;
	}

	private void runSelfTest() {
		try {
			int rc = NativeDriverIntegration.selfTest();
			String msg = (rc == 0) ? "PASS: selfTest() returned 0" : "FAIL: selfTest() returned " + rc;
			System.out.println("[DriverIntegrationTest] selfTest rc=" + rc);
			setResult(msg);
		} catch (UnsatisfiedLinkError e) {
			System.out.println("[DriverIntegrationTest] UnsatisfiedLinkError: " + e);
			setResult("FAIL: native not linked (UnsatisfiedLinkError): " + e.getMessage());
		} catch (Throwable t) {
			System.out.println("[DriverIntegrationTest] Exception: " + t);
			setResult("FAIL: native call threw: " + t.getClass().getName() + ": " + t.getMessage());
		}
	}

	private void readVersion() {
		try {
			int v = NativeDriverIntegration.getDriverVersion();
			System.out.println("[DriverIntegrationTest] Driver version: " + v);
			if (v <= 0) {
				setResult("FAIL: driver version invalid: " + v);
			} else {
				setResult("Driver version: " + v);
			}
		} catch (UnsatisfiedLinkError e) {
			System.out.println("[DriverIntegrationTest] UnsatisfiedLinkError: " + e);
			setResult("FAIL: native not linked (UnsatisfiedLinkError): " + e.getMessage());
		} catch (Throwable t) {
			System.out.println("[DriverIntegrationTest] Exception: " + t);
			setResult("FAIL: native call threw: " + t.getClass().getName() + ": " + t.getMessage());
		}
	}

	private void blinkLed() {
		try {
			int rc = NativeDriverIntegration.blinkLed(1, 1);
			System.out.println("[DriverIntegrationTest] blinkLed rc=" + rc);
			setResult((rc == 0) ? "blinkLed OK" : "blinkLed failed rc=" + rc);
		} catch (UnsatisfiedLinkError e) {
			System.out.println("[DriverIntegrationTest] UnsatisfiedLinkError: " + e);
			setResult("FAIL: native not linked (UnsatisfiedLinkError): " + e.getMessage());
		} catch (Throwable t) {
			System.out.println("[DriverIntegrationTest] Exception class=" + t.getClass().getName() + ", msg=" + t.getMessage());
			setResult("FAIL: native call threw: " + t.getClass().getName() + ": " + t.getMessage());
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
