package com.example.microej.bench;

/**
 * Captures startup timestamps.
 * <p>
 * Call {@link #markAppClassLoaded()} as early as possible (ideally in Main static init)
 * and {@link #markUiFirstScreenShown()} when the first screen/page is displayed.
 */
public final class StartupProbe {

	private static final long JVM_CLASSLOAD_MS = BenchUtil.nowMs();
	private static volatile long appClassLoadedMs = JVM_CLASSLOAD_MS;
	private static volatile long uiFirstScreenShownMs = 0;

	private StartupProbe() {
	}

	private static void log(String msg) {
		try {
			System.out.println("[StartupProbe] " + msg);
		} catch (Throwable ignored) {
			// ignore
		}
	}

	public static void markAppClassLoaded() {
		appClassLoadedMs = BenchUtil.nowMs();
		log("markAppClassLoaded: " + getSummary());
	}

	public static void markUiFirstScreenShown() {
		if (uiFirstScreenShownMs == 0) {
			uiFirstScreenShownMs = BenchUtil.nowMs();
			log("markUiFirstScreenShown: " + getSummary());
		}
	}

	public static long getAppClassLoadedMs() {
		return appClassLoadedMs;
	}

	public static long getUiFirstScreenShownMs() {
		return uiFirstScreenShownMs;
	}

	public static String getSummary() {
		long now = BenchUtil.nowMs();
		return "appLoadedMs=" + appClassLoadedMs + " uiShownMs=" + uiFirstScreenShownMs + " nowMs=" + now;
	}
}
