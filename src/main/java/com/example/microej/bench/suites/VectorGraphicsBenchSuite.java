package com.example.microej.bench.suites;

import com.example.microej.AppStyle;
import com.example.microej.bench.*;

import ej.microui.display.BufferedImage;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;

/**
 * Vector raster throughput probe: draws the same arc pattern that
 * {@code VectorGraphicsPage} renders, but into a {@link BufferedImage} for a fixed
 * time window. Reports arcs/sec, frames/sec, and ms/frame.
 * <p>
 * Useful because arc rasterization is one of the most expensive 2D primitives on
 * embedded GPUs / software renderers — regressions here are usually invisible from
 * coarser benches like CPU ops/sec.
 */
public class VectorGraphicsBenchSuite implements BenchSuite {

	// Small off-screen canvas — embedded image heaps are tight; 80x80 @ 16bpp = ~12KB.
	private static final int CANVAS_W = 80;
	private static final int CANVAS_H = 80;
	private static final long WINDOW_MS = 1000;
	/** Per-frame arc count (matches VectorGraphicsPage: 10 colors * 2 arcs). */
	private static final int ARCS_PER_FRAME = 20;

	@Override
	public String getName() {
		return "Vector graphics";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "drawCircleArc throughput"; }
				@Override public BenchResult run() {
					return UiThreadBenchUtil.runOnUiThread(getName(), 15000L, new UiThreadBenchUtil.UiWork() {
						@Override public BenchResult run(long start) {
							return measureArcsFps(getName(), start);
						}
					});
				}
			}
		};
	}

	private static BenchResult measureArcsFps(String name, long start) {
		BufferedImage canvas = null;
		try {
			canvas = new BufferedImage(CANVAS_W, CANVAS_H);
			GraphicsContext g = canvas.getGraphicsContext();

			int[] colors = { AppStyle.INDIGO, AppStyle.PURPLE, AppStyle.CYAN, AppStyle.BLUE,
					AppStyle.TEAL, AppStyle.GREEN, AppStyle.YELLOW, AppStyle.ORANGE,
					AppStyle.RED, AppStyle.PINK };

			long t0 = BenchUtil.nowMs();
			long deadline = t0 + WINDOW_MS;
			long frames = 0;
			int cx = CANVAS_W / 2, cy = CANVAS_H / 2;
			float t = 0f;

			while (BenchUtil.nowMs() < deadline) {
				g.setColor(AppStyle.BG_DARK);
				Painter.fillRectangle(g, 0, 0, CANVAS_W, CANVAS_H);

				for (int i = 0; i < colors.length; i++) {
					int r = 4 + i * 3;   // scaled to fit CANVAS_W=80
					float speed = 60f + i * 30f;
					int startAngle = (int) ((t * speed) % 360);
					int arcAngle = 90 + i * 18;
					g.setColor(colors[i]);
					Painter.drawCircleArc(g, cx - r, cy - r, r * 2, startAngle, arcAngle);
					int startAngle2 = ((int) ((-t * speed * 0.7f + 180) % 360) + 360) % 360;
					int r2 = Math.max(2, r - 2);
					g.setColor(dimColor(colors[i]));
					Painter.drawCircleArc(g, cx - r2, cy - r2, r2 * 2, startAngle2, arcAngle + 30);
				}

				frames++;
				t += 0.016f;
			}
			long durMs = Math.max(1, BenchUtil.nowMs() - t0);
			long fps = (frames * 1000L) / durMs;
			long arcsPerSec = fps * ARCS_PER_FRAME;
			long frameMs = durMs / Math.max(1, frames);

			String details = "frames=" + frames + " fps=" + fps
					+ " arcsPerSec=" + arcsPerSec + " frameMsAvg=" + frameMs;
			BenchResult r = BenchUtil.pass(name, start, details);
			r.metrics = BenchMetrics.of(
					"vg.fps", String.valueOf(fps),
					"vg.arcsPerSec", String.valueOf(arcsPerSec),
					"vg.frame.ms.avg", String.valueOf(frameMs)
			);
			return r;
		} catch (UnsatisfiedLinkError ule) {
			return BenchUtil.skip(name, start, "Drawing native not available: " + ule.getMessage());
		} catch (Throwable t) {
			return BenchUtil.fail(name, start, t);
		} finally {
			if (canvas != null) {
				try { canvas.close(); } catch (Throwable ignored) { /* ignore */ }
			}
		}
	}

	private static int dimColor(int color) {
		int r  = ((color >> 16) & 0xFF) / 3;
		int gg = ((color >> 8)  & 0xFF) / 3;
		int b  = (color & 0xFF) / 3;
		return (r << 16) | (gg << 8) | b;
	}
}
