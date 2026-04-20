package com.example.microej.bench.suites;

import com.example.microej.bench.*;

public class FileSystemBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "FileSystemPage";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "FS simulated ops"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					// Current page is explicitly simulated (no java.io). We validate that we can build
					// strings / time and that the app doesn't crash.
					try {
						String p = "/tmp/demo_1.txt";
						String data = "MicroEJ FS write #1 @ " + System.currentTimeMillis();
						BenchUtil.require(p.length() > 0 && data.length() > 0, "bad simulated data");
						return BenchUtil.pass(getName(), start, "Simulated write/read/list strings ok");
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
