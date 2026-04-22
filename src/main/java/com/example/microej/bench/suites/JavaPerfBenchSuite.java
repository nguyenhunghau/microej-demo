package com.example.microej.bench.suites;

import com.example.microej.bench.*;

/** Java execution speed suite (math, string processing, parsing). */
public class JavaPerfBenchSuite implements BenchSuite {

	@Override
	public String getName() {
		return "Java";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Math loop (int)"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						final long windowMs = 500;
						long end = start + windowMs;
						int a = 1;
						long it = 0;
						while (BenchUtil.nowMs() < end) {
							a ^= (a << 13);
							a ^= (a >>> 17);
							a ^= (a << 5);
							it++;
						}
						long dur = Math.max(1, BenchUtil.nowMs() - start);
						long opsPerSec = (it * 1000L) / dur;
						BenchResult r = BenchUtil.pass(getName(), start, "opsPerSec=" + opsPerSec + " sink=" + a);
						r.metrics = BenchMetrics.of(BenchMetricKeys.JAVA_OPS_PER_SEC, String.valueOf(opsPerSec));
						return r;
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			},
			new BenchCase() {
				@Override public String getName() { return "String append (StringBuilder)"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						final long windowMs = 400;
						long end = start + windowMs;
						long it = 0;
						int sink = 0;
						while (BenchUtil.nowMs() < end) {
							StringBuilder sb = new StringBuilder(64);
							sb.append("abc").append(123).append('-').append("def");
							sink ^= sb.length();
							it++;
						}
						long dur = Math.max(1, BenchUtil.nowMs() - start);
						long opsPerSec = (it * 1000L) / dur;
						return BenchUtil.pass(getName(), start, "opsPerSec=" + opsPerSec + " sink=" + sink);
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			},
			new BenchCase() {
				@Override public String getName() { return "Data parsing (int from String)"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						final long windowMs = 400;
						long end = start + windowMs;
						long it = 0;
						int sink = 0;
						while (BenchUtil.nowMs() < end) {
							sink ^= Integer.parseInt("123456");
							it++;
						}
						long dur = Math.max(1, BenchUtil.nowMs() - start);
						long opsPerSec = (it * 1000L) / dur;
						return BenchUtil.pass(getName(), start, "opsPerSec=" + opsPerSec + " sink=" + sink);
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
