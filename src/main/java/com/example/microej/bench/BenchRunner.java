package com.example.microej.bench;

/** Runs suites sequentially. */
public final class BenchRunner {
	private BenchRunner() {
	}

	public static BenchResult[] runSuite(BenchSuite suite) {
		BenchCase[] cases = suite.getCases();
		BenchResult[] results = new BenchResult[cases.length];
		for (int i = 0; i < cases.length; i++) {
			BenchCase c = cases[i];
			BenchResult r;
			try {
				r = c.run();
			} catch (Throwable t) {
				long start = BenchUtil.nowMs();
				r = BenchUtil.fail(c.getName(), start, t);
			}
			results[i] = r;
		}
		return results;
	}
}
