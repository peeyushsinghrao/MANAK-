# PRODUCT REQUIREMENTS DOCUMENT (PRD)

**Project:** SMART-IV MONITOR  
**Document:** 01_PRD.md  
**Version:** 0.2 (Architecture Baseline)  
**Status:** DRAFT / PROTOTYPE DEFINITION  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Product Name & Metadata
- **Project Title:** SMART-IV MONITOR: External Non-Invasive Fluid Level Alert Prototype for Hanging Drip Containers
- **Target Category:** School / INSPIRE-MANAK Science & Technology Prototype (Educational / Proof of Concept)
- **Primary Hardware Baseline:** Espressif ESP32-CAM (AI-Thinker module) / Standard ESP32 DevKit V1
- **Companion Software:** Mobile Monitoring Application (Bluetooth Low Energy / BLE)

---

## 2. Product Status
- **Current Lifecycle Stage:** Phase 0–1 (Architecture, Specification, and Proof-of-Concept Bench Prototype)
- **Maturity Level:** Version 0.2 — Concept and Architecture Specification. No clinical or commercial certification claimed.

---

## 3. Project Purpose
The SMART-IV MONITOR project investigates whether a low-cost, externally mounted, non-contact optical/visual sensing apparatus paired with an ESP32 microcontroller can accurately identify the descending approximate fluid boundary of a mock IV/drip container and dispatch local audio-visual and wireless (BLE) notifications to a caregiver's mobile terminal before the reservoir is fully depleted.

---

## 4. Problem Statement & Background Research
In resource-constrained secondary healthcare centers, community clinics, and general hospital wards across developing regions (e.g., rural Indian primary healthcare centers / PHCs):
1. **Nurse-to-Patient Ratio Disparity:** Caregivers often oversee multiple beds simultaneously, leading to periodic delays in manual visual checks of hanging gravity-fed infusion sets.
2. **Consequences of Depletion:** When a gravity IV bottle empties completely, blood can backflow into the IV cannula due to venous pressure (approx. 10–15 mmHg in peripheral veins) once hydrostatic pressure drops below intravascular pressure, causing cannula clotting, patient distress, and nursing overhead for recannulation. Additionally, fluid exhaustion disrupts continuous therapeutic delivery.
3. **Cost Barrier of Commercial Infusion Systems:** Electronic volumetric infusion pumps and automated smart drip stands cost between ₹40,000 and ₹1,50,000+ per unit, rendering them inaccessible for routine non-critical gravity infusions in underfunded clinics.

---

## 5. Target Users & Stakeholders
- **Primary Evaluator:** INSPIRE-MANAK Reviewers, Science Exhibition Judges, Secondary School Technical Mentors.
- **Simulated End-User:** Ward Nurses, Attendants, and Caregivers in general wards overseeing multiple gravity drip setups.
- **Secondary Stakeholder:** Clinic biomedical maintenance technicians (evaluating low-cost servicing and battery autonomy).

---

## 6. Use Scenarios

### 6.1 Primary Demonstration Scenario (Bench / Mock Setup)
1. A standard 500 mL semi-transparent plastic mock infusion bottle filled with water (or tinted non-toxic liquid) is suspended on a demonstration stand.
2. The SMART-IV MONITOR sensing module is clipped/bracketed externally onto the stand facing the container's graduation markings at a fixed focal distance (approx. 8–12 cm).
3. The demonstrator sets a low-level threshold (e.g., "Alert when fluid reaches ~15% / ~75 mL remaining") via the companion mobile application over BLE.
4. Fluid is drained through a standard gravity flow clamp into a waste vessel.
5. As the fluid meniscus passes the configured threshold mark, the ESP32 activates a local piezobuzzer/LED indicator and pushes a prioritized BLE alert packet to the smartphone app.
6. The app triggers a high-priority audible and vibration alert, updating the UI status from `MONITORING` to `THRESHOLD_REACHED`.

### 6.2 Secondary Scenario (Offline / Standalone Alert)
1. If the mobile device disconnects or is out of range, the ESP32 local audio-visual buzzer and status LED remain fully autonomous, sounding the alert locally without relying on smartphone connectivity.

---

## 7. Core Value Proposition
- **Non-Invasive / Zero Fluid Contact:** Mounts entirely outside the sterile pathway; zero risk of contaminating the fluid or altering tube hydraulic resistance.
- **Ultra-Low Bill of Materials (BOM):** Utilizes accessible hobbyist components (< ₹1,500 total prototype cost) rather than proprietary clinical sensors.
- **Decoupled Alert Architecture:** Simultaneous local (piezo/LED) and remote (BLE smartphone) notification pathways guarantee failsafe alert delivery.
- **Configurable Threshold:** Accommodates arbitrary user-defined warning levels rather than a single fixed hardwired binary switch.

