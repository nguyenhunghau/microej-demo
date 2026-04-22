package com.example.microej.bench.suites;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.microej.bench.*;

/**
 * Network performance suite (real device).
 * <p>
 * This suite measures basic TCP connectivity and round-trip throughput against a configurable echo server.
 * <p>
 * Configuration is done using Java system properties (recommended so you don't have to recompile):
 * <ul>
 *   <li><b>bench.net.host</b> (String, required) - server hostname or IP</li>
 *   <li><b>bench.net.port</b> (int, required) - server port (echo server)</li>
 *   <li><b>bench.net.bytes</b> (int, optional, default 1024) - payload size per iteration</li>
 *   <li><b>bench.net.iterations</b> (int, optional, default 5) - number of ping-pong iterations</li>
 *   <li><b>bench.net.timeoutMs</b> (int, optional, default 3000) - connect/read timeout</li>
 * </ul>
 * If host/port are not configured, the case returns SKIP.
 */
public class NetworkBenchSuite implements BenchSuite {

	// Defaults can be overridden using -Dbench.net.* system properties.
	// IMPORTANT: host must be a hostname/IP only (no "https://" and no path).
	private static final String DEFAULT_HOST = "pautomationpractice.com/index.php";
	private static final int DEFAULT_PORT = 80;
	private static final int DEFAULT_BYTES = 1024;
	private static final int DEFAULT_ITERATIONS = 5;
	private static final int DEFAULT_TIMEOUT_MS = 3000;

	@Override
	public String getName() {
		return "Network Testing";
	}

	@Override
	public BenchCase[] getCases() {
		return new BenchCase[] {
			new BenchCase() {
				@Override public String getName() { return "TCP echo latency/throughput"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();

					String host = sanitizeHost(getProp("bench.net.host", DEFAULT_HOST));
					int port = DEFAULT_PORT ; //getIntProp("bench.net.port", DEFAULT_PORT, 1, 65535);
					int bytes = getIntProp("bench.net.bytes", DEFAULT_BYTES, 1, 1024 * 1024);
					int iterations = getIntProp("bench.net.iterations", DEFAULT_ITERATIONS, 1, 100000);
					int timeoutMs = getIntProp("bench.net.timeoutMs", DEFAULT_TIMEOUT_MS, 100, 120000);

					log("Config host=" + host + " port=" + port + " bytes=" + bytes + " iterations=" + iterations + " timeoutMs=" + timeoutMs);
					if (host == null || host.length() == 0) {
						return BenchUtil.skip(getName(), start, "Missing host. Set -Dbench.net.host=<echo-server-host>");
					}
					if (looksLikeTlsWebEndpoint(host, port)) {
						return BenchUtil.skip(getName(), start,
								"Configured endpoint looks like HTTPS/TLS (" + host + ":" + port + "), not TCP echo. "
										+ "Use an echo server, e.g. -Dbench.net.host=<host> -Dbench.net.port=<echo-port>.");
					}

					Socket socket = null;
					try {
						socket = new Socket();
						long t0 = BenchUtil.nowMs();
						socket.connect(new InetSocketAddress(host, port), timeoutMs);
						long connectMs = Math.max(0, BenchUtil.nowMs() - t0);
						socket.setSoTimeout(timeoutMs);

						InputStream in = socket.getInputStream();
						OutputStream out = socket.getOutputStream();

						byte[] payload = new byte[bytes];
						for (int i = 0; i < payload.length; i++) {
							payload[i] = (byte) (i ^ 0x5A);
						}
						byte[] rx = new byte[bytes];

						long rttSumMs = 0;
						long xferStart = BenchUtil.nowMs();
						for (int i = 0; i < iterations; i++) {
							long it0 = BenchUtil.nowMs();
							out.write(payload);
							out.flush();
							readFully(in, rx, 0, bytes);
							long rtt = Math.max(0, BenchUtil.nowMs() - it0);
							rttSumMs += rtt;
							log("iter " + (i + 1) + "/" + iterations + " rttMs=" + rtt);
						}
						long xferDurMs = Math.max(1, BenchUtil.nowMs() - xferStart);

						long totalBytes = (long) bytes * (long) iterations; // one direction payload size
						double avgRttMs = (double) rttSumMs / (double) iterations;
						double bytesPerSec = ((double) totalBytes * 1000.0) / (double) xferDurMs;

						BenchResult r = BenchUtil.pass(getName(), start,
								"connectMs=" + connectMs + " avgRttMs=" + fmt1(avgRttMs) + " bytesPerSec=" + fmt1(bytesPerSec));
						r.metrics = BenchMetrics.of(
								BenchMetricKeys.COMM_HOST, host,
								BenchMetricKeys.COMM_PORT, String.valueOf(port),
								BenchMetricKeys.COMM_CONNECT_MS, String.valueOf(connectMs),
								BenchMetricKeys.COMM_LATENCY_MS, String.valueOf(avgRttMs),
								BenchMetricKeys.COMM_BYTES, String.valueOf(totalBytes),
								BenchMetricKeys.COMM_BYTES_PER_SEC, String.valueOf(bytesPerSec)
						);
						return r;
					} catch (Throwable t) {
						log("ERROR: " + t);
						return BenchUtil.fail(getName(), start, t);
					} finally {
						try {
							if (socket != null) socket.close();
						} catch (Throwable ignored) {
							// ignore
						}
					}
				}
			},
			new BenchCase() {
				@Override public String getName() { return "TCP connect only"; }
				@Override public BenchResult run() {
					long start = BenchUtil.nowMs();

					String host = sanitizeHost(getProp("bench.net.host", DEFAULT_HOST));
					int port = getIntProp("bench.net.port", DEFAULT_PORT, 1, 65535);
					int iterations = getIntProp("bench.net.iterations", DEFAULT_ITERATIONS, 1, 100000);
					int timeoutMs = getIntProp("bench.net.timeoutMs", DEFAULT_TIMEOUT_MS, 100, 120000);

					log("Connect-only config host=" + host + " port=" + port + " iterations=" + iterations + " timeoutMs=" + timeoutMs);
					if (host == null || host.length() == 0) {
						return BenchUtil.skip(getName(), start, "Missing host. Set -Dbench.net.host=<host>");
					}

					long sumConnectMs = 0;
					long minConnectMs = Long.MAX_VALUE;
					long maxConnectMs = 0;

					for (int i = 0; i < iterations; i++) {
						Socket socket = null;
						try {
							socket = new Socket();
							long t0 = BenchUtil.nowMs();
							socket.connect(new InetSocketAddress(host, port), timeoutMs);
							long connectMs = Math.max(0, BenchUtil.nowMs() - t0);
							sumConnectMs += connectMs;
							if (connectMs < minConnectMs) minConnectMs = connectMs;
							if (connectMs > maxConnectMs) maxConnectMs = connectMs;
							log("connect iter " + (i + 1) + "/" + iterations + " connectMs=" + connectMs);
						} catch (Throwable t) {
							log("ERROR (connect-only): " + t);
							return BenchUtil.fail(getName(), start, t);
						} finally {
							try {
								if (socket != null) socket.close();
							} catch (Throwable ignored) {
								// ignore
							}
						}
					}

					double avgConnectMs = (double) sumConnectMs / (double) iterations;
					BenchResult r = BenchUtil.pass(getName(), start,
							"avgConnectMs=" + fmt1(avgConnectMs) + " minConnectMs=" + minConnectMs + " maxConnectMs=" + maxConnectMs);
					r.metrics = BenchMetrics.of(
							BenchMetricKeys.COMM_HOST, host,
							BenchMetricKeys.COMM_PORT, String.valueOf(port),
							BenchMetricKeys.COMM_CONNECT_MS, String.valueOf(avgConnectMs)
					);
					return r;
				}
			}
		};
	}

