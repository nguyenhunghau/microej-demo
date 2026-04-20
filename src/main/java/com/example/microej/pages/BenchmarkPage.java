package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.bench.*;
import com.example.microej.bench.suites.*;

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

public class BenchmarkPage implements Page {

	private static final int SECTION = 9000;
	private static final int INFO = 9001;
	private static final int ACTION_BTN = 9002;
	private static final int RESULT = 9003;

	private Label summary;
	private List results;

	@Override
	public String getName() {
		return "Benchmarks";
	}

	@Override
	public String getDescription() {
		return "Runs quick self-tests for key pages and prints pass/fail + timing";
	}

	@Override
	public int getAccentColor() {
		return AppStyle.PURPLE;
	}

	@Override
	public void populateStylesheet(CascadingStylesheet ss) {
		EditableStyle s = ss.getSelectorStyle(new ClassSelector(SECTION));
		s.setColor(AppStyle.PURPLE);
		s.setBackground(new RectangularBackground(AppStyle.SECTION_BG));
		s.setPadding(new UniformOutline(8));

		s = ss.getSelectorStyle(new ClassSelector(INFO));
		s.setColor(AppStyle.TEXT_LIGHT);
		s.setBackground(new RectangularBackground(AppStyle.BG_DARK));
		s.setPadding(new UniformOutline(5));

		s = ss.getSelectorStyle(new ClassSelector(ACTION_BTN));
		s.setDimension(new FixedDimension(340, 48));
		s.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
		s.setColor(Colors.WHITE);
		s.setPadding(new UniformOutline(10));
		s.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

		s = ss.getSelectorStyle(new ClassSelector(RESULT));
		s.setColor(AppStyle.TEXT_WHITE);
		s.setBackground(new RectangularBackground(AppStyle.VALUE_BG));
		s.setBorder(new RectangularBorder(AppStyle.PURPLE, 1));
		s.setPadding(new UniformOutline(6));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		section(main, "\u25b6  Benchmarks / Self-tests");
		info(main, "Runs a small suite for each page (some hardware tests may be SKIP in simulator).");

		Button runBtn = new Button("Run All Benchmarks");
		runBtn.addClassSelector(ACTION_BTN);
		runBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				runAll();
			}
		});
		main.addChild(runBtn);

		this.summary = new Label("Press 'Run All Benchmarks'");
		this.summary.addClassSelector(RESULT);
		main.addChild(this.summary);

		section(main, "\u25b6  Results");
		this.results = new List(LayoutOrientation.VERTICAL);
		main.addChild(this.results);

		return main;
	}

	private void runAll() {
		this.results.removeAllChildren();

		BenchSuite[] suites = new BenchSuite[] {
				new DeviceInfoBenchSuite(),
				new DisplayHardwareBenchSuite(),
				new TouchHardwareBenchSuite(),
				new McuTempBenchSuite(),
				new FileSystemBenchSuite()
		};

		int pass = 0, fail = 0, skip = 0;
		long startAll = BenchUtil.nowMs();

		for (int s = 0; s < suites.length; s++) {
			BenchSuite suite = suites[s];
			addLine("[SUITE] " + suite.getName());
			BenchResult[] rs = BenchRunner.runSuite(suite);
			for (int i = 0; i < rs.length; i++) {
				BenchResult r = rs[i];
				if (r.status == BenchStatus.PASS) pass++;
				else if (r.status == BenchStatus.FAIL) fail++;
				else skip++;

				String line = "  [" + r.status + "] " + r.name + "  (" + r.durationMs + " ms)";
				if (r.details != null && r.details.length() > 0) {
					line += "  -  " + r.details;
				}
				addLine(line);
			}
		}

		long dur = Math.max(0, BenchUtil.nowMs() - startAll);
		this.summary.setText("Done in " + dur + " ms  |  PASS=" + pass + "  FAIL=" + fail + "  SKIP=" + skip);
		this.summary.requestRender();
		this.results.requestRender();
	}

	private void addLine(String t) {
		Label l = new Label(t);
		l.addClassSelector(RESULT);
		this.results.addChild(l);
	}

	private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
	private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
