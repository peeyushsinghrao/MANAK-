# OPEN TECHNICAL QUESTIONS & RISK REGISTER

**Project:** SMART-IV MONITOR  
**Document:** 13_OPEN_QUESTIONS.md  
**Version:** 0.2 (Risk & Question Baseline)  
**Status:** ACTIVE / WORKING REGISTER  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Open Technical Questions (OTQ)

| Question ID | Domain | Technical Question / Investigation Topic | Current Hypothesis / Working Plan | Impact on Prototype | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **OTQ-01** | Optics / Sensor | How does the 1D intensity gradient algorithm perform when the test bottle has embossed graduation numbers or label stickers directly in the ROI column? | Place the ROI bounding box in a clear vertical strip between graduation labels, or apply a software morphological opening filter to remove small text edges. | High (requires user visual positioning during clamp setup). | `IN PROGRESS` |
| **OTQ-02** | Hardware / Power | Will the active piezo buzzer induce ground bounce or inductive flyback into the ESP32 ADC/GPIO lines when switching off? | Added a 1N4148 clamping flyback diode across buzzer terminals and a $1\text{k}\Omega$ series base resistor on the 2N2222 transistor. | Medium (resolved in schematic 04). | `RESOLVED` |
| **OTQ-03** | Mobile / Android | Will aggressive Chinese OEM Android battery savers (MIUI/ColorOS) kill the Flutter BLE notify stream when the screen is locked for $>30\text{ minutes}$? | Implement a sticky Android Foreground Service with `FOREGROUND_SERVICE_CONNECTED_DEVICE` type and acquire a partial `WakeLock`. | High (critical for overnight ward monitoring). | `IN PROGRESS` |
| **OTQ-04** | Mechanical / Optics | What is the minimum mechanical rigidity required for the retort stand clamp to prevent thermal drift of the camera focal plane? | Use an aluminum laboratory bosshead clamp with a 3D-printed PETG or $3\text{ mm}$ acrylic mounting arm. | Medium (bench rig construction). | `OPEN` |
| **OTQ-05** | Firmware / Memory | Does running FreeRTOS with NimBLE BLE stack leave sufficient internal SRAM for a $160\times 120$ QQVGA frame buffer without PSRAM? | QQVGA 8-bit grayscale requires only $19,200\text{ bytes}$ ($18.75\text{ KB}$). ESP32 has $\sim 300\text{ KB}$ available internal SRAM, allowing execution even on non-PSRAM ESP32 boards. | High (validated by memory budget calculation). | `RESOLVED` |

---

## 2. Technical Risk Matrix & Mitigation Actions

```
  5 │                                  [RSK-01: Light Glare]
    │
S 4 │               [RSK-03: BLE Range]
E   │
V 3 │ [RSK-04: ESP32 Brownout]       [RSK-02: Lens Focus]
E   │
R 2 │               [RSK-05: Label Noise]
I   │
T 1 │
Y 0 └───┬───────────────┬───────────────┬───────────────┬───────
        1               2               3               4
                         LIKELIHOOD
```

### Risk Register Details:
1. **RSK-01: Ambient Lighting Glare (Severity 5, Likelihood 3):**  
   *Risk:* Direct sunlight or flickering overhead AC fluorescent tubes corrupt the 1D gradient calculation.  
   *Mitigation:* Fixed diffuse LED backlight panel with physical 3-sided black optical shielding hood.
2. **RSK-02: Camera Lens Macro Blur (Severity 3, Likelihood 3):**  
   *Risk:* Stock OV2640 lens is factory set for infinity; at $10\text{ cm}$, the image is completely blurred.  
   *Mitigation:* Manually break the glue spot on the M7 lens barrel and rotate $90^\circ$ CCW for close macro focus.
3. **RSK-03: BLE Packet Drop in Crowded RF Environments (Severity 4, Likelihood 2):**  
   *Risk:* 2.4 GHz Wi-Fi interference in exhibition halls causing lost alarm notifications.  
   *Mitigation:* Local hardware buzzer and LED operate 100% autonomously; mobile app implements a $6\text{ s}$ link-loss watchdog alert.
4. **RSK-04: ESP32 Power Brownout (Severity 3, Likelihood 1):**  
   *Risk:* Inrush current during camera startup causes ESP32 reset.  
   *Mitigation:* $470\,\mu\text{F}$ low-ESR electrolytic capacitor installed directly across VDD and GND pins.

---

## 3. Recommended Next Engineering Action Steps

1. **Step 1 (Hardware Assembly):** Wire the buzzer driver, LEDs, push button, and power decoupling capacitor on the solderless breadboard according to `04_HARDWARE_ARCHITECTURE.md`.
2. **Step 2 (Optics Setup):** Mount the ESP32-CAM on the test stand facing the backlit mock saline container; adjust the lens barrel to achieve sharp macro focus on the container graduations.
3. **Step 3 (Firmware Flashing):** Flash the ESP32 edge firmware containing the 1D gradient meniscus extraction algorithm and custom BLE GATT service (`06_BLE_PROTOCOL.md`).
4. **Step 4 (Mobile App Deployment):** Compile and launch the companion Android/Flutter application on a test smartphone and establish BLE bonding.
5. **Step 5 (Empirical Data Collection):** Execute Test Suites 1 through 5 as specified in `09_TEST_PLAN.md`, recording actual measured physical values into the blank validation tables.
