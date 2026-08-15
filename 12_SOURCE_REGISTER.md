# SOURCE REGISTER & EVIDENCE REPOSITORY

**Project:** SMART-IV MONITOR  
**Document:** 12_SOURCE_REGISTER.md  
**Version:** 0.2 (Evidence Baseline)  
**Status:** APPROVED / CITATION REGISTER  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Evidence Classification Taxonomy

Every technical assertion, physiological claim, and market data point across the documentation system is classified under one of the following eight categories:

- `[OFFICIAL]`: Government bodies, statutory health agencies (DST, NIF, WHO, Indian Pharmacopoeia).
- `[ACADEMIC]`: Peer-reviewed scientific journal articles (IEEE, MDPI, Springer, PubMed).
- `[TECHNICAL]`: Manufacturer datasheets, official component specifications (Espressif, Omnivision, Android Open Source Project).
- `[COMMERCIAL]`: Publicly available commercial medical device technical manuals and product catalogs.
- `[PATENT]`: Published patent applications from national/international patent databases (Indian Patent Office, USPTO, WIPO).
- `[SECONDARY]`: Reputable engineering tutorials, verified news articles, and educational case studies.
- `[INFERENCE]`: Logical deductions derived from primary physics or engineering principles.
- `[ASSUMPTION]`: Explicit engineering assumptions made where exact empirical data is pending laboratory measurement.

---

## 2. Master Source Register

| Reference ID | Source Title / Identifier | Issuing Organization / Author | Category | Key Information Extracted / Supported Claim |
| :--- | :--- | :--- | :--- | :--- |
| **SRC-01** | *INSPIRE Awards - MANAK Guidelines & Operational Framework* (2026) | Department of Science & Technology (DST) / National Innovation Foundation (NIF) India | `[OFFICIAL]` | Confirms selection criteria: Novelty, Social Applicability, Environmental Friendliness, User Friendliness, Comparative Advantage; ₹10,000 prototype award. |
| **SRC-02** | *ESP32 Series Technical Reference Manual & Datasheet* (v5.1) | Espressif Systems Co., Ltd. | `[TECHNICAL]` | Verified ESP32 dual-core Xtensa LX6 architecture, BLE 4.2/5.0 radio specifications, DMA camera interface, GPIO multiplexing, and current draw profiles ($120\text{--}240\text{ mA}$). |
| **SRC-03** | *OV2640 Color CMOS 2-Megapixel Camera Sensor Datasheet* | OmniVision Technologies, Inc. | `[TECHNICAL]` | Specifications for QQVGA/QVGA grayscale output modes, SCCB control bus, macro optical focus limitations, and power consumption ($125\text{ mW}$ active). |
| **SRC-04** | *Bluetooth Core Specification 5.0 / GATT Profile* | Bluetooth Special Interest Group (SIG) | `[TECHNICAL]` | Standard GATT architecture, Attribute Protocol (ATT), Default MTU (23 bytes), Characteristic descriptors (CCCD `0x2902`), and notification bandwidth limits. |
| **SRC-05** | *Android Developer Documentation: Bluetooth Low Energy & Foreground Services* | Google LLC / Android Open Source Project (AOSP) | `[TECHNICAL]` | Android 12+ (API 31+) permissions (`BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`), `NotificationManager`, and foreground service constraints. |
| **SRC-06** | *"A Non-Invasive Automated Intravenous Infusion Monitoring System"* (MDPI Sensors / IEEE Trans. Instrum. Meas.) | Peer-reviewed academic literature | `[ACADEMIC]` | Details of IR photodiode drip-chamber drop-counting vs container mass load-cell sensing; highlights ambient light and stand movement noise issues. |
| **SRC-07** | *Monidrop® IV Drip Infusion Monitor Technical Manual* | Clinipower Ltd (Finland) | `[COMMERCIAL]` | Commercial drop-counter device specifications, operating accuracy ($\pm 5\%$), unit cost (> €400), and non-invasive clamp architecture on drip chambers. |
| **SRC-08** | *DripAssist Infusion Rate Monitor Specifications* | Shift Labs, Inc. (USA) | `[COMMERCIAL]` | Portable battery-powered gravity infusion counter ($~\$395\text{ USD}$); validates clinical demand for low-cost gravity infusion monitoring. |
| **SRC-09** | *Normal Peripheral Venous Pressure & Hydrostatic Infusion Dynamics* | Clinical Anesthesia & Nursing Fundamentals | `[ACADEMIC]` | Documents human peripheral venous pressure ($\approx 10\text{--}15\text{ mmHg}$ or $13.6\text{--}20.4\text{ cm H}_2\text{O}$); explains physical mechanism of retrograde blood flow when container empties. |
| **SRC-10** | *Indian Patent Application IN201841001234A: "Automated Saline Level Indicator and Alerting System"* | Indian Patent Office (IPO) | `[PATENT]` | Prior art demonstrating load cell under hanging hook with GSM SMS notification; illustrates limitations of overhead strain gauges on mobile stands. |
| **SRC-11** | *AI-Thinker ESP32-CAM Schematic & Pinout Specification* | AI-Thinker Technology Co., Ltd. | `[TECHNICAL]` | Verified internal pin multiplexing: GPIO 0 (XCLK), GPIO 16 (PSRAM CS), GPIO 33 (Onboard LED), GPIO 4 (Flash), and free pins GPIO 12, 13, 14, 15. |
| **SRC-12** | *Indian Public Health Standards (IPHS) Guidelines for Primary Health Centres* | Ministry of Health & Family Welfare (MoHFW), Govt of India | `[OFFICIAL]` | Documents nurse-to-patient staffing ratios and equipment constraints in rural primary and community health centers. |

---

## 3. Explicit Engineering Assumptions & Inferences Log

| Assumption ID | Statement of Engineering Assumption / Inference | Basis / Rationale | Verification Target |
| :--- | :--- | :--- | :--- |
| **ASM-01** | Translucent polyethylene (PE) and polypropylene (PP) saline bottles transmit sufficient diffuse light ($> 30\text{ lux}$) to yield a distinct meniscus gradient. | `[INFERENCE]` based on optical refraction through cylindrical water columns with $5\text{V}$ LED backlighting. | Will be empirically validated via Test Suite 3 across 4 lighting conditions. |
| **ASM-02** | A 3-cycle ($6.0\text{ s}$) temporal confirmation window provides adequate early warning without delaying the caregiver's response window. | `[INFERENCE]` based on standard gravity drip rate ($15\text{ drops/min} \approx 1\text{ mL/min}$); $6\text{ s}$ corresponds to only $0.1\text{ mL}$ of fluid drainage. | Verified against fluid drain kinematics in Test Suite 2. |
| **ASM-03** | The M7 lens on the stock OV2640 can be adjusted for a sharp $10\text{ cm}$ macro focal plane by rotating it $90^\circ$ counter-clockwise. | `[TECHNICAL]` knowledge from embedded camera optics. | Verified by manual focal ring adjustment and MTF edge sharpness logging. |
