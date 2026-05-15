package com.example.microej;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.mwt.Widget;
import ej.mwt.util.Size;

/**
 * Small header widget that displays live heap usage + CPU load when available.
 * <p>
 * CPU load is optional and depends on VEE Port support; when unsupported this widget shows "CPU --".
 */
public class SystemStatsWidget extends Widget {

	private static final int UPDATE_PERIOD_MS = 2000;
	private static boolean cpuInitAttempted;

	private Timer timer;
	private TimerTask timerTask;

	@Override
	protected void computeContentOptimalSize(Size size) {
		// Use a stable width so the label doesn't get clipped when its content grows
		// (e.g., when CPU debug counters are appended) after the first layout pass.
		// A wider last-child in SimpleDock also naturally pushes it more to the left.
		size.setSize(280, 44);
	}

	@Override
	protected void onShown() {
		ensureCpuLoadInitialized();
		startRefreshTimer();
	}

	@Override
	protected void onHidden() {
		stopRefreshTimer();
	}

	private void startRefreshTimer() {
		stopRefreshTimer();
		try {
			this.timer = new Timer();
			this.timerTask = new TimerTask() {
				@Override
				public void run() {
					try {
						SystemStatsWidget.this.requestRender();
					} catch (Throwable ignored) {
						// ignore
					}
				}
			};
			// Refresh at a human-friendly rate while allowing the RTOS idle task to run.
			this.timer.schedule(this.timerTask, 0, UPDATE_PERIOD_MS);
		} catch (Throwable ignored) {
			// If timers are unavailable for some reason, fall back to a single render.
			requestRender();
		}
	}

	private void stopRefreshTimer() {
		try {
			if (this.timerTask != null) {
				this.timerTask.cancel();
			}
		} catch (Throwable ignored) {
			// ignore
		}
		this.timerTask = null;

		try {
			if (this.timer != null) {
				this.timer.cancel();
			}
		} catch (Throwable ignored) {
			// ignore
		}
		this.timer = null;
	}

	@Override
	protected void renderContent(GraphicsContext g, int w, int h) {
		// Background
		g.setColor(AppStyle.BUTTON_BG);
		Painter.fillRectangle(g, 0, 0, w, h);
		g.setColor(AppStyle.CARD_STROKE);
		Painter.drawRectangle(g, 0, 0, w - 1, h - 1);

		Font f = getStyle().getFont();
		String line;
		if (f != null) {
			line = buildStatsLine(f, w - 20);
		} else {
			line = buildStatsLine();
		}

		g.setColor(AppStyle.TEXT_WHITE);
		// Left-align to keep the label visually more to the left.
		Painter.drawString(g, line, f, 10, 10);
	}

	/** Builds a single-line label suitable for top bars. */
	public static String buildStatsLine() {
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long used = total - free;

		StringBuffer sb = new StringBuffer();
		sb.append("MEM ");
		sb.append(formatBytesShort(used));
		sb.append('/');
		sb.append(formatBytesShort(total));

		// Prefer a raw numeric load value (permille) when available.
		int cpuPermille = readCpuLoadPermilleSafe();
		sb.append(" CPU ");
		if (cpuPermille >= 0) {
			// Permille (0..1000) is more precise than percent (0..100).
			// Format it as a decimal percent (e.g., 523 -> 52.3%) to avoid relying on
			// special/unit characters that might not exist in the embedded font subset.
			int wholePct = cpuPermille / 10;
			int tenthPct = cpuPermille % 10;
			sb.append(wholePct);
			if (tenthPct != 0) {
				sb.append('.');
				sb.append(tenthPct);
			}
			sb.append('%');

			// If the load is pegged (0% or 100%), append idle/reference counters to quickly
			// diagnose whether the RTOS ever reaches the idle task (idle=0) or if the
			// reference calibration is off.
			if (cpuPermille == 0 || cpuPermille == 1000) {
				int idle = readCpuIdleCounterSafe();
				int ref = readCpuReferenceCounterSafe();
				if (idle >= 0 && ref > 0) {
					sb.append(" i");
					sb.append(formatCountShort(idle));
					sb.append("/r");
					sb.append(formatCountShort(ref));
				}
			}
		} else {
			int cpu = readCpuLoadPercentSafe();
			if (cpu >= 0) {
				sb.append(cpu);
				sb.append('%');
			} else {
				sb.append("N/A");
			}
		}

		return sb.toString();
	}

