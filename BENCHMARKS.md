# Benchmarks (MicroEJ Showcase)

This document describes the benchmark/self-test feature available in the MicroEJ Showcase app, how to run it on **real hardware**, and what each test measures.

## Where it lives in the code

- UI page: `src/main/java/com/example/microej/pages/BenchmarkPage.java`
- Harness:
  - `src/main/java/com/example/microej/bench/BenchRunner.java`
  - `src/main/java/com/example/microej/bench/BenchUtil.java`
  - `src/main/java/com/example/microej/bench/BenchResult.java`
  - `src/main/java/com/example/microej/bench/BenchMetrics.java`
  - `src/main/java/com/example/microej/bench/BenchMetricKeys.java`
- Suites (the actual tests): `src/main/java/com/example/microej/bench/suites/*.java`
- Startup timing probe: `src/main/java/com/example/microej/bench/StartupProbe.java`
- Native (device-only) integration API: `src/main/java/com/example/microej/NativeDriverIntegration.java`
- Native facade used by benchmark suites: `src/main/java/com/example/microej/bench/NativeDriverFacade.java`

## How to run

### Run from the UI

1. Open the app on the device.
2. Navigate to **Benchmarks**.
3. Press **Run All Benchmarks**.

The results are displayed on-screen and also printed to the console.

### Console logs you should see

The app prints several log lines to help you monitor a run on real hardware:

- Benchmark run banner (from `BenchmarkPage`):
  - `[Bench] RunAll start (env=DEVICE)`
- Per-case execution logs (from `BenchRunner`):
  - `[BenchRun] START suite='...' case='...'`
  - `[BenchRun] END   suite='...' case='...' status=PASS|FAIL|SKIP durMs=... details='...'`
- Startup probe marks (from `StartupProbe`):
  - `[StartupProbe] markAppClassLoaded: ...`
  - `[StartupProbe] markUiFirstScreenShown: ...`
- Native/hardware access logs (from `NativeDriverFacade`) when native calls fail:
  - `[BenchNative] getDriverVersion failed: ...`
  - `[BenchNative] getMcuTempCentiCelsius failed: ...`

If the **UI doesn’t show** the page for any reason, these logs are still enough to confirm whether benchmarks are being executed.

## Understanding benchmark results

Each test returns a `BenchResult` with:

- `status`: `PASS`, `FAIL`, or `SKIP`
- `durationMs`: wall-clock duration in milliseconds
- `details`: human-readable extra information (optional)
- `metrics`: optional key/value pairs for more structured values

### PASS vs FAIL vs SKIP

- **PASS**: The test executed and its checks succeeded.
- **FAIL**: The test executed and detected an error or threw an exception.
- **SKIP**: The test is not applicable on this target (common on Simulator) or requires manual/external instrumentation.

`SKIP` is used intentionally for board-specific items like *power measurement* or *communication throughput*, so the benchmark page remains safe and portable.

## Key performance areas covered

The benchmark page is designed around these areas:

1. **Startup time**
2. **Memory usage**
3. **CPU/compute throughput**
4. **UI/graphics readiness** (basic display checks)
5. **Java execution speed**
6. **Communication performance** (placeholder by default)
7. **Power consumption** (placeholder by default)

The sections below describe each suite and how to interpret it.

---

## Suite: Startup (`StartupBenchSuite`)

**Goal:** quantify app startup milestones.

### What it measures

This suite uses `StartupProbe`, which records timestamps:

- `markAppClassLoaded()` is called very early (in `Main` static initializer).
- `markUiFirstScreenShown()` is called right after the first `Display.requestShow(...)` (in `DemoApp.showMenu()` and also `DemoApp.showPage(...)` as a fallback).

The benchmark then derives durations like:

- **JVM/Classload → app loaded**
- **app loaded → first UI requested**

### How to measure on a real device

1. Flash and boot the firmware.
2. Observe logs:
   - `[StartupProbe] markAppClassLoaded: ...`
   - `[StartupProbe] markUiFirstScreenShown: ...`
3. Run the benchmark page.

### Notes / caveats

