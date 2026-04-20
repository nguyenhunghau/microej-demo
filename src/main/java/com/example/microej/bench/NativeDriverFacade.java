package com.example.microej.bench;

/**
 * Facade used by benchmarks to support both Simulator and Device.
 * <p>
 * The MicroEJ Simulator uses a HIL classpath that doesn't necessarily contain application classes.
 * To avoid Simulator failures, we return deterministic mock values when we detect the Simulator.
 */
public final class NativeDriverFacade {

	private NativeDriverFacade() {
	}

	/** Same constants as NativeDriverIntegration, duplicated to avoid classloading it on Simulator. */
	public static final int MCU_TEMP_READ_INVALID = Integer.MIN_VALUE;

	/**
	 * Reads MCU temperature in centi-celsius.
	 *
	 * @return temperature or {@link #MCU_TEMP_READ_INVALID} if not available.
	 */
	public static int getMcuTempCentiCelsius() {
		return com.example.microej.NativeDriverIntegration.getMcuTempCentiCelsius();
	}

	/**
	 * Reads driver version. Returns negative on error.
	 */
	public static int getDriverVersion() {
		return com.example.microej.NativeDriverIntegration.getDriverVersion();
	}
}