	/**
	 * Builds a single-line label that tries to fit within the given pixel width.
	 * Falls back to shorter variants when space is tight.
	 */
	static String buildStatsLine(Font f, int maxTextWidth) {
		if (f == null || maxTextWidth <= 0) {
			return buildStatsLine();
		}

		// Compute the same data once, then try multiple string variants.
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long used = total - free;

		String usedS = formatBytesShort(used);
		String totalS = formatBytesShort(total);

		String cpuS;
		int cpuPermille = readCpuLoadPermilleSafe();
		if (cpuPermille >= 0) {
			int wholePct = cpuPermille / 10;
			int tenthPct = cpuPermille % 10;
			StringBuffer cpu = new StringBuffer();
			cpu.append(wholePct);
			if (tenthPct != 0) {
				cpu.append('.');
				cpu.append(tenthPct);
			}
			cpu.append('%');

			// Append debug counters only if we have enough space later.
			int idle = -1;
			int ref = -1;
			if (cpuPermille == 0 || cpuPermille == 1000) {
				idle = readCpuIdleCounterSafe();
				ref = readCpuReferenceCounterSafe();
			}
			cpuS = cpu.toString();
			String dbg = (idle >= 0 && ref > 0) ? (" i" + formatCountShort(idle) + "/r" + formatCountShort(ref)) : null;

			// Try long -> medium -> short
			String s;
			s = "MEM " + usedS + "/" + totalS + " CPU " + cpuS + (dbg != null ? dbg : "");
			if (f.stringWidth(s) <= maxTextWidth) return s;
			s = "M " + usedS + "/" + totalS + " C " + cpuS;
			if (f.stringWidth(s) <= maxTextWidth) return s;
			s = "CPU " + cpuS;
			if (f.stringWidth(s) <= maxTextWidth) return s;
			return cpuS; // last resort
		}

		int cpuPct = readCpuLoadPercentSafe();
		if (cpuPct >= 0) {
			cpuS = cpuPct + "%";
		} else {
			cpuS = "N/A";
		}

		String s;
		s = "MEM " + usedS + "/" + totalS + " CPU " + cpuS;
		if (f.stringWidth(s) <= maxTextWidth) return s;
		s = "M " + usedS + "/" + totalS + " C " + cpuS;
		if (f.stringWidth(s) <= maxTextWidth) return s;
		s = "CPU " + cpuS;
		if (f.stringWidth(s) <= maxTextWidth) return s;
		return cpuS;
	}

	/**
	 * Returns CPU load percentage (0..100) when supported, otherwise -1.
	 * Never throws.
	 */
	public static int readCpuLoadPercentSafe() {
		try {
			int v = NativeDriverIntegration.getCpuLoadPercent();
			return (v < 0 || v > 100) ? -1 : v;
		} catch (Throwable ignored) {
			return -1;
		}
	}

	/**
	 * Returns CPU load permille (0..1000) when supported, otherwise -1.
	 * Never throws.
	 */
	public static int readCpuLoadPermilleSafe() {
		try {
			int v = NativeDriverIntegration.getCpuLoadPermille();
			return (v < 0 || v > 1000) ? -1 : v;
		} catch (Throwable ignored) {
			return -1;
		}
	}

	/** Returns last raw idle counter when supported, otherwise -1. Never throws. */
	public static int readCpuIdleCounterSafe() {
		try {
			int v = NativeDriverIntegration.getCpuIdleCounter();
			return (v < 0) ? -1 : v;
		} catch (Throwable ignored) {
			return -1;
		}
	}

	/** Returns last raw reference counter when supported, otherwise -1. Never throws. */
	public static int readCpuReferenceCounterSafe() {
		try {
			int v = NativeDriverIntegration.getCpuReferenceCounter();
			return (v <= 0) ? -1 : v;
		} catch (Throwable ignored) {
			return -1;
		}
	}

	private static void ensureCpuLoadInitialized() {
		if (cpuInitAttempted) {
			return;
		}
		cpuInitAttempted = true;
		try {
			NativeDriverIntegration.initCpuLoad();
		} catch (Throwable ignored) {
			// optional
		}
	}

	/**
	 * Formats bytes into a short human readable label (e.g., 950K, 12.3M).
	 * Keeps output stable and cheap (no floating point formatting).
	 */
	static String formatBytesShort(long bytes) {
		if (bytes < 0) {
			return "?";
		}
		final long KB = 1024L;
		final long MB = 1024L * 1024L;

		if (bytes >= MB) {
			long tenth = (bytes * 10L) / MB; // tenths of MB
			long whole = tenth / 10L;
			long frac = tenth % 10L;
			// Show one decimal only for small values to keep width in check.
			if (whole < 100) {
				return whole + "." + frac + "M";
			}
			return whole + "M";
		}
		if (bytes >= KB) {
			return (bytes / KB) + "K";
		}
		return bytes + "B";
	}

	/** Compact formatting for counters (no decimals, uses only digits + K/M). */
	static String formatCountShort(int v) {
		if (v < 0) {
			return "?";
		}
		if (v >= 1_000_000) {
			return (v / 1_000_000) + "M";
		}
		if (v >= 10_000) {
			return (v / 1_000) + "K";
		}
		return Integer.toString(v);
	}
}


