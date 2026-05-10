package com.example.microej.pages;

import com.example.microej.AppStyle;
import com.example.microej.Page;
import com.example.microej.RuntimeEnv;
import com.example.microej.UiClickLog;
import com.example.microej.bench.*;
import com.example.microej.bench.suites.*;

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
import ej.mwt.util.Size;
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
	private Label progressLabel;
	private ProgressBarWidget progressBar;

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

		Button runBtn = new Button("Run All Benchmarks");
		runBtn.addClassSelector(ACTION_BTN);
		runBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				UiClickLog.click("BenchmarkPage", "Run All Benchmarks", "runAll");
				runAll();
			}
		});
		main.addChild(runBtn);

		this.summary = new Label("Press 'Run All Benchmarks'");
		this.summary.addClassSelector(RESULT);
		main.addChild(this.summary);

		// Progress bar (initially hidden)
		this.progressLabel = new Label("Running tests...");
		this.progressLabel.addClassSelector(INFO);
		main.addChild(this.progressLabel);

		this.progressBar = new ProgressBarWidget();
		main.addChild(this.progressBar);

		section(main, "\u25b6  Benchmarks / Self-tests");
		info(main, "Runs a small suite for each page.");

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

		// Show progress bar
		this.progressBar.show();
		this.progressLabel.setText("Running tests...");
		this.progressLabel.requestRender();

		int pass = 0, fail = 0, skip = 0;
		long startAll = BenchUtil.nowMs();

		int completedTests = 0;
		int totalTests = 0;
		// First pass: count total tests
		for (BenchSuite suite : suites) {
			BenchResult[] rs = BenchRunner.runSuite(suite);
			totalTests += rs.length;
		}

		// Reset and run again with progress tracking
		completedTests = 0;
		for (BenchSuite suite : suites) {
			log("Suite: " + suite.getName());
			addLine("[SUITE] " + suite.getName());
			BenchResult[] rs = BenchRunner.runSuite(suite);
			for (BenchResult r : rs) {
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

				// Update progress
				completedTests++;
				this.progressBar.setProgress(completedTests, totalTests);
				this.progressLabel.setText("Running: " + completedTests + " / " + totalTests + " tests");
				this.progressLabel.requestRender();
				this.progressBar.requestRender();
			}
			log("");
		}

		long dur = Math.max(0, BenchUtil.nowMs() - startAll);
		log("RunAll done in " + dur + " ms (PASS=" + pass + " FAIL=" + fail + " SKIP=" + skip + ")");
		this.summary.setText("Done in " + dur + " ms  |  PASS=" + pass + "  FAIL=" + fail + "  SKIP=" + skip);
		this.summary.requestRender();
		this.results.requestRender();

		// Hide progress bar when done
		this.progressBar.hide();
		this.progressLabel.setText("Tests complete!");
		this.progressLabel.requestRender();
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

	static class ProgressBarWidget extends Widget {
		private int current = 0;
		private int total = 100;
		private boolean visible = false;

		void setProgress(int current, int total) {
			this.current = Math.max(0, Math.min(current, total));
			this.total = Math.max(1, total);
		}

		void show() {
			this.visible = true;
			this.current = 0;
			requestRender();
		}

		void hide() {
			this.visible = false;
			requestRender();
		}

		@Override
		protected void computeContentOptimalSize(Size size) {
			size.setSize(640, 40);
		}

		@Override
		protected void renderContent(GraphicsContext g, int w, int h) {
			if (!this.visible) {
				return;
			}

			// Background
			g.setColor(AppStyle.VALUE_BG);
			Painter.fillRectangle(g, 0, 0, w, h);

			// Border
			g.setColor(AppStyle.PURPLE);
			Painter.drawRectangle(g, 0, 0, w - 1, h - 1);

			// Progress fill
			float progress = this.total > 0 ? (float) this.current / this.total : 0f;
			int fillWidth = (int) (w * progress);
			if (fillWidth > 0) {
				g.setColor(AppStyle.PURPLE);
				Painter.fillRectangle(g, 0, 0, fillWidth, h);
			}

			// Text: percentage
			g.setColor(AppStyle.TEXT_WHITE);
			int percent = this.total > 0 ? (this.current * 100) / this.total : 0;
			Painter.drawString(g, percent + "%", getStyle().getFont(), w / 2 - 10, h / 2 - 8);
		}
	}
}
