package com.example.microej;

/**
 * Small runtime-environment helpers.
 * <p>
 * We avoid depending on optional MicroEJ libraries (like BON) so this code can
 * compile for both simulator and device builds.
 */
public final class RuntimeEnv {

	private RuntimeEnv() {
		// static-only
	}

	/**
	 * Best-effort detection.
	 * <ul>
	 * <li>When running on Simulator via Gradle, you can force it with
	 * {@code -Dapp.simulator=true}.</li>
	 * <li>Otherwise we try to detect simulator-only classes.</li>
	 * <li>Fallback: assume device.</li>
	 * </ul>
	 */
	public static boolean isSimulator() {
		try {
			if (Boolean.getBoolean("app.simulator")) {
				return true;
			}
		} catch (Throwable ignored) {
			// ignore
		}

		// Heuristics: try a couple of common simulator-only class names.
		// If they don't exist, Class.forName throws and we keep looking.
		String[] markers = new String[] {
				"com.microej.simulator.Simulator",
				"com.is2t.microui.simulation.Simulator",
				"com.is2t.mwt.simulation.Simulator"
		};

		for (int i = 0; i < markers.length; i++) {
			try {
				Class.forName(markers[i]);
				return true;
			} catch (Throwable ignored) {
				// ignore
			}
		}
		return false;
	}

	public static String getEnvLabel() {
		return isSimulator() ? "SIMULATOR" : "DEVICE";
	}
}
