package com.example.microej.bench.suites;

import com.example.microej.bench.BenchCase;
import com.example.microej.bench.BenchMetricKeys;
import com.example.microej.bench.BenchMetrics;
import com.example.microej.bench.BenchResult;
import com.example.microej.bench.BenchSuite;
import com.example.microej.bench.BenchUtil;
import com.example.microej.bench.NativeDriverFacade;

/**
 * Power consumption suite.
 * <p>
 * Uses BSP native integration when available; otherwise returns SKIP with guidance.
 */
public class PowerBenchSuite implements BenchSuite {
	private static final int DEFAULT_SUPPLY_MV = 5000;

	@Override
	public String getName() {
		return "Power";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Power measurement"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						int currentMa = NativeDriverFacade.getBoardCurrentMilliAmps();
						if (currentMa == NativeDriverFacade.POWER_CURRENT_INVALID) {
							BenchResult skip = BenchUtil.skip(getName(), start,
									"Current sensor unavailable. Implement BSP native hook or use external power meter.");
							skip.metrics = BenchMetrics.of(BenchMetricKeys.POWER_NOTE, "unsupported");
							return skip;
						}

						int supplyMv = getIntProp("bench.power.supplyMv", DEFAULT_SUPPLY_MV, 1, 24000);
						long powerMw = ((long) currentMa * (long) supplyMv) / 1000L;

						BenchResult r = BenchUtil.pass(getName(), start,
								"currentMa=" + currentMa + " supplyMv=" + supplyMv + " powerMw=" + powerMw);
						r.metrics = new BenchMetrics(
								new String[] {
										BenchMetricKeys.POWER_CURRENT_MA,
										BenchMetricKeys.POWER_VOLTAGE_MV,
										BenchMetricKeys.POWER_MW,
										BenchMetricKeys.POWER_NOTE
								},
								new String[] {
										String.valueOf(currentMa),
										String.valueOf(supplyMv),
										String.valueOf(powerMw),
										"native-sensor"
								});
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}

	private static int getIntProp(String key, int def, int min, int max) {
		try {
			String raw = System.getProperty(key);
			if (raw == null || raw.trim().length() == 0) {
				return clamp(def, min, max);
			}
			return clamp(Integer.parseInt(raw.trim()), min, max);
		} catch (Throwable ignored) {
			return clamp(def, min, max);
		}
	}

	private static int clamp(int v, int min, int max) {
		return (v < min) ? min : (v > max) ? max : v;
	}
}
