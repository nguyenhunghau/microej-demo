package com.example.microej.bench.suites;

import com.example.microej.bench.NativeDriverFacade;
import com.example.microej.bench.*;

public class McuTempBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "McuTempPage";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Read TMPSNS once"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						int centi = NativeDriverFacade.getMcuTempCentiCelsius();
						if (centi == NativeDriverFacade.MCU_TEMP_READ_INVALID) {
							return BenchUtil.skip(getName(), start, "MCU temperature not supported / native integration missing");
						}
						// sanity bounds
						BenchUtil.require(centi > -4000 && centi < 12500, "temp out of expected range: " + centi);
						BenchResult r = BenchUtil.pass(getName(), start, "tempC=" + (centi / 100f));
						r.metrics = BenchMetrics.of(BenchMetricKeys.MCU_TEMP_C, String.valueOf(centi / 100f));
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
