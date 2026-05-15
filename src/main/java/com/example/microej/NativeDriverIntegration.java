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
	/**
	 * Sentinel returned by {@link #getBoardCurrentMilliAmps()} when current measurement
	 * is not available on the current BSP/hardware setup.
	 */
	public static final int POWER_CURRENT_INVALID = -1;

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

	/**
	 * Reads board current in milli-amps using BSP-specific instrumentation.
	 * <p>
	 * Typical implementation options:
	 * <ul>
	 *   <li>external power monitor over I2C (INA219/INA226, etc.)</li>
	 *   <li>on-board measurement circuitry if available</li>
	 * </ul>
	 *
	 * @return current in mA, or {@link #POWER_CURRENT_INVALID} if unsupported/unavailable.
	 */
	public static native int getBoardCurrentMilliAmps();

	/**
	 * Board-level (native) memory: total bytes available to the C heap/allocator used by the platform.
	 * <p>
	 * This is NOT the Java heap. It is intended to report real device memory (e.g., FreeRTOS heap,
	 * or another native allocator), when implemented by the VEE Port.
	 *
	 * @return total native heap bytes, or -1 if unsupported.
	 */
	public static native long getNativeHeapTotalBytes();

	/**
	 * Board-level (native) memory: free bytes available to the C heap/allocator used by the platform.
	 *
	 * @return free native heap bytes, or -1 if unsupported.
	 */
	public static native long getNativeHeapFreeBytes();

	/**
	 * Optional: initializes a native CPU load measurement facility when available.
	 * <p>
	 * On the RT1170 VEE Port, CPU load is typically measured using an RTOS idle hook.
	 *
	 * @return 0 on success, or a negative value if unsupported/failed.
	 */
	public static native int initCpuLoad();

	/**
	 * Optional: returns an instantaneous/averaged CPU load percentage.
	 *
	 * @return 0..100 when supported, or a negative value when unsupported.
	 */
	public static native int getCpuLoadPercent();

	/**
	 * Optional: returns the instantaneous CPU load in permille (0..1000).
	 * <p>
	 * This is more precise than {@link #getCpuLoadPercent()} and avoids rounding to an integer percent.
	 *
	 * @return 0..1000 when supported, or a negative value when unsupported.
	 */
	public static native int getCpuLoadPermille();

	/**
	 * Optional debug: raw idle counter captured during the last CPU load sampling interval.
	 *
	 * @return non-negative counter value when supported, or a negative value when unsupported.
	 */
	public static native int getCpuIdleCounter();

	/**
	 * Optional debug: reference counter used to normalize the idle counter.
	 *
	 * @return non-negative counter value when supported, or a negative value when unsupported.
	 */
	public static native int getCpuReferenceCounter();

	/** DHCP state constants — must match lwip_util.c */
	public static final int DHCP_START            = 1;
	public static final int DHCP_WAIT_ADDRESS     = 2;
	public static final int DHCP_ADDRESS_ASSIGNED = 3;
	public static final int DHCP_TIMEOUT          = 4;
	public static final int DHCP_LINK_DOWN        = 5;

	/**
	 * Returns the IPv4 address of the default lwIP network interface (netif_default-&gt;ip_addr.addr)
	 * as a 32-bit value in network byte order. Returns 0 when no address has been assigned yet.
	 * <p>
	 * Convert to dotted-decimal in Java:
	 * <pre>
	 *   int ip = getNetworkIpInt();
	 *   String s = (ip&amp;0xFF)+"."+((ip&gt;&gt;8)&amp;0xFF)+"."+((ip&gt;&gt;16)&amp;0xFF)+"."+((ip&gt;&gt;24)&amp;0xFF);
	 * </pre>
	 *
	 * @return 32-bit IPv4 address, or 0 if not assigned / netif not ready.
	 */
	public static native int getNetworkIpInt();

	/**
	 * Returns the current DHCP state from the C layer (g_dhcp_state in lwip_util.c).
	 * Compare with {@link #DHCP_START}, {@link #DHCP_WAIT_ADDRESS}, {@link #DHCP_ADDRESS_ASSIGNED},
	 * {@link #DHCP_TIMEOUT}, {@link #DHCP_LINK_DOWN}.
	 *
	 * @return DHCP state code, or -1 if unsupported.
	 */
	public static native int getDhcpState();

	/**
	 * Returns 1 if the default lwIP network interface has physical link up
	 * ({@code netif_is_link_up(netif_default)}), 0 if link is down or netif is null.
	 */
	public static native int isNetworkLinkUp();

	/**
	 * Returns the {@code netif->flags} bitmask of {@code netif_default} (e.g. 0xE7).
	 * Useful for diagnosing NETIF_FLAG_UP (0x01), NETIF_FLAG_LINK_UP (0x40), etc.
	 * Returns -1 if netif_default is NULL.
	 */
	public static native int getNetifFlags();

	/**
	 * Returns the {@code netif->num} of {@code netif_default}.
	 * 0 = eth0 (ENET, 100M), 1 = eth1 (ENET_1G, 1Gbps).
	 * Returns -1 if netif_default is NULL.
	 */
	public static native int getNetifNum();

	/**
	 * Returns one byte of the MAC address of {@code netif_default}.
	 * {@code index} must be 0..5. Returns -1 if netif_default is NULL or index out of range.
	 */
	public static native int getNetifMacByte(int index);

	/**
	 * Returns a bitmask of link state for ALL known netifs:
	 * bit 0 = g_netif0 link up, bit 1 = g_netif1 link up.
	 * Allows checking both Ethernet ports at once.
	 * Returns -1 if neither port is available.
	 */
	public static native int getAllNetifLinkMask();

	/** Formats a MAC address from 6 calls to {@link #getNetifMacByte}. */
	public static String readNetifMac() {
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 6; i++) {
				if (i > 0) {
					sb.append(':');
				}
				int b = getNetifMacByte(i) & 0xFF;
				if (b < 0x10) {
					sb.append('0');
				}
				sb.append(Integer.toHexString(b));
			}
			return sb.toString();
		} catch (Throwable t) {
			return "MAC read failed";
		}
	}

	/** Decodes {@link #getNetifFlags()} into a human-readable string. */
	public static String describeNetifFlags(int flags) {
		if (flags < 0) {
			return "netif=NULL";
		}
		StringBuffer sb = new StringBuffer("0x");
		sb.append(Integer.toHexString(flags));
		sb.append(" [");
		if ((flags & 0x01) != 0) { sb.append("UP "); }
		if ((flags & 0x02) != 0) { sb.append("BROADCAST "); }
		if ((flags & 0x04) != 0) { sb.append("POINTTOPOINT "); }
		if ((flags & 0x08) != 0) { sb.append("DHCP "); }
		if ((flags & 0x10) != 0) { sb.append("IGMP "); }
		if ((flags & 0x20) != 0) { sb.append("MLD6 "); }
		if ((flags & 0x40) != 0) { sb.append("LINK_UP "); }
		if ((flags & 0x80) != 0) { sb.append("ETHARP "); }
		sb.append("]");
		return sb.toString();
	}

	/** Formats the value returned by {@link #getNetworkIpInt()} to a dotted-decimal string. */
	public static String formatIpInt(int addr) {
		return (addr & 0xFF) + "." + ((addr >> 8) & 0xFF) + "." + ((addr >> 16) & 0xFF) + "." + ((addr >> 24) & 0xFF);
	}

	/** Human-readable DHCP state label. */
	public static String describeDhcpState(int state) {
		switch (state) {
			case DHCP_START:            return "DHCP starting...";
			case DHCP_WAIT_ADDRESS:     return "DHCP waiting for address...";
			case DHCP_ADDRESS_ASSIGNED: return "DHCP address assigned";
			case DHCP_TIMEOUT:          return "DHCP timeout (no server?)";
			case DHCP_LINK_DOWN:        return "Link down (check cable)";
			default:                    return "DHCP state: " + state;
		}
	}
}
