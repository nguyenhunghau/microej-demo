package com.example.microej;

/**
 * Lightweight, always-safe click logger for UI buttons.
 * <p>
 * This is intentionally console-only (System.out) so it works both on simulator
 * and on real hardware (serial console).
 */
public final class UiClickLog {

    /** Enable / disable click logs globally. */
    private static final boolean ENABLED = true;

    private UiClickLog() {
        // no-op
    }

    public static void click(String page, String buttonLabel) {
        click(page, buttonLabel, null);
    }

    public static void click(String page, String buttonLabel, String action) {
        if (!ENABLED) {
            return;
        }
        try {
            long ts;
            try {
                // Prefer BON time (monotonic in some ports) when available.
                ts = ej.bon.Util.platformTimeMillis();
            } catch (Throwable t) {
                ts = System.currentTimeMillis();
            }

            StringBuilder sb = new StringBuilder(96);
            sb.append("[UI_CLICK] page=").append(page != null ? page : "?");
            sb.append(" btn=\"").append(buttonLabel != null ? buttonLabel : "?").append('"');
            if (action != null && action.length() > 0) {
                sb.append(" action=").append(action);
            }
            sb.append(" t=").append(ts);
            System.out.println(sb.toString());
        } catch (Throwable ignored) {
            // Never crash on logging.
        }
    }
}

