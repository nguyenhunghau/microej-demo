package com.example.microej.bench;

/** A single benchmark / self-test case. */
public interface BenchCase {
	String getName();

	/**
	 * Executes the test.
	 *
	 * @return result (PASS/FAIL/SKIP)
	 */
	BenchResult run();
}