- This is “time as seen by the VM”, not pure power-on time.
- To include *bootloader + RTOS + VEE startup*, you’d need a hardware timestamp at reset and a bridge into Java. This suite is still useful for comparing Java/app changes.

**Metric keys:** `startup.ms` (and/or details string depending on suite code).

---

## Suite: Device info (`DeviceInfoBenchSuite`)

**Goal:** ensure basic device identification APIs work and provide useful context.

### What it checks

Typical checks:

- Target architecture / device id availability
- Quick sanity that you’re running on expected target

### How to interpret

- **PASS** means the platform exposes these values.
- **FAIL** may indicate missing foundation libs or a platform integration issue.

---

## Suite: Memory (`MemoryBenchSuite`)

**Goal:** snapshot the Java heap state.

### What it measures

Values commonly derived from `Runtime`:

- used heap bytes
- free heap bytes
- total heap bytes

This doesn’t include all native memory (framebuffers, stacks, DMA buffers) unless your platform exposes that separately.

### How to measure on a real device

- Run the benchmark multiple times:
  - once just after startup
  - once after navigating several pages
  - once after heavy activities (network, graphics)

This helps detect leaks or unusually high transient allocations.

**Metric keys:**
- `mem.used.bytes`
- `mem.free.bytes`
- `mem.total.bytes`

---

## Suite: CPU throughput (`CpuBenchSuite`)

**Goal:** provide a simple, comparable compute-throughput score.

### What it measures

A tight integer arithmetic loop runs for a fixed time window (example: 500 ms). It records:

- number of loop iterations
- elapsed time
- derived throughput: **operations per second**

This is **not CPU%**. It’s a throughput indicator for *this specific workload*.

### How to measure on a real device

1. Ensure the device is in a stable state (avoid heavy background tasks).
2. Run benchmarks.
3. Read the result line for CPU suite, look for `opsPerSec=...`.

### Tips for stable numbers

- Run 5–10 times and keep the median.
- Minimize log spam while measuring.
- Keep CPU frequency/power mode constant.

**Metric keys:** `cpu.opsPerSec`

---

## Suite: Java execution speed (`JavaPerfBenchSuite`)

**Goal:** measure common Java workloads.

### What it measures

Typical micro-tests:

- Math loops
- String processing
- Parsing / data handling

The suite reports a throughput value (often `opsPerSec`) that helps compare VM settings or code changes.

**Metric keys:** `java.opsPerSec`

---

## Suite: Communication (`CommBenchSuite`) (default: placeholder)

**Goal:** measure latency/throughput for a real communication channel.

### Default behavior

The current implementation returns `SKIP` with a message like:

- `No comm target configured in this build`

### How to implement “for real device”

You have a few options depending on your board and foundations included:

- **HTTP/HTTPS latency**
  - Use your existing `NetworkPage` path.
  - Measure DNS + TCP connect + TLS handshake + first byte.
- **TCP echo throughput**
  - Connect to a known echo server and measure bytes/sec.
- **UART/SPI/I2C loopback**
  - Requires board wiring and a HAL/driver API.

**Suggested metrics:**
- `comm.latency.ms`
- `comm.bytesPerSec`

---

## Suite: Display hardware (`DisplayHardwareBenchSuite`)

**Goal:** verify display APIs + native driver connectivity.

### Test: `Display.getDisplay basics`

- Reads width / height / pixel depth.
- Validates they’re non-zero.

This ensures MicroUI display is initialized and responsive.

### Test: `Native driver version reachable`

- Calls `NativeDriverFacade.getDriverVersion()`.
- On real device, that calls `NativeDriverIntegration.getDriverVersion()` (SNI).

Interpretation:

- **PASS**: native integration is present and returned a non-negative version.
- **SKIP**: native integration isn’t available on this build/target.
- **FAIL**: unexpected exception / platform error.

**Suggested metric:** `native.driver.version`

---

## Suite: Touch hardware (`TouchHardwareBenchSuite`) (default: manual)

**Goal:** validate touch pipeline.

### Default behavior

This suite returns `SKIP` because full touch validation is interactive:

- You typically confirm touch values update on the Touch page.

