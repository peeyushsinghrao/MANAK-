# TECHNICAL REQUIREMENTS DOCUMENT (TRD)

**Project:** SMART-IV MONITOR  
**Document:** 02_TRD.md  
**Version:** 0.2 (Engineering Baseline)  
**Status:** DRAFT / PROTOTYPE SPECIFICATION  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Technical Objective
To specify the complete set of verifiable engineering, electrical, optical, wireless, and software requirements necessary to construct, flash, and validate the SMART-IV MONITOR prototype.

---

## 2. Requirement Classification Legend
- **Priority:**
  - `CRITICAL`: Mandatory for functional safety, basic operation, or prototype integrity.
  - `HIGH`: Core operational requirement for the MVP demonstration.
  - `MEDIUM`: Important for user experience, calibration, and power efficiency.
  - `LOW`: Nice-to-have or future extension item.

---

## 3. System & Hardware Requirements

| Requirement ID | Specification Statement | Priority | Technical Rationale | Verification Method |
| :--- | :--- | :--- | :--- | :--- |
| **REQ-HW-001** | The processing core shall be an Espressif ESP32-WROOM-32 or ESP32-S3 module running at $\le 160\text{ MHz}$ or ESP32-CAM with $\ge 4\text{ MB}$ PSRAM. | `CRITICAL` | Provides integrated BLE 4.2/5.0 radio, hardware timers, and memory headroom for optical frame buffer processing. | Board inspection and firmware boot log verification. |
| **REQ-HW-002** | The input voltage supply shall accept regulated $5.0\text{V} \pm 0.25\text{V}$ via USB-C or a $3.7\text{V}$ nominal Li-ion cell stepped down to $3.3\text{V}$ via an LDO (e.g., AMS1117-3.3 or ME6211). | `CRITICAL` | Prevents core brownout resets during peak RF transmission and camera sensor activation. | Digital Multimeter (DMM) and oscilloscope measurement during RF transmit bursts. |
| **REQ-HW-003** | The power rail shall include at least $470\,\mu\text{F}$ bulk capacitance across the 3.3V and GND rails near the ESP32 module. | `HIGH` | Mitigates ESP32 known RF current surge transients (up to $320\text{ mA}$ peak). | Visual schematic audit and oscilloscope transient probe under load. |
| **REQ-HW-004** | A physical momentary push button shall be connected to a hardware interrupt pin (pulled HIGH with debouncing) for local alarm acknowledgment/silencing. | `HIGH` | Allows immediate physical mute by caregiver without smartphone interaction. | Oscilloscope button bounce test and firmware interrupt state transition logging. |
| **REQ-HW-005** | An active magnetic buzzer (operating at $3.3\text{V} / 5\text{V}$, resonant frequency $2.4\text{ kHz} \pm 300\text{ Hz}$) driven by an NPN transistor (e.g., 2N2222 / SS8050) shall provide local acoustic output $\ge 75\text{ dBA}$ at $10\text{ cm}$. | `HIGH` | Guarantees audible alert even if the smartphone BLE connection is dropped. | Sound level meter (dB meter) test in quiet room at $10\text{ cm}$ distance. |
| **REQ-HW-006** | A dual-LED indicator (Green = Normal/Monitoring, Red = Alert/Fault) shall be provided with current-limiting series resistors ($220\,\Omega$ to $330\,\Omega$). | `MEDIUM` | Provides instantaneous bedside visual status indication. | Visual inspection and forward current measurement ($< 15\text{ mA}$). |
| **REQ-HW-007** | A mounting bracket shall rigidly maintain the optical sensor axis orthogonal ($90^\circ \pm 3^\circ$) to the vertical axis of the mock container at a fixed distance of $100\text{ mm} \pm 15\text{ mm}$. | `HIGH` | Eliminates parallax errors and geometric distortion in fluid edge detection. | Caliper measurement and test rig alignment verification. |

---

## 4. Optical & Level Detection Requirements

