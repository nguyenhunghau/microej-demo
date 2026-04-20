package com.example.microej;

public class NativeDeviceInfo {

    private NativeDeviceInfo() {
    }

    // SOAR/SNI constraint: native methods must return base types only.
    // We return small integer codes and convert them to Strings in Java.
//    private static native int nGetFileEncodingCode();
//    private static native int nGetRuntimeVersionCode();
//    private static native int nGetOSNameCode();

    public static String getFileEncoding() {
        try {
            int code = 1; // nGetFileEncodingCode();
            switch (code) {
                case 1:
                    return "UTF-8";
                default:
                    return "UTF-8";
            }
        } catch (UnsatisfiedLinkError e) {
            return "UTF-8";
        }
    }

    public static String getRuntimeVersion() {
        try {
            int code = 1; //nGetRuntimeVersionCode();
            // For now, just expose the VEE/port version as a simple code.
            // If you need the full VEE_VERSION string, we can expose it through a System property instead.
            switch (code) {
                case 0:
                    return "Unknown Runtime Version";
                default:
                    return "VEE runtime (code=" + code + ")";
            }
        } catch (UnsatisfiedLinkError e) {
            return "Unknown Runtime Version";
        }
    }

    public static String getOSName() {
        try {
            int code = 1; //nGetOSNameCode();
            switch (code) {
                case 1:
                    return "FreeRTOS";
                default:
                    return "Unknown OS";
            }
        } catch (UnsatisfiedLinkError e) {
            return "Unknown OS";
        }
    }
}
