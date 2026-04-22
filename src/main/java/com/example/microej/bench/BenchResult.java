package com.example.microej.bench;

/** Result of one benchmark test case execution. */
public final class BenchResult {
	public final String name;
	public final BenchStatus status;
	public final long durationMs;
	public final String details;

	/** Optional structured metrics (may be null). */
	public BenchMetrics metrics;

	public BenchResult(String name, BenchStatus status, long durationMs, String details) {
		this(name, status, durationMs, details, null);
	}

	public BenchResult(String name, BenchStatus status, long durationMs, String details, BenchMetrics metrics) {
		this.name = name;
		this.status = status;
		this.durationMs = durationMs;
		this.details = details;
		this.metrics = metrics;
	}
}
