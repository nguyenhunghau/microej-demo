package com.example.microej.bench;

/** Common metric keys used by benchmark suites. */
public final class BenchMetricKeys {
	private BenchMetricKeys() {}

	public static final String STARTUP_MS = "startup.ms";

	public static final String MEM_USED_BYTES = "mem.used.bytes";
	public static final String MEM_FREE_BYTES = "mem.free.bytes";
	public static final String MEM_TOTAL_BYTES = "mem.total.bytes";

	public static final String NATIVE_MEM_TOTAL_BYTES = "native.mem.total.bytes";
	public static final String NATIVE_MEM_FREE_BYTES = "native.mem.free.bytes";
	public static final String NATIVE_MEM_USED_BYTES = "native.mem.used.bytes";

	public static final String JAVA_OPS_PER_SEC = "java.opsPerSec";
	public static final String CPU_OPS_PER_SEC = "cpu.opsPerSec";

	public static final String UI_FPS = "ui.fps";
	public static final String UI_FRAME_MS_AVG = "ui.frame.ms.avg";

	public static final String COMM_BYTES_PER_SEC = "comm.bytesPerSec";
	public static final String COMM_LATENCY_MS = "comm.latency.ms";
	public static final String COMM_CONNECT_MS = "comm.connect.ms";
	public static final String COMM_HOST = "comm.host";
	public static final String COMM_PORT = "comm.port";
	public static final String COMM_BYTES = "comm.bytes";

	public static final String MCU_TEMP_C = "mcu.temp.c";

	public static final String POWER_NOTE = "power.note";
	public static final String POWER_CURRENT_MA = "power.current.ma";
	public static final String POWER_VOLTAGE_MV = "power.voltage.mv";
	public static final String POWER_MW = "power.mw";
}
