package com.example.microej.bench.suites;

import com.example.microej.NativeDriverIntegration;
import com.example.microej.bench.*;

/** Memory usage suite using Runtime heap information. */
public class MemoryBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "Memory";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Heap snapshot"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						Runtime rt = Runtime.getRuntime();
						long free = rt.freeMemory();
						long total = rt.totalMemory();
						long used = total - free;
						BenchUtil.require(total > 0, "totalMemory <= 0");
						BenchUtil.require(free >= 0 && free <= total, "freeMemory out of range");

						long nativeTotal = -1;
						long nativeFree = -1;
						long nativeUsed = -1;
						try {
							nativeTotal = NativeDriverIntegration.getNativeHeapTotalBytes();
							nativeFree = NativeDriverIntegration.getNativeHeapFreeBytes();
							if (nativeTotal >= 0 && nativeFree >= 0 && nativeFree <= nativeTotal) {
								nativeUsed = nativeTotal - nativeFree;
							}
						} catch (Throwable ignored) {
							// keep -1 if unsupported
						}

						String details = "java.used=" + used + " java.free=" + free + " java.total=" + total;
						BenchMetrics metrics = BenchMetrics.of(
								BenchMetricKeys.MEM_USED_BYTES, String.valueOf(used),
								BenchMetricKeys.MEM_FREE_BYTES, String.valueOf(free),
								BenchMetricKeys.MEM_TOTAL_BYTES, String.valueOf(total)
						);

						if (nativeTotal >= 0 && nativeFree >= 0) {
							details += " | native.used=" + nativeUsed + " native.free=" + nativeFree + " native.total=" + nativeTotal;
							metrics = BenchMetrics.of(
									BenchMetricKeys.MEM_USED_BYTES, String.valueOf(used),
									BenchMetricKeys.MEM_FREE_BYTES, String.valueOf(free),
									BenchMetricKeys.MEM_TOTAL_BYTES, String.valueOf(total),
									BenchMetricKeys.NATIVE_MEM_USED_BYTES, String.valueOf(nativeUsed),
									BenchMetricKeys.NATIVE_MEM_FREE_BYTES, String.valueOf(nativeFree),
									BenchMetricKeys.NATIVE_MEM_TOTAL_BYTES, String.valueOf(nativeTotal)
							);
						} else {
							details += " | native=unsupported";
						}

						BenchResult r = BenchUtil.pass(getName(), start, details);
						r.metrics = metrics;
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			},
			new BenchCase() {
				@Override public String getName() { return "GC effectiveness"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						Runtime rt = Runtime.getRuntime();
						long used1 = rt.totalMemory() - rt.freeMemory();
						try { System.gc(); } catch (Throwable ignored) { /* ignore */ }
						long used2 = rt.totalMemory() - rt.freeMemory();
						// Not all VMs change memory after GC; don't fail.
						BenchResult r = BenchUtil.pass(getName(), start, "usedBefore=" + used1 + " usedAfter=" + used2);
						r.metrics = BenchMetrics.of("mem.used.before.bytes", String.valueOf(used1), "mem.used.after.bytes", String.valueOf(used2));
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
