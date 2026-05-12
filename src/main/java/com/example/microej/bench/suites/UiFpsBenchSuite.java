package com.example.microej.bench.suites;

import com.example.microej.bench.*;

import ej.microui.display.BufferedImage;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microvg.Matrix;
import ej.microvg.VectorGraphicsPainter;
import ej.microvg.VectorImage;

/**
 * Off-screen UI FPS probe: animates the mascot VectorImage into a {@link BufferedImage}
 * for a fixed time window and reports frames/sec plus heap allocation delta.
 * <p>
 * Mirrors what {@code AnimatedMascotPage} does in production, minus pointer events
 * and display flush, so it gives a clean renderer throughput number.
 */
public class UiFpsBenchSuite implements BenchSuite {

	private static final String MASCOT_PATH = "/images/mascot.xml";
	// Small off-screen canvas — embedded image heaps are tight; 80x80 @ 16bpp = ~12KB.
	private static final int CANVAS_W = 80;
	private static final int CANVAS_H = 80;
	private static final long WINDOW_MS = 1000;

	@Override
	public String getName() {
		return "UI FPS (mascot)";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Mascot vector animation frames/sec"; }
				@Override public BenchResult run() {
					return UiThreadBenchUtil.runOnUiThread(getName(), 15000L, new UiThreadBenchUtil.UiWork() {
						@Override public BenchResult run(long start) {
							return measureMascotFps(getName(), start);
						}
					});
				}
			}
		};
	}

	private static BenchResult measureMascotFps(String name, long start) {
		BufferedImage canvas = null;
		try {
			VectorImage mascot = VectorImage.getImage(MASCOT_PATH);
			if (mascot == null) {
				return BenchUtil.skip(name, start, "mascot.xml not found");
			}

			canvas = new BufferedImage(CANVAS_W, CANVAS_H);
			GraphicsContext g = canvas.getGraphicsContext();

			Matrix m = new Matrix();
			float scale = Math.min(CANVAS_W / mascot.getWidth(), CANVAS_H / mascot.getHeight());
			m.setScale(scale, scale);

			Runtime rt = Runtime.getRuntime();
			try { System.gc(); } catch (Throwable ignored) { /* ignore */ }
			long usedBefore = rt.totalMemory() - rt.freeMemory();

			long t0 = BenchUtil.nowMs();
			long deadline = t0 + WINDOW_MS;
			long frames = 0;
			int elapsed = 0;
			int step = 16;
			while (BenchUtil.nowMs() < deadline) {
				g.setColor(0xFFFFFF);
				Painter.fillRectangle(g, 0, 0, CANVAS_W, CANVAS_H);
				VectorGraphicsPainter.drawAnimatedImage(g, mascot, m, elapsed);
				elapsed += step;
				if (mascot.getDuration() < elapsed) elapsed = 0;
				frames++;
			}
			long durMs = Math.max(1, BenchUtil.nowMs() - t0);
			long usedAfter = rt.totalMemory() - rt.freeMemory();
			long allocDelta = Math.max(0, usedAfter - usedBefore);
			long fps = (frames * 1000L) / durMs;
			long frameMs = durMs / Math.max(1, frames);

			String details = "frames=" + frames + " durMs=" + durMs
					+ " fps=" + fps + " frameMsAvg=" + frameMs
					+ " allocBytes=" + allocDelta;
			BenchResult r = BenchUtil.pass(name, start, details);
			r.metrics = BenchMetrics.of(
					BenchMetricKeys.UI_FPS, String.valueOf(fps),
					BenchMetricKeys.UI_FRAME_MS_AVG, String.valueOf(frameMs),
					"ui.alloc.bytes", String.valueOf(allocDelta)
			);
			return r;
		} catch (UnsatisfiedLinkError ule) {
			return BenchUtil.skip(name, start, "MicroVG native not available: " + ule.getMessage());
		} catch (Throwable t) {
			return BenchUtil.fail(name, start, t);
		} finally {
			if (canvas != null) {
				try { canvas.close(); } catch (Throwable ignored) { /* ignore */ }
			}
		}
	}
}
