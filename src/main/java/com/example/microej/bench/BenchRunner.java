package com.example.microej.bench;

/** Runs suites sequentially. */
public final class BenchRunner {
	private BenchRunner() {
	}

	private static void log(String msg) {
		try {
			System.out.println("[BenchRun] " + msg);
		} catch (Throwable ignored) {
			// ignore
		}
	}

	public static BenchResult[] runSuite(BenchSuite suite) {
		BenchCase[] cases = suite.getCases();
		BenchResult[] results = new BenchResult[cases.length];
		for (int i = 0; i < cases.length; i++) {
			BenchCase c = cases[i];
			BenchResult r;
			long start = BenchUtil.nowMs();
			log("START suite='" + suite.getName() + "' case='" + c.getName() + "'");
			try {
				r = c.run();
			} catch (Throwable t) {
				r = BenchUtil.fail(c.getName(), start, t);
			}
			long dur = Math.max(0, BenchUtil.nowMs() - start);
			log("END   suite='" + suite.getName() + "' case='" + c.getName() + "' status=" + r.status + " durMs=" + dur + (r.details != null && r.details.length() > 0 ? (" details='" + r.details + "'") : ""));
			results[i] = r;
		}
		return results;
	}
}
