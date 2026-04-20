package com.example.microej;

/**
 * Tiny API used to validate end-to-end native driver integration.
 * <p>
 * This is intentionally app-owned (no dependency on optional foundations like "hal").
 * The corresponding native functions must be provided by the VEE Port (SNI/C).
 */
public final class NativeDriverIntegration {

	/** NXP BSP {@code DEMO_PANEL_RK055AHD091} — 720x1280, RK055HDMIPI4M family. */
	public static final int BSP_PANEL_RK055AHD091 = 0;
	/** NXP BSP {@code DEMO_PANEL_RK055IQH091} — 540x960. */
	public static final int BSP_PANEL_RK055IQH091 = 1;
	/** NXP BSP {@code DEMO_PANEL_RK055MHD091} — 720x1280, RK055HDMIPI4MA0 (RK055MHD091A0-CTG). */
	public static final int BSP_PANEL_RK055MHD091 = 2;
	/** NXP BSP {@code DEMO_PANEL_RK043FN02H} — 480x272. */
	public static final int BSP_PANEL_RK043FN02H = 3;

	/**
	 * Sentinel returned by {@link #getMcuTempCentiCelsius()} when the TMPSNS read fails
	 * (does not collide with valid centi-Celsius in roughly {@code -8000..15000}).
	 */
	public static final int MCU_TEMP_READ_INVALID = -30000;

	private NativeDriverIntegration() {
	}

	/**
	 * Compile-time panel selection from BSP ({@code DEMO_PANEL} in {@code display_support.h}).
	 *
	 * @return one of {@link #BSP_PANEL_RK055AHD091}, {@link #BSP_PANEL_RK055IQH091},
	 *         {@link #BSP_PANEL_RK055MHD091}, {@link #BSP_PANEL_RK043FN02H}, or negative if unknown.
	 */
	public static native int getBspPanelId();

	/**
	 * Human-readable panel line for UI (kept in sync with NXP EVK {@code display_support.h} comments).
	 */
	public static String describeBspPanel(int bspPanelId) {
		switch (bspPanelId) {
			case BSP_PANEL_RK055AHD091:
				return "RK055AHD091-CTG (RK055HDMIPI4M)  720 x 1280";
			case BSP_PANEL_RK055IQH091:
				return "RK055IQH091-CTG  540 x 960";
			case BSP_PANEL_RK055MHD091:
				return "RK055MHD091A0-CTG (RK055HDMIPI4MA0)  720 x 1280";
			case BSP_PANEL_RK043FN02H:
				return "RK043FN02H (FT5336)  480 x 272";
			default:
				return "Unknown BSP panel id: " + bspPanelId;
		}
	}

	/**
	 * Returns a driver integration version/identifier from native code.
	 * <p>
	 * SOAR/SNI constraints: native methods must return base types.
	 *
	 * @return a positive version on success (e.g., 100 for 1.0.0), or a negative error code.
	 */
	public static native int getDriverVersion();

	/**
	 * Performs a small native self-test.
	 * <p>
	 * Expected to check that the BSP/driver layer can be called safely.
	 *
	 * @return 0 on success; non-zero error code on failure.
	 */
	public static native int selfTest();

	/**
	 * Optional: performs a visible hardware action (e.g., blink an LED) to prove the end-to-end path.
	 *
	 * @param times number of blinks, must be &gt;= 1.
	 * @param periodMs blink period in milliseconds, must be &gt;= 1.
	 * @return 0 on success; non-zero error code on failure.
	 */
	public static native int blinkLed(int times, int periodMs);

	/**
	 * Sets the state of the real hardware USER_LED.
	 *
	 * @param on true to turn on, false to turn off.
	 */
	public static native void setLedState(boolean on);

	/**
	 * Reads the state of the real hardware USER_BUTTON.
	 *
	 * @return true if pressed, false otherwise.
	 */
	public static native boolean getButtonState();

	/**
	 * On-die MCU temperature for i.MX RT1170 (NXP {@code TMPSNS} / {@code fsl_tempsensor}).
	 *
	 * @return temperature in centi-degrees Celsius (e.g. 3650 = 36.50\u00b0C), or
	 *         {@link #MCU_TEMP_READ_INVALID} if the sample is out of range or the peripheral failed.
	 */
	public static native int getMcuTempCentiCelsius();
}