| Requirement ID | Specification Statement | Priority | Technical Rationale | Verification Method |
| :--- | :--- | :--- | :--- | :--- |
| **REQ-OPT-001** | The optical sensor (OV2640 on ESP32-CAM) shall capture frames in QQVGA ($160\times 120$) or QVGA ($320\times 240$) grayscale format. | `CRITICAL` | Minimizes memory footprint, eliminates Bayer color demosaicing overhead, and accelerates 1D column scanning. | Firmware buffer size query and memory profiling. |
| **REQ-OPT-002** | The level detection algorithm shall compute a vertical 1D intensity gradient profile $\frac{\partial I}{\partial y}$ across a designated Region of Interest (ROI) column of $\ge 20$ pixels width. | `HIGH` | Detects the steep refractive index transition (meniscus boundary) between air and liquid. | Static image validation against mock fluid levels with plotted gradient output. |
| **REQ-OPT-003** | The system shall employ a 3-sample moving window confirmation filter before declaring a state transition from `NORMAL` to `THRESHOLD_REACHED`. | `HIGH` | Eliminates false-positive triggers caused by transient liquid sloshing or mechanical bumps. | Step-drain test with intentional vibration perturbations. |
| **REQ-OPT-004** | An auxiliary constant-intensity diffuse LED illuminator (wavelength $6000\text{K}$ white or $850\text{ nm}$ near-IR) shall illuminate the container from behind or beside to maintain contrast $\ge 30\text{ dB}$ across ambient light variations from $50\text{ lux}$ to $500\text{ lux}$. | `HIGH` | Prevents ambient ward lighting fluctuations (daylight, shadows) from corrupting meniscus edge detection. | Lux meter calibration across darkroom, fluorescent, and indirect daylight conditions. |
| **REQ-OPT-005** | The fluid level threshold shall be software-configurable from $5\%$ to $50\%$ of container volume with a resolution of $\le 5\%$. | `MEDIUM` | Allows caregiver to set early warning appropriate for the infusion flow rate. | Threshold setting test via BLE command and validation against graduated marks. |

---

## 5. Wireless & BLE Communication Requirements

| Requirement ID | Specification Statement | Priority | Technical Rationale | Verification Method |
| :--- | :--- | :--- | :--- | :--- |
| **REQ-BLE-001** | The ESP32 shall operate as a BLE Peripheral (Server) advertising as `SMART-IV-XXXX` (where `XXXX` is the last 4 characters of the MAC address). | `CRITICAL` | Facilitates unambiguous mobile device discovery and pairing in multi-device environments. | BLE scanner tool (e.g., nRF Connect) advertising packet inspection. |
| **REQ-BLE-002** | The BLE GATT Server shall expose a custom Primary Service with at least three Characteristics: Telemetry (Notify), Command/Control (Write/Read), and Calibration (Write/Read). | `CRITICAL` | Standardizes data exchange for telemetry, state control, and calibration parameters. | GATT profile discovery and attribute handle verification using standard BLE tools. |
| **REQ-BLE-003** | Telemetry packets shall be structured as compact binary byte arrays ($\le 12\text{ bytes}$) with a leading OpCode, Sequence ID, Level Integer ($0\text{--}100\%$), State Flags, Battery Voltage ($mV$), and CRC-8 checksum. | `HIGH` | Minimizes BLE packet fragmentation, reduces MTU negotiation dependencies, and conserves radio energy. | Byte stream capture and packet decoder verification. |
| **REQ-BLE-004** | The advertising interval shall be set to $200\text{ ms} \pm 20\text{ ms}$ in unpaired state, and connection interval shall be negotiated to $100\text{ ms} \pm 25\text{ ms}$ upon pairing. | `MEDIUM` | Balances rapid connection establishment with low standby power drain. | Radio packet sniffer timing log analysis. |
| **REQ-BLE-005** | The BLE stack shall emit a `HEARTBEAT` update every $2000\text{ ms} \pm 200\text{ ms}$. If the mobile client detects $>3$ missed heartbeats ($>6\text{ s}$), it shall assert a `LINK_LOST` warning. | `HIGH` | Ensures immediate notification if the caregiver moves out of wireless coverage. | Range walk-away disconnect test and app warning latency timer. |

---

## 6. Mobile Application Requirements

