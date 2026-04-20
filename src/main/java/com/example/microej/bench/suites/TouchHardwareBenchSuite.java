package com.example.microej.bench.suites;

import com.example.microej.bench.*;

public class TouchHardwareBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "TouchHardwarePage";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Touch interactive (manual)"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					// Touch requires human interaction to fully validate; this is a placeholder case.
					return BenchUtil.skip(getName(), start, "Requires user to touch screen; verify Touch page updates rawX/rawY.");
				}
			}
		};
	}
}
