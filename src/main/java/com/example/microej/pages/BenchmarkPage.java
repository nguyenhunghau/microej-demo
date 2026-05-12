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

	private Label statusLabel;
	private Label countsLabel;
	private Label startupLabel;
	private Label cpuLabel;
	private Label javaLabel;
	private Label uiFpsLabel;
	private Label vgLabel;
	private Label heapLabel;
	private Label netLabel;
	private Label mcuLabel;
	private Label failedLabel;
	private ProgressBarWidget progressBar;
	private volatile boolean running;

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
				startRun();
			}
		});
		main.addChild(runBtn);

		info(main, "Probes CPU, memory, startup, display, network.");
		info(main, "Console shows full per-case log.");

		this.statusLabel = new Label("Status: Idle");
		this.statusLabel.addClassSelector(RESULT);
		main.addChild(this.statusLabel);

		this.progressBar = new ProgressBarWidget();
		main.addChild(this.progressBar);

		this.countsLabel = new Label("Counts:  -");
		this.countsLabel.addClassSelector(RESULT);
		main.addChild(this.countsLabel);

		this.startupLabel = new Label("Startup: -");
		this.startupLabel.addClassSelector(RESULT);
		main.addChild(this.startupLabel);

		this.cpuLabel = new Label("CPU:     -");
		this.cpuLabel.addClassSelector(RESULT);
		main.addChild(this.cpuLabel);

		this.javaLabel = new Label("Java:    -");
		this.javaLabel.addClassSelector(RESULT);
		main.addChild(this.javaLabel);

		this.uiFpsLabel = new Label("UI FPS:  -");
		this.uiFpsLabel.addClassSelector(RESULT);
		main.addChild(this.uiFpsLabel);

		this.vgLabel = new Label("VG:      -");
		this.vgLabel.addClassSelector(RESULT);
		main.addChild(this.vgLabel);

		this.heapLabel = new Label("Heap:    -");
		this.heapLabel.addClassSelector(RESULT);
		main.addChild(this.heapLabel);

		this.netLabel = new Label("Net:     -");
		this.netLabel.addClassSelector(RESULT);
		main.addChild(this.netLabel);

		this.mcuLabel = new Label("MCU:     -");
		this.mcuLabel.addClassSelector(RESULT);
		main.addChild(this.mcuLabel);

		this.failedLabel = new Label("Failed:  -");
		this.failedLabel.addClassSelector(RESULT);
		main.addChild(this.failedLabel);

		return main;
	}

	private void startRun() {
		if (this.running) {
			log("Run already in progress");
			return;
		}
		this.running = true;
		Thread t = new Thread(new Runnable() {
			@Override public void run() {
				try {
					runAll();
				} finally {
					BenchmarkPage.this.running = false;
				}
			}
		}, "BenchRunner");
		t.start();
	}

	private void setText(Label l, String t) {
		l.setText(t);
		l.requestRender();
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
		log("RunAll start (env=" + RuntimeEnv.getEnvLabel() + ")");

		BenchSuite[] suites = new BenchSuite[] {
				new StartupBenchSuite(),
				new DeviceInfoBenchSuite(),
				new MemoryBenchSuite(),
				new com.example.microej.bench.suites.CpuBenchSuite(),
				new com.example.microej.bench.suites.JavaPerfBenchSuite(),
				new com.example.microej.bench.suites.UiFpsBenchSuite(),
				new com.example.microej.bench.suites.VectorGraphicsBenchSuite(),
				new NetworkBenchSuite(),
				new DisplayHardwareBenchSuite(),
//				new TouchHardwareBenchSuite(),
				new McuTempBenchSuite(),
//				new FileSystemBenchSuite(),
//				new com.example.microej.bench.suites.PowerBenchSuite()
		};

		int totalTests = 0;
		for (BenchSuite suite : suites) {
			totalTests += suite.getCases().length;
		}

		// Reset UI to "running" state.
		this.progressBar.show();
		this.progressBar.setProgress(0, totalTests);
		this.progressBar.requestRender();
		setText(this.statusLabel,  "Status:  Running 0/" + totalTests);
		setText(this.countsLabel,  "Counts:  -");
		setText(this.startupLabel, "Startup: -");
		setText(this.cpuLabel,     "CPU:     -");
		setText(this.javaLabel,    "Java:    -");
		setText(this.uiFpsLabel,   "UI FPS:  -");
		setText(this.vgLabel,      "VG:      -");
		setText(this.heapLabel,    "Heap:    -");
		setText(this.netLabel,     "Net:     -");
		setText(this.mcuLabel,     "MCU:     -");
		setText(this.failedLabel,  "Failed:  -");

		int pass = 0, fail = 0, skip = 0;
		long startAll = BenchUtil.nowMs();
		int completed = 0;

		String cpuOps = null, javaOps = null, memUsed = null, memTotal = null;
		String connectMs = null, rttMs = null, bps = null;
		String startupMs = null, mcuTemp = null;
		String uiFps = null, uiFrameMs = null, uiAlloc = null;
		String vgFps = null, vgArcs = null, vgFrameMs = null;
		String uiFailReason = null, vgFailReason = null;
		StringBuilder failedNames = new StringBuilder(64);

		for (BenchSuite suite : suites) {
			log("Suite: " + suite.getName());
			BenchResult[] rs = BenchRunner.runSuite(suite);
			for (BenchResult r : rs) {
				log("  " + r.status + " " + r.name + " (" + r.durationMs + " ms)" + (r.details != null && r.details.length() > 0 ? (" - " + r.details) : ""));
				if (r.status == BenchStatus.PASS) pass++;
				else if (r.status == BenchStatus.FAIL) {
					fail++;
					if (failedNames.length() > 0) failedNames.append(", ");
					failedNames.append(r.name);
					String reason = (r.details != null && r.details.length() > 0) ? r.details : "(no detail)";
					if ("UI FPS (mascot)".equals(suite.getName())) uiFailReason = reason;
					else if ("Vector graphics".equals(suite.getName())) vgFailReason = reason;
				}
				else skip++;

				String v;
				v = lookupMetric(r.metrics, BenchMetricKeys.STARTUP_MS);         if (v != null) startupMs = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.CPU_OPS_PER_SEC);    if (v != null) cpuOps = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.JAVA_OPS_PER_SEC);   if (v != null) javaOps = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.MEM_USED_BYTES);     if (v != null) memUsed = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.MEM_TOTAL_BYTES);    if (v != null) memTotal = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.COMM_CONNECT_MS);    if (v != null) connectMs = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.COMM_LATENCY_MS);    if (v != null) rttMs = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.COMM_BYTES_PER_SEC); if (v != null) bps = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.MCU_TEMP_C);         if (v != null) mcuTemp = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.UI_FPS);             if (v != null) uiFps = v;
				v = lookupMetric(r.metrics, BenchMetricKeys.UI_FRAME_MS_AVG);    if (v != null) uiFrameMs = v;
				v = lookupMetric(r.metrics, "ui.alloc.bytes");                   if (v != null) uiAlloc = v;
				v = lookupMetric(r.metrics, "vg.fps");                           if (v != null) vgFps = v;
				v = lookupMetric(r.metrics, "vg.arcsPerSec");                    if (v != null) vgArcs = v;
				v = lookupMetric(r.metrics, "vg.frame.ms.avg");                  if (v != null) vgFrameMs = v;

				completed++;
				this.progressBar.setProgress(completed, totalTests);
				this.progressBar.requestRender();

				// Live-update labels so the user sees progress + partial metrics.
				setText(this.statusLabel, "Status:  Running " + completed + "/" + totalTests + "  (" + suite.getName() + ")");
				setText(this.countsLabel, "Counts:  PASS=" + pass + "  FAIL=" + fail + "  SKIP=" + skip);
				if (startupMs != null) setText(this.startupLabel, "Startup: " + startupMs + " ms");
				if (cpuOps != null)    setText(this.cpuLabel,     "CPU:     " + cpuOps + " ops/s");
				if (javaOps != null)   setText(this.javaLabel,    "Java:    " + javaOps + " ops/s");
				if (memUsed != null && memTotal != null) {
					long mu, mt;
					try { mu = Long.parseLong(memUsed); mt = Long.parseLong(memTotal); }
					catch (Throwable e) { mu = 0; mt = 0; }
					int pct = (mt > 0) ? (int) ((mu * 100L) / mt) : -1;
					setText(this.heapLabel, "Heap:    " + memUsed + " / " + memTotal + " B" + (pct >= 0 ? "  (" + pct + "%)" : ""));
				}
				if (connectMs != null || rttMs != null || bps != null) {
					StringBuilder n = new StringBuilder(48).append("Net:     ");
					if (connectMs != null) n.append("connect=").append(connectMs).append("ms  ");
					if (rttMs != null)     n.append("rtt=").append(rttMs).append("ms  ");
					if (bps != null)       n.append("bps=").append(bps);
					setText(this.netLabel, n.toString());
				}
				if (mcuTemp != null) setText(this.mcuLabel, "MCU:     " + mcuTemp + " C");
				if (uiFps != null) {
					StringBuilder u = new StringBuilder(48).append("UI FPS:  ").append(uiFps);
					if (uiFrameMs != null) u.append("  (").append(uiFrameMs).append(" ms/frame)");
					if (uiAlloc != null)   u.append("  alloc=").append(uiAlloc).append("B");
					setText(this.uiFpsLabel, u.toString());
				}
				if (vgFps != null || vgArcs != null) {
					StringBuilder u = new StringBuilder(48).append("VG:      ");
					if (vgFps != null)     u.append(vgFps).append(" fps  ");
					if (vgArcs != null)    u.append(vgArcs).append(" arcs/s  ");
					if (vgFrameMs != null) u.append("(").append(vgFrameMs).append(" ms/frame)");
					setText(this.vgLabel, u.toString());
				}
				if (failedNames.length() > 0) setText(this.failedLabel, "Failed:  " + failedNames);
			}
			log("");
		}

		long dur = Math.max(0, BenchUtil.nowMs() - startAll);
		log("RunAll done in " + dur + " ms (PASS=" + pass + " FAIL=" + fail + " SKIP=" + skip + ")");

		setText(this.statusLabel,  "Status:  Done in " + dur + " ms");
		setText(this.countsLabel,  "Counts:  PASS=" + pass + "  FAIL=" + fail + "  SKIP=" + skip);
		if (startupMs == null) setText(this.startupLabel, "Startup: (not marked)");
		if (cpuOps == null)    setText(this.cpuLabel,     "CPU:     (no data)");
		if (javaOps == null)   setText(this.javaLabel,    "Java:    (no data)");
		if (uiFps == null) {
			setText(this.uiFpsLabel, "UI FPS:  FAIL " + truncate(uiFailReason, 64));
		}
		if (vgFps == null) {
			setText(this.vgLabel, "VG:      FAIL " + truncate(vgFailReason, 64));
		}
		if (memUsed == null)   setText(this.heapLabel,    "Heap:    (no data)");
		if (connectMs == null && rttMs == null && bps == null) {
			setText(this.netLabel, "Net:     (failed or skipped, see log)");
		}
		if (mcuTemp == null)   setText(this.mcuLabel, "MCU:     (skipped on simulator)");
		if (failedNames.length() == 0) setText(this.failedLabel, "Failed:  none");

		this.progressBar.hide();
		this.progressBar.requestRender();
	}

	private static String lookupMetric(BenchMetrics m, String key) {
		if (m == null || m.keys == null || m.values == null) return null;
		int n = Math.min(m.keys.length, m.values.length);
		for (int i = 0; i < n; i++) {
			if (key.equals(m.keys[i])) return m.values[i];
		}
		return null;
	}

	private static String truncate(String s, int max) {
		if (s == null || s.length() == 0) return "(no detail)";
		if (s.length() <= max) return s;
		return s.substring(0, max - 1) + "…";
	}

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
			// Take zero space when idle so the page doesn't show an empty white strip.
			if (!this.visible) {
				size.setSize(0, 0);
				return;
			}
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