| Requirement ID | Specification Statement | Priority | Technical Rationale | Verification Method |
| :--- | :--- | :--- | :--- | :--- |
| **REQ-APP-001** | The mobile application shall be built using Flutter / Jetpack Compose with Android target SDK $\ge 34$ and minimum SDK $24$ (Android 7.0+). | `HIGH` | Guarantees broad device compatibility across affordable Android smartphones common in Indian clinics. | APK installation and testing on Android 8.0, 11.0, and 14.0 test devices. |
| **REQ-APP-002** | The app shall request runtime permissions for `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, and `POST_NOTIFICATIONS` with user-friendly explanatory dialogs. | `CRITICAL` | Complies strictly with Android 12+ (API 31+) fine-grained security policies. | Fresh install permission flow audit on clean Android 13/14 device. |
| **REQ-APP-003** | The app UI shall render a single-screen dashboard displaying: Connection Status, Visual Fluid Level Gauge (0–100%), Active Threshold Line, Battery Level, and Mute/Acknowledge Button. | `HIGH` | Provides clean, high-contrast, uncluttered situational awareness at a glance. | UI usability walkthrough and test tag accessibility audit. |
| **REQ-APP-004** | Upon receiving the `ALERT_THRESHOLD_REACHED` event, the mobile app shall trigger full-screen alarm audio using the Android `NotificationManager` with `NotificationCompat.CATEGORY_ALARM` and high-priority channel. | `CRITICAL` | Guarantees the alarm sounds even when the phone screen is locked or app is in background. | Lock-screen background alert trigger trial. |
| **REQ-APP-005** | The app shall store calibration offsets locally using persistent key-value storage (SharedPreferences / Hive / DataStore). | `MEDIUM` | Preserves calibration data across app restarts. | App kill-and-restart regression test. |

---

## 7. Power & Environmental Requirements

| Requirement ID | Specification Statement | Priority | Technical Rationale | Verification Method |
| :--- | :--- | :--- | :--- | :--- |
| **REQ-PWR-001** | Average steady-state current consumption in active monitoring mode shall not exceed $160\text{ mA}$ at $5.0\text{V}$ ($800\text{ mW}$). | `HIGH` | Enables $>12\text{ hours}$ continuous operation from a standard $3000\text{ mAh}$ 5V power bank. | USB power meter and inline current probe measurement. |
| **REQ-PWR-002** | The firmware shall support an optional duty-cycled sleep mode (wake, capture frame, evaluate, sleep $1.8\text{ s}$) dropping average current to $\le 60\text{ mA}$. | `MEDIUM` | Drastically extends battery life for battery-operated field deployment. | Current waveform logging during duty cycle execution. |
| **REQ-ENV-001** | The prototype enclosure and optical bracket shall operate reliably across ambient temperatures of $+15^\circ\text{C}$ to $+40^\circ\text{C}$ and relative humidity $20\%\text{--}80\%$ non-condensing. | `MEDIUM` | Covers typical unconditioned ward climates in tropical zones. | Environmental thermal chamber or controlled room temperature validation. |

---

## 8. Safety & Compliance Constraints

| Requirement ID | Specification Statement | Priority | Technical Rationale | Verification Method |
| :--- | :--- | :--- | :--- | :--- |
| **REQ-SAF-001** | The device shall have zero physical contact with the fluid column, drip chamber needle, or IV tubing. | `CRITICAL` | Preserves sterile boundary and prevents fluid contamination. | Physical inspection of mock setup clamp isolation. |
| **REQ-SAF-002** | The device housing and all mobile app screens shall prominently display the disclaimer: `"EDUCATIONAL DEMONSTRATION PROTOTYPE — NOT FOR CLINICAL USE"`. | `CRITICAL` | Prevents misidentification as medical equipment during public demonstrations. | Visual audit of enclosure sticker and app header layout. |
| **REQ-SAF-003** | The system shall never actuate mechanical valves, pinch clamps, or alter gravity hydrostatic head. | `CRITICAL` | Eliminates risk of inadvertent flow blockage or uncontrolled fluid bolus. | Mechanical inspection confirming strictly passive optical clamping. |

---

## 9. Open Technical Questions (TRD)

1. **OTQ-01:** What is the minimum optical contrast achievable across different generic plastic IV bottles (PVC vs. Polypropylene vs. Glass) without backlighting?  
   *Current Assumption:* Non-uniform translucent plastics require a dedicated diffuse LED backlight strip for consistent edge extraction.
2. **OTQ-02:** Can the OV2640 sensor on the AI-Thinker ESP32-CAM reliably focus at a distance of $8\text{--}10\text{ cm}$ without manual lens defocusing/adjustment?  
   *Current Assumption:* The stock M12/M7 lens is factory set to infinity and must be manually rotated $\sim 1/4$ turn counter-clockwise (breaking glue seal) to adjust close macro focus.
3. **OTQ-03:** Does Android OS background battery optimization kill BLE notify subscriptions after 15+ minutes on specific OEM ROMs (Xiaomi/Samsung)?  
   *Current Assumption:* Requires an ongoing Android Foreground Service with an active persistent status bar notification.
