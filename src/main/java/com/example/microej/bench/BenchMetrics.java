package com.example.microej.bench;

/**
 * Optional structured metrics for one benchmark result.
 * <p>
 * Kept intentionally lightweight and allocation-friendly for embedded targets.
 */
public final class BenchMetrics {

	public final String[] keys;
	public final String[] values;

	public BenchMetrics(String[] keys, String[] values) {
		this.keys = keys;
		this.values = values;
	}

	public static BenchMetrics of(String k1, String v1) {
		return new BenchMetrics(new String[] { k1 }, new String[] { v1 });
	}

	public static BenchMetrics of(String k1, String v1, String k2, String v2) {
		return new BenchMetrics(new String[] { k1, k2 }, new String[] { v1, v2 });
	}

	public static BenchMetrics of(String k1, String v1, String k2, String v2, String k3, String v3) {
		return new BenchMetrics(new String[] { k1, k2, k3 }, new String[] { v1, v2, v3 });
	}

	public static BenchMetrics of(String commHost, String host, String commPort, String s,
								  String commConnectMs, String s1, String commLatencyMs, String s2,
								  String commBytes, String s3, String commBytesPerSec, String s4) {
		return new BenchMetrics(new String[] { commHost, commPort, commConnectMs, commLatencyMs, commBytes, commBytesPerSec },
							 new String[] { host, s, s1, s2, s3, s4 });
	}
}