	private static void readFully(InputStream in, byte[] buf, int off, int len) throws java.io.IOException {
		int read = 0;
		while (read < len) {
			int r = in.read(buf, off + read, len - read);
			if (r < 0) {
				throw new java.io.IOException("EOF while reading echo response");
			}
			read += r;
		}
	}

	/**
	 * Accepts a hostname or a full URL and returns a hostname suitable for InetSocketAddress.
	 */
	private static String sanitizeHost(String hostOrUrl) {
		if (hostOrUrl == null) {
			return null;
		}
		String h = hostOrUrl.trim();
		// Strip scheme if present.
		int scheme = h.indexOf("://");
		if (scheme >= 0) {
			h = h.substring(scheme + 3);
		}
		// Strip path.
		int slash = h.indexOf('/');
		if (slash >= 0) {
			h = h.substring(0, slash);
		}
		// Strip credentials if any.
		int at = h.lastIndexOf('@');
		if (at >= 0) {
			h = h.substring(at + 1);
		}
		// Strip port if any.
		int colon = h.lastIndexOf(':');
		if (colon >= 0) {
			// If it's an IPv6 literal, it contains multiple ':'; we don't support full IPv6 parsing here.
			// For now, only strip a single ":port" suffix for typical hostnames.
			String after = h.substring(colon + 1);
			boolean digits = after.length() > 0;
			for (int i = 0; i < after.length(); i++) {
				char c = after.charAt(i);
				if (c < '0' || c > '9') {
					digits = false;
					break;
				}
			}
			if (digits) {
				h = h.substring(0, colon);
			}
		}
		return h;
	}

	private static int clamp(int v, int min, int max) {
		return (v < min) ? min : (v > max) ? max : v;
	}

	private static String getProp(String key, String def) {
		try {
			String v = System.getProperty(key);
			return (v == null || v.trim().length() == 0) ? def : v.trim();
		} catch (Throwable ignored) {
			return def;
		}
	}

	private static int getIntProp(String key, int def, int min, int max) {
		try {
			String raw = System.getProperty(key);
			if (raw == null || raw.trim().length() == 0) {
				return clamp(def, min, max);
			}
			return clamp(Integer.parseInt(raw.trim()), min, max);
		} catch (Throwable ignored) {
			return clamp(def, min, max);
		}
	}

	private static boolean looksLikeTlsWebEndpoint(String host, int port) {
		if (port == 443 || port == 8443) {
			return true;
		}
		String h = host == null ? "" : host.toLowerCase();
		return h.startsWith("www.");
	}

	private static String fmt1(double v) {
		// Avoid depending on Locale.
		long iv = (long) (v * 10.0);
		return String.valueOf(iv / 10) + "." + Math.abs(iv % 10);
	}

	private static void log(String msg) {
		try {
			System.out.println("[NetBench] " + msg);
		} catch (Throwable ignored) {
			// ignore
		}
	}
}
