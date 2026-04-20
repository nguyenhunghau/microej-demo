package com.example.microej.bench;

import ej.bon.Util;

public final class BenchUtil {
	private BenchUtil() {
	}

	public static long nowMs() {
		try {
			return Util.platformTimeMillis();
		} catch (Throwable t) {
			return System.currentTimeMillis();
		}
	}

	public static BenchResult pass(String name, long startMs, String details) {
		return new BenchResult(name, BenchStatus.PASS, Math.max(0, nowMs() - startMs), details);
	}

	public static BenchResult fail(String name, long startMs, Throwable error) {
		String d = error == null ? "" : (error.getClass().getName() + (error.getMessage() != null ? (": " + error.getMessage()) : ""));
		return new BenchResult(name, BenchStatus.FAIL, Math.max(0, nowMs() - startMs), d);
	}

	public static BenchResult skip(String name, long startMs, String details) {
		return new BenchResult(name, BenchStatus.SKIP, Math.max(0, nowMs() - startMs), details);
	}

	public static void require(boolean condition, String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}
}
