package com.example.microej.bench.suites;

import com.example.microej.bench.*;

/**
 * CPU usage/performance-oriented suite.
 * <p>
 * True "CPU %" requires OS/RTOS support, so we expose a lightweight compute-throughput
 * metric (ops/sec) that still helps compare builds/boards.
 */
public class CpuBenchSuite implements BenchSuite {

	@Override
	public String getName() {
		return "CPU";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Integer loop throughput"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						final long windowMs = 500;
						long end = start + windowMs;
						int x = 1;
						long it = 0;
						while (BenchUtil.nowMs() < end) {
							// Some deterministic arithmetic to keep the interpreter/JIT busy.
							x = (x * 1103515245 + 12345);
							it++;
						}
						long dur = Math.max(1, BenchUtil.nowMs() - start);
						long opsPerSec = (it * 1000L) / dur;
						BenchResult r = BenchUtil.pass(getName(), start, "opsPerSec=" + opsPerSec);
						r.metrics = BenchMetrics.of(BenchMetricKeys.CPU_OPS_PER_SEC, String.valueOf(opsPerSec));
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
