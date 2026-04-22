package com.example.microej.bench;

import com.example.microej.NativeDriverIntegration;
import com.example.microej.RuntimeEnv;

/**
 * Facade used by benchmarks to support both Simulator and Device.
 * <p>
 * Note: MicroEJ's class library may not include java.lang.reflect, so keep this facade
 * free of reflection.
 */
public final class NativeDriverFacade {

	private NativeDriverFacade() {
	}

	/** Same semantic as {@code NativeDriverIntegration.MCU_TEMP_READ_INVALID}. */
	public static final int MCU_TEMP_READ_INVALID = NativeDriverIntegration.MCU_TEMP_READ_INVALID;
	/** Same semantic as {@code NativeDriverIntegration.POWER_CURRENT_INVALID}. */
	public static final int POWER_CURRENT_INVALID = NativeDriverIntegration.POWER_CURRENT_INVALID;

	private static void log(String msg) {
		try {
			System.out.println("[BenchNative] " + msg);
		} catch (Throwable ignored) {
			// ignore
		}
	}

	/**
	 * Reads MCU temperature in centi-celsius.
	 *
	 * @return temperature, or {@link #MCU_TEMP_READ_INVALID} if not available.
	 */
	public static int getMcuTempCentiCelsius() {
		if (RuntimeEnv.isSimulator()) {
			// Deterministic mock; keep in a realistic range.
			return 3650;
		}
		try {
			return NativeDriverIntegration.getMcuTempCentiCelsius();
		} catch (Throwable t) {
			log("getMcuTempCentiCelsius failed: " + t);
			return MCU_TEMP_READ_INVALID;
		}
	}

	/**
	 * Reads driver version.
	 *
	 * @return a non-negative version on success; negative indicates unsupported/error.
	 */
	public static int getDriverVersion() {
		if (RuntimeEnv.isSimulator()) {
			return 100; // 1.0.0 mock
		}
		try {
			return NativeDriverIntegration.getDriverVersion();
		} catch (Throwable t) {
			log("getDriverVersion failed: " + t);
			return -1;
		}
	}

	/**
	 * Reads board current in milli-amps when BSP instrumentation is available.
	 *
	 * @return current in mA, or {@link #POWER_CURRENT_INVALID} if not available.
	 */
	public static int getBoardCurrentMilliAmps() {
		if (RuntimeEnv.isSimulator()) {
			return POWER_CURRENT_INVALID;
		}
		try {
			return NativeDriverIntegration.getBoardCurrentMilliAmps();
		} catch (Throwable t) {
			log("getBoardCurrentMilliAmps failed: " + t);
			return POWER_CURRENT_INVALID;
		}
	}
}
