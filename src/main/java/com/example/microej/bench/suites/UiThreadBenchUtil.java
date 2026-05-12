package com.example.microej.bench.suites;

import com.example.microej.bench.BenchResult;
import com.example.microej.bench.BenchStatus;
import com.example.microej.bench.BenchUtil;

import ej.microui.MicroUI;

/**
 * Marshals bench work onto the MicroUI thread.
 * <p>
 * MicroUI primitives (BufferedImage, VectorGraphicsPainter, etc.) are
 * single-threaded — calling them from a worker thread throws. Bench suites that
 * need to draw must dispatch via {@link MicroUI#callSerially(Runnable)} and the
 * bench thread waits on a lock until the UI thread finishes.
 */
final class UiThreadBenchUtil {

	private UiThreadBenchUtil() {}

	interface UiWork {
		BenchResult run(long start);
	}

	/**
	 * Schedules {@code work} on the MicroUI thread, blocks until it completes
	 * (or {@code timeoutMs} elapses), and returns the resulting BenchResult.
	 */
	static BenchResult runOnUiThread(final String name, final long timeoutMs, final UiWork work) {
		final long start = BenchUtil.nowMs();
		final BenchResult[] holder = new BenchResult[1];
		final Object lock = new Object();

		try {
			MicroUI.callSerially(new Runnable() {
				@Override public void run() {
					BenchResult r;
					try {
						r = work.run(start);
					} catch (Throwable t) {
						r = BenchUtil.fail(name, start, t);
					}
					synchronized (lock) {
						holder[0] = r;
						lock.notifyAll();
					}
				}
			});
		} catch (Throwable schedErr) {
			return BenchUtil.fail(name, start, schedErr);
		}

		synchronized (lock) {
			long deadline = BenchUtil.nowMs() + timeoutMs;
			while (holder[0] == null) {
				long remaining = deadline - BenchUtil.nowMs();
				if (remaining <= 0) break;
				try {
					lock.wait(remaining);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
			}
			if (holder[0] == null) {
				return new BenchResult(name, BenchStatus.FAIL, BenchUtil.nowMs() - start,
						"UI thread bench timeout after " + timeoutMs + " ms");
			}
			return holder[0];
		}
	}
}
