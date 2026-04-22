package com.example.microej.bench.suites;

import com.example.microej.bench.*;

public class DeviceInfoBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "DeviceInfoPage";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Device: "; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						String arch = ej.util.Device.getArchitecture();
						byte[] id = ej.util.Device.getId();
						BenchUtil.require(arch != null && arch.length() > 0, "architecture is empty");
						BenchUtil.require(id != null && id.length > 0, "device id is empty");
						return BenchUtil.pass(getName(), start, "arch=" + arch + " idLen=" + id.length);
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			},
			new BenchCase() {
				@Override public String getName() { return "Runtime memory snapshot"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						Runtime rt = Runtime.getRuntime();
						long free = rt.freeMemory();
						long total = rt.totalMemory();
						BenchUtil.require(total > 0, "totalMemory <= 0");
						BenchUtil.require(free >= 0 && free <= total, "freeMemory out of range");
						return BenchUtil.pass(getName(), start, "free=" + free + " total=" + total);
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