---

## 8. Product Goals vs. Non-Goals

### 8.1 Product Goals (Strict Scope)
- [G-1] Accurately detect fluid level transition past a designated geometric region of interest (ROI) on a calibrated mock bottle under controlled indoor lighting.
- [G-2] Transmit telemetry (fluid state, battery voltage estimate, system health) over BLE to an Android/Flutter mobile client within 10 meters line-of-sight.
- [G-3] Provide an end-to-end alert latency of less than 3 seconds from meniscus passage to phone notification.
- [G-4] Operate autonomously for at least 4 continuous hours on a rechargeable single-cell Li-ion / 5V USB battery pack.
- [G-5] Provide a simple calibration routine for zero-level and threshold registration.

### 8.2 Non-Goals (Explicitly Excluded)
- [NG-1] **No Patient Connection:** The prototype must NEVER be connected to a living human or animal.
- [NG-2] **No Flow Rate Control:** The system does NOT actuate roller clamps or motorized pinch valves; it is strictly a passive observer/alert system.
- [NG-3] **No Micro-Droplet Counting in Drip Chamber:** This device monitors container bulk level, not optical drop drop-by-drop volumetric kinematics.
- [NG-4] **No Medical Certification:** It does not meet IEC 60601-1, IEC 60601-2-24 (infusion devices), or ISO 13485 standards.
- [NG-5] **No Clinical Efficacy Claims:** Makes no claim to prevent air embolism or physiological venous backflow under all hemodynamic variations.

---