### How to implement “for real device”

If you want an automated measurement (latency), you need either:

- a driver hook that timestamps the touch interrupt and provides it to Java, or
- external measurement (high-speed camera, logic analyzer)

---

## Suite: MCU Temperature (`McuTempBenchSuite`)

**Goal:** validate a real “SNI → BSP” hardware read.

### What it measures

- Calls `NativeDriverFacade.getMcuTempCentiCelsius()`.
- On real device, that calls the native function `NativeDriverIntegration.getMcuTempCentiCelsius()`.

Interpretation:

- **PASS**: got a valid temperature, sanity-checked.
- **SKIP**: temperature not supported or native integration missing.
- **FAIL**: unexpected exception.

**Metric keys:** `mcu.temp.c`

---

## Suite: File system (`FileSystemBenchSuite`) (default: simulated)

**Goal:** keep a safe placeholder until a real filesystem foundation is configured.

### Default behavior

The current suite only validates basic string/time operations (no real I/O). It’s intentionally safe on any target.

### How to implement “for real device”

To do actual file I/O you’ll need a filesystem foundation available in your build (and a configured FS in the VEE Port), then implement:

- write N bytes → measure ms
- read N bytes → measure ms
- list directory entries → measure ms

---

## Suite: Power (`PowerBenchSuite`) (default: placeholder)

**Goal:** document where power testing belongs.

### Default behavior

Returns `SKIP`:

- `Requires external power meter or BSP integration`

### How to implement “for real device”

Power measurement generally requires either:

- external measurement equipment (preferred), or
- a board-specific power monitor chip integrated into firmware.

A typical approach:

- put device into an idle/sleep state for X seconds
- measure average current using external meter
- log value and store as a metric

**Suggested metric keys:**
- `power.note` (already present)
- `power.ua.avg` (if you add it)

---

## Suite: Network Testing (`CommBenchSuite`)

**Goal:** measure real TCP connectivity, latency, and simple throughput on a real device.

### What it measures

This suite opens a TCP socket to an **echo server** (server must send back exactly the bytes it receives), then measures:

- **connectMs**: TCP connect time
- **avgRttMs**: average round-trip time for a payload
- **bytesPerSec**: transfer rate for the test payload (one direction payload size)

### Configuration (required)

This test is configured through Java system properties:

- `bench.net.host` (required): echo server hostname or IP
- `bench.net.port` (required): echo server port
- `bench.net.bytes` (optional, default `1024`): payload size per iteration
- `bench.net.iterations` (optional, default `5`): number of ping-pong iterations
- `bench.net.timeoutMs` (optional, default `3000`): connect + read timeout

If `bench.net.host` / `bench.net.port` are not set, the test returns **SKIP**.

### Console logs

During execution you’ll see:

- `[NetBench] Config host=... port=... bytes=... iterations=... timeoutMs=...`
- `[NetBench] iter 1/5 rttMs=...`

### Metrics

- `comm.host`
- `comm.port`
- `comm.connect.ms`
- `comm.latency.ms`
- `comm.bytes`
- `comm.bytesPerSec`

### Notes / tips (real device)

- Use a server on the same LAN for repeatable results.
- Run multiple times and take the median.
- If you see `network not initialized` or timeouts, verify Wi-Fi/Ethernet is up *before* running the bench and that the echo server is reachable.

---

## Recommended workflow for real-device benchmarking

1. **Boot** the device and wait for the system to stabilize.
2. Run benchmarks **3–10 times** to check variance.
3. If you change one thing (VM config, display settings, networking), re-run and compare:
   - `startup.ms`
   - `mem.used.bytes`
   - `cpu.opsPerSec`
   - `java.opsPerSec`

## Extending the benchmark page

If you want, we can extend benchmark coverage to match your target list more closely:

- UI FPS benchmark: render loop for N frames → compute FPS
- Network/TLS benchmark: DNS/TCP/TLS/HTTP timings (very useful for your Network page issues)
- Real filesystem benchmark (requires FS foundation + mount)
- BLE / Wi-Fi throughput (requires stack + test endpoint)
