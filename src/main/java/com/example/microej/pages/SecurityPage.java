package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.UiClickLog;
import ej.microui.display.Colors;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class SecurityPage implements Page {

	private static final int SECTION    = 4300;
	private static final int INFO       = 4301;
	private static final int ACTION_BTN = 4302;
	private static final int RESULT     = 4303;
	private static final int LOG        = 4304;

	private Label resultLabel;
	private List  logList;
	private int   logCount;

	@Override public String getName()        { return "Security / Crypto"; }
	@Override public String getDescription() { return "Crypto APIs not available in this profile; stub page."; }
	@Override public int    getAccentColor() { return AppStyle.PINK; }

	@Override
	public void populateStylesheet(CascadingStylesheet ss) {
		EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
		s.setColor(AppStyle.PINK);
		s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
		s.setPadding(new UniformOutline(8));

		s = ss.getSelectorStyle(new ClassSelector(INFO));
		s.setColor(AppStyle.TEXT_LIGHT);
		s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
		s.setPadding(new UniformOutline(5));

		s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
		s.setDimension(new FixedDimension(380, 48));
		s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
		s.setColor(Colors.WHITE);
		s.setPadding(new UniformOutline(10));
		s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

		s = ss.getSelectorStyle(new ClassSelector(RESULT));
		s.setColor(AppStyle.YELLOW);
		s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
		s.setBorder(new RectangularBorder(AppStyle.PINK, 1));
		s.setPadding(new UniformOutline(8));

		s = ss.getSelectorStyle(new ClassSelector(LOG));
		s.setColor(AppStyle.TEAL);
		s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
		s.setPadding(new UniformOutline(4));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		section(main, "\u25b6  Crypto / Security");
		info(main, "This application profile doesn't include java.security / javax.crypto.");
		info(main, "If you need crypto, add the MicroEJ Security pack (LLSEC) + required Java APIs.");

		Button btn = new Button("Show details");
		btn.addClassSelector(ACTION_BTN);
		btn.setOnClickListener(new OnClickListener() {
			@Override public void onClick() {
				UiClickLog.click("SecurityPage", "Show details", "showDetails");
				log("Missing APIs: java.security.MessageDigest, javax.crypto.Cipher, SecureRandom...");
				result("Crypto APIs missing in this build (stub page)");
			}
		});
		main.addChild(btn);

		this.resultLabel = new Label("Crypto page stub");
		this.resultLabel.addClassSelector(RESULT);
		main.addChild(this.resultLabel);

		section(main, "\u25b6  Log");
		this.logList = new List(LayoutOrientation.VERTICAL);
		main.addChild(this.logList);

		return main;
	}

	private void result(String t) { this.resultLabel.setText(t); this.resultLabel.requestRender(); }

	private void log(String t) {
		if (this.logCount++ > 20) { this.logList.removeAllChildren(); this.logCount = 1; }
		Label l = new Label(t);
		l.addClassSelector(LOG);
		this.logList.addChild(l);
		this.logList.requestRender();
	}

	private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
	private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
