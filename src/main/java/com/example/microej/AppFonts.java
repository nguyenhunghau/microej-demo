package com.example.microej;

import ej.microui.display.Font;

/**
 * Central place for application fonts.
 * <p>
 * On embedded targets, the platform/system font can be small. We try to load a bundled font resource
 * if available and fall back to the default font.
 */
public final class AppFonts {
	private AppFonts() {
	}

	public static Font getUiFont() {
		// Let the platform decide which font to use (can be overridden by a property in the VEE Port).
		// If not configured, fall back to the default system font.
		try {
			String fontPath = System.getProperty("app.ui.font");
			if (fontPath != null && fontPath.length() > 0) {
				System.out.println("[AppFonts] app.ui.font=" + fontPath);
				return Font.getFont(fontPath);
			}
		} catch (Throwable t) {
			System.out.println("[AppFonts] app.ui.font load failed: " + t.getMessage());
		}

		System.out.println("[AppFonts] Using default system font.");
		return Font.getDefaultFont();
	}
}
