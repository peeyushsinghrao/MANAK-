# REQUIREMENTS TRACEABILITY MATRIX (RTM)

**Project:** SMART-IV MONITOR  
**Document:** 11_TRACEABILITY_MATRIX.md  
**Version:** 0.2 (Traceability Baseline)  
**Status:** APPROVED / ENGINEERING MATRIX  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. System Engineering Traceability Matrix

This matrix provides bi-directional traceability linking high-level Product Requirements (PRD), low-level Technical Requirements (TRD), System Architecture Modules, Physical Hardware/Software Implementation Components, and Test Plan Verification Protocols.

| PRD Req ID | TRD Req ID | System Architecture Module | Implementation Component / Source | Test Suite ID | Expected Verification Criterion |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **FR-01** (Continuous Level Sampling) | **REQ-OPT-001**, **REQ-OPT-002** | Tier 1: Edge Sensing Core | ESP32-CAM OV2640 Driver (`camera_driver.cpp`, `meniscus_filter.cpp`) | **TEST-SUITE-01** | QQVGA frame captured and processed every $2.0\text{ s} \pm 0.1\text{ s}$. |
| **FR-02** (Local Audio-Visual Alert) | **REQ-HW-004**, **REQ-HW-005**, **REQ-HW-006** | Tier 1: Local Transducer Driver | Active Buzzer (GPIO 12), Red LED (GPIO 14), 2N2222 Transistor | **TEST-SUITE-02**, **TEST-SUITE-05** | Buzzer fires at $\ge 75\text{ dBA}$ ($10\text{ cm}$) and Red LED flashes at $2\text{ Hz}$ when level $\le$ threshold. |
| **FR-03** (Wireless Telemetry Stream) | **REQ-BLE-001**, **REQ-BLE-003** | Tier 1 & 2: BLE GATT Protocol | ESP32 NimBLE / BLE Server & Flutter `PacketCodec.dart` | **TEST-SUITE-04** | 10-byte binary packet transmitted every $2.0\text{ s}$; decoded without CRC errors. |
| **FR-04** (Remote Mobile Alert) | **REQ-APP-004**, **REQ-BLE-002** | Tier 2: Mobile Alarm Engine | Flutter `AndroidAlarmService.dart`, `NotificationManager` | **TEST-SUITE-02** | High-priority alarm sound and heads-up notification trigger within $3.0\text{ s}$ of threshold breach. |
| **FR-05** (Interactive Calibration) | **REQ-OPT-005**, **REQ-APP-005** | Tier 2: Calibration Wizard | Mobile `CalibrationScreen.dart`, ESP32 NVS Flash Storage | **TEST-SUITE-01** | User records full ($100\%$) and empty ($0\%$) reference pixel heights stored persistently in NVS. |
| **FR-06** (Alarm Mute / Acknowledge) | **REQ-HW-004**, **REQ-BLE-003** | Tier 1 & 2: State Machine Control | Push Button (GPIO 15 Interrupt) & App UI "Acknowledge" Button | **TEST-SUITE-02** | Both physical button and app button successfully silence buzzer and transition state to `ACKNOWLEDGED`. |
| **NFR-01** (Alert Latency $\le 3\text{ s}$) | **REQ-BLE-004**, **REQ-APP-004** | End-to-End Control Loop | Edge Gradient Filter + BLE GATT Indication Pipeline | **TEST-SUITE-02** | Latency from physical threshold passage to smartphone alarm $\le 2500\text{ ms}$. |
| **NFR-02** (Power Autonomy $\le 180\text{ mA}$) | **REQ-PWR-001**, **REQ-PWR-002** | Tier 1: Power Management | ESP32 Power Decoupling ($470\,\mu\text{F}$ capacitor) + Duty Cycle Loop | **TEST-SUITE-05** | Average operating current $< 70\text{ mA}$ at $3.3\text{V}$; continuous battery life $> 24\text{ hours}$. |
| **NFR-03** (Repeatability $\le \pm 15\text{ mL}$) | **REQ-OPT-002**, **REQ-OPT-003** | Tier 1: Gradient Extraction Engine | 1D Central Difference Kernel + 3-Cycle Temporal Filter | **TEST-SUITE-02** | Standard deviation of trigger volume $\sigma \le 8\text{ mL}$ across 10 consecutive drain cycles. |
| **NFR-04** (BOM Cost $< ₹1500$) | **REQ-HW-001**, **REQ-HW-002** | Bill of Materials (BOM) | Off-the-shelf hobbyist components and sensors | BOM Audit | Total purchase cost of components = ₹800 – ₹1,250 INR. |
| **SAF-01** (Zero Fluid Contact) | **REQ-SAF-001**, **REQ-SAF-003** | Mechanical Standoff Jig | Non-contact optical clamp ($100\text{ mm}$ air gap) | Physical Inspection | No mechanical parts or electrical conductors enter or touch the sterile fluid path. |
| **SAF-02** (Safety Disclaimers) | **REQ-SAF-002** | Enclosure & UI Branding | Physical Label + Mobile App Header Banner | Visual Audit | Disclaimer `"EDUCATIONAL DEMONSTRATION PROTOTYPE — NOT FOR CLINICAL USE"` permanently visible. |