## 9. System Boundaries & Critical Safety Rules
```
┌─────────────────────────────────────────────────────────────┐
│                      SAFE BENCH ZONE                        │
│                                                             │
│  [Mock IV Bottle] ──(External Optics)──> [ESP32 Sensor]     │
│         │                                        │          │
│         ▼                                        ▼ (BLE)    │
│  [Drain to Bucket]                         [Mobile App]     │
│                                                             │
│ ─────────────────────────────────────────────────────────── │
│                     PROHIBITED ZONE                         │
│  ✖ No Patient Cannula                                       │
│  ✖ No Blood Vessel Access                                   │
│  ✖ No Sterile Path Modification                             │
│  ✖ No Automated Medicine Dosing                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 10. MVP Scope vs. Future Roadmap

| Feature Dimension | Minimum Viable Prototype (MVP - v0.3–v0.5) | Future Version (v1.0+ / Academic Extension) |
| :--- | :--- | :--- |
| **Sensing Mechanism** | ESP32-CAM optical edge/intensity detection on calibrated ROI (or 1D optical array backup) | Dual-sensor fusion (Optical Meniscus + Micro Load Cell tare validation) |
| **Fluid Types** | Clear water / colored mock saline in standardized 500 mL semi-rigid bottle | Multi-fluid density compensation (lipids, albumin, glucose, opaque containers) |
| **Wireless Protocol**| BLE 4.2 / 5.0 Custom GATT Service (Unicast Point-to-Point) | BLE Mesh / ESP-NOW / MQTT over Wi-Fi multi-bed central nursing dashboard |
| **User Interface** | Single-screen Flutter Android App (Pairing, Calibrate, Level Meter, Alert) | Multi-bed ward visualizer with bed-ID grouping and hospital server bridge |
| **Power System** | 5V USB power bank or single 18650 cell with linear TP4056 charger | Ultra-low-power deep sleep duty-cycling with dynamic frame-rate adjustment |
| **Enclosure** | 3D-printed / laser-cut acrylic bracket clamped to demonstration IV pole | Medical-grade IP54 antimicrobial snap-on housing with toolless adjustment |

---

## 11. Functional Requirements (High Level)

- **FR-01: Continuous Level Sampling:** The hardware module shall sample the fluid reservoir boundary at least once every 2.0 seconds during active monitoring mode.
- **FR-02: Local Audio-Visual Alert:** Upon confirming that fluid level is $\le$ threshold for 3 consecutive sampling cycles, the onboard buzzer shall emit an intermittent 2.4 kHz chime ($\ge 75\text{ dB}$ at 10 cm) and illuminate a red alert LED.
- **FR-03: Wireless Telemetry Stream:** The ESP32 shall update its BLE Telemetry Characteristic with the current estimated level percentage, state flags, and timestamp delta every 2 seconds.
- **FR-04: Remote Alert Notification:** The mobile application shall trigger high-priority push notifications and localized alarm sounds when the `THRESHOLD_REACHED` BLE packet is acknowledged.
- **FR-05: Interactive Calibration:** The mobile app shall guide the user through a 2-step reference calibration: (a) Full Bottle Level, (b) Selected Warning Threshold Level.
- **FR-06: Manual Alarm Mute / Acknowledge:** The user shall be able to silence the alarm either via a physical onboard push button or via the mobile app's "Acknowledge" button.

---

## 12. Non-Functional Requirements

- **NFR-01 (Latency):** Alert notification on mobile app shall display within 3.0 seconds of physical threshold breach under nominal RF conditions.
- **NFR-02 (Power Autonomy):** Average active power consumption shall not exceed 180 mA at 3.3V (approx. 600 mW) during intermittent capture mode.
- **NFR-03 (Demonstration Repeatability):** The threshold trigger point shall demonstrate a repeatable geometric precision of within $\pm 10\text{ mL}$ (for a 500 mL container) across 10 consecutive mock drain cycles under fixed lighting.
- **NFR-04 (Cost Constraint):** Total discrete BOM cost for prototype electronic parts shall remain below ₹1,500 ($< \$18\text{ USD}$).
- **NFR-05 (User Simplicity):** Initial BLE pairing and calibration workflow shall require less than 60 seconds for an untrained student demonstrator.

---

## 13. User Personas & User Journey

### Persona: Sister Ananya (Simulated Ward Nurse)
- **Context:** Works night shifts in a 20-bed semi-urban clinic with frequent power cuts and high patient density.
- **Goal:** Receive a timely warning 10 minutes before an infusion ends so she can prepare the next bottle or saline flush without having to stand by the bed continuously.
- **Pain Point:** Constant worry about clotted cannulas or unexpected bag depletion when called away for an emergency in another room.

### Step-by-Step User Journey:
1. **Mounting:** Ananya hangs a fresh mock saline bottle on the IV stand and clips the SMART-IV module onto the stand neck.
2. **App Launch:** Opens the SMART-IV Android app; the app automatically scans and connects to the nearby ESP32 via BLE.
3. **Threshold Selection:** Sliders on screen allow setting an alert mark at 20% remaining (~100 mL).
4. **Arming:** Presses "Start Monitoring". The green status LED blinks rhythmically.
5. **Routine Duties:** Ananya attends other tasks in the ward room.
6. **Alert Event:** When fluid drops to 20%, the phone rings a distinctive high-priority chime and vibrates, while the device buzzer chimes at the bedside.
7. **Action:** Ananya taps "Acknowledge" on her phone, walks to the bed, closes the roller clamp, and replaces the bottle.

---

## 14. Risks & Mitigations

| Risk ID | Description | Severity | Likelihood | Mitigation Strategy |
| :--- | :--- | :--- | :--- | :--- |
| **RSK-01** | Ambient light variations (sunlight / shadows) disrupting optical edge detection | High | High | Use an integrated active IR LED backlight / diffuser strip and relative edge contrast gradient algorithms rather than absolute brightness thresholds. |
| **RSK-02** | Camera lens distortion or misalignment due to mechanical vibration | Medium | Medium | Rigid 3D-printed or acrylic clamping jig maintaining fixed optical axis ($90^\circ \pm 3^\circ$) and fixed standoff distance. |
| **RSK-03** | BLE connection dropout when phone is carried past range boundary | High | Medium | App implements a connection heartbeat watchdog: if packets stop for $>10\text{ s}$, the app alerts `DEVICE_OUT_OF_RANGE`. |
| **RSK-04** | ESP32-CAM brownout during concurrent Wi-Fi / Flash LED spikes | High | Medium | Disable Wi-Fi radio entirely; use low-power BLE only; filter 3.3V rail with a low-ESR 470 µF electrolytic capacitor. |
| **RSK-05** | Accidental confusion with certified medical equipment | Critical | Low | Prominent physical labeling: **"EDUCATIONAL DEMONSTRATION PROTOTYPE ONLY — NOT FOR CLINICAL OR PATIENT USE"**. |

---

## 15. Acceptance Criteria (Gate for Milestone v0.5)
- [ ] Physical bench rig completed with mock bottle and fixed bracket.
- [ ] ESP32 firmware reliably identifies meniscus passage across 10 drain cycles with zero false-negative failures.
- [ ] BLE packet latency measured $\le 2.5\text{ s}$.
- [ ] Mobile app UI displays real-time connection state, battery indicator, and animated fluid level gauge.
- [ ] Physical push-button and in-app acknowledge silencing both function seamlessly.
- [ ] All safety warnings permanently marked on hardware and app screens.
