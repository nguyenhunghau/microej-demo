package com.example.microej.bench.suites;

import com.example.microej.bench.NativeDriverFacade;
import com.example.microej.bench.*;

import ej.microui.display.Display;

public class DisplayHardwareBenchSuite implements BenchSuite {
	@Override
	public String getName() {
		return "DisplayHardwarePage";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "Display.getDisplay basics"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						Display d = Display.getDisplay();
						BenchUtil.require(d.getWidth() > 0, "width <= 0");
						BenchUtil.require(d.getHeight() > 0, "height <= 0");
						BenchUtil.require(d.getPixelDepth() > 0, "pixelDepth <= 0");
						return BenchUtil.pass(getName(), start, d.getWidth() + "x" + d.getHeight() + " " + d.getPixelDepth() + "bpp");
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			},
			new BenchCase() {
				@Override public String getName() { return "Native driver version reachable"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();
					try {
						int ver = NativeDriverFacade.getDriverVersion();
						BenchUtil.require(ver >= 0, "driver version negative: " + ver);
						return BenchUtil.pass(getName(), start, "ver=" + ver);
					} catch (Throwable t) {
						return BenchUtil.fail(getName(), start, t);
					}
				}
			}
		};
	}
}
