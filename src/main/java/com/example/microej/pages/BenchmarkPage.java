package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.RuntimeEnv;
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
		info(main, "Runs a small suite for each page.");

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
//		runAll();
		return main;
	}

	// Remove BON dependency: ej.bon.Util isn't always available.
	// (RuntimeEnv is best-effort and can be forced in simulator with -Dapp.simulator=true)

	private static void log(String msg) {
		try {
            if(msg.isEmpty()) {
                System.out.println();
                return;
            }
			System.out.println("[Bench] " + msg);
		} catch (Throwable ignored) {
			// ignore
		}
	}

	private void runAll() {
		this.results.removeAllChildren();

		log("RunAll start (env=" + RuntimeEnv.getEnvLabel() + ")");

		BenchSuite[] suites = new BenchSuite[] {
				new StartupBenchSuite(),
				new DeviceInfoBenchSuite(),
				new MemoryBenchSuite(),
				new com.example.microej.bench.suites.CpuBenchSuite(),
				new com.example.microej.bench.suites.JavaPerfBenchSuite(),
				new NetworkBenchSuite(),
				new DisplayHardwareBenchSuite(),
//				new TouchHardwareBenchSuite(),
				new McuTempBenchSuite(),
//				new FileSystemBenchSuite(),
//				new com.example.microej.bench.suites.PowerBenchSuite()
		};

		int pass = 0, fail = 0, skip = 0;
		long startAll = BenchUtil.nowMs();

		for (int s = 0; s < suites.length; s++) {
			BenchSuite suite = suites[s];
			log("Suite: " + suite.getName());
			addLine("[SUITE] " + suite.getName());
			BenchResult[] rs = BenchRunner.runSuite(suite);
			for (int i = 0; i < rs.length; i++) {
				BenchResult r = rs[i];
				log("  " + r.status + " " + r.name + " (" + r.durationMs + " ms)" + (r.details != null && r.details.length() > 0 ? (" - " + r.details) : ""));
				if (r.status == BenchStatus.PASS) pass++;
				else if (r.status == BenchStatus.FAIL) fail++;
				else skip++;

				String line = r.name + "  (" + r.durationMs + " ms)";
				String extra = "";
				if (r.details != null && r.details.length() > 0) {
					extra = r.details;
				}
				String metrics = formatMetrics(r.metrics);
				if (metrics.length() > 0) {
					if (extra.length() > 0) {
						extra += " | " + metrics;
					} else {
						extra = metrics;
					}
				}
				if (extra.length() > 0) {
					line += "  -  " + extra;
				}
				addLine(line);
			}
			log("");
		}

		long dur = Math.max(0, BenchUtil.nowMs() - startAll);
		log("RunAll done in " + dur + " ms (PASS=" + pass + " FAIL=" + fail + " SKIP=" + skip + ")");
		this.summary.setText("Done in " + dur + " ms  |  PASS=" + pass + "  FAIL=" + fail + "  SKIP=" + skip);
		this.summary.requestRender();
		this.results.requestRender();
	}

	private static String formatMetrics(BenchMetrics m) {
		if (m == null || m.keys == null || m.values == null) {
			return "";
		}
		int n = Math.min(m.keys.length, m.values.length);
		if (n <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder(64);
		for (int i = 0; i < n; i++) {
			String k = m.keys[i];
			String v = m.values[i];
			if (k == null || v == null) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(k).append('=').append(v);
		}
		return sb.toString();
	}

	private void addLine(String t) {
//		Label l = new Label(t);
//		l.addClassSelector(RESULT);
//		this.results.addChild(l);
	}

	private void section(List p, String t) { Label l = new Label(t); l.addClassSelector(SECTION); p.addChild(l); }
	private void info(List p, String t)    { Label l = new Label(t); l.addClassSelector(INFO);    p.addChild(l); }
}
