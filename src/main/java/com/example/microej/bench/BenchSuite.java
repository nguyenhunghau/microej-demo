package com.example.microej.bench;

/** A suite for one feature/page. */
public interface BenchSuite {
	String getName();

	BenchCase[] getCases();
}
