package com.example.microej.bench.suites;

import com.example.microej.bench.*;

/** Startup time suite (requires StartupProbe integration). */
public class StartupBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "Startup";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Time to first screen"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						long uiMs = StartupProbe.getUiFirstScreenShownMs();
						if (uiMs == 0) {
							return BenchUtil.skip(getName(), start,
									"StartupProbe not marked yet. Call StartupProbe.markUiFirstScreenShown() when first page is displayed.");
						}
						long appLoadedMs = StartupProbe.getAppClassLoadedMs();
						long delta = Math.max(0, uiMs - appLoadedMs);
						BenchResult r = BenchUtil.pass(getName(), start, "startupMs=" + delta + " (appLoaded->uiShown)");
						r.metrics = BenchMetrics.of(BenchMetricKeys.STARTUP_MS, String.valueOf(delta));
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
