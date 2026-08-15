# EXPERIMENTAL VALIDATION & TEST PLAN

**Project:** SMART-IV MONITOR  
**Document:** 09_TEST_PLAN.md  
**Version:** 0.2 (Test Plan Baseline)  
**Status:** APPROVED / SCIENTIFIC PROTOCOL  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Scientific Testing Objectives & Hypotheses

### 1.1 Core Scientific Hypotheses
- **Hypothesis 1 (Optical Detection Repeatability):** The 1D spatial intensity gradient $\frac{\partial I}{\partial y}$ computed across a backlight-illuminated mock container will identify the fluid meniscus with a geometric repeatability of $\le \pm 5\text{ mm}$ (corresponding to $\le \pm 15\text{ mL}$ in a 500 mL container) across 10 drain trials.
- **Hypothesis 2 (End-to-End Latency):** The total time elapsed from the physical liquid meniscus descending past the configured threshold coordinate to the smartphone rendering the alarm notification will be $\le 3.0\text{ seconds}$.
- **Hypothesis 3 (False-Positive Immunity):** The 3-cycle temporal confirmation filter will suppress 100% of false alarms induced by mechanical stand vibrations ($\le 5\text{ Hz}$) and transient liquid sloshing.

---

## 2. Experimental Variables & Controls

- **Independent Variables:**
  - Configured Alert Threshold Percentage ($10\%, 20\%, 30\%$).
  - Ambient Lighting Level ($50\text{ lux}$ dim room, $300\text{ lux}$ fluorescent indoor, $800\text{ lux}$ indirect sunlight).
  - BLE Communication Distance ($1\text{ m}, 5\text{ m}, 10\text{ m}, 15\text{ m}$ through partition).
  - Gravity Drain Flow Rate ($50\text{ mL/min}$ fast gravity drain vs $10\text{ mL/min}$ slow trickle).
- **Dependent Variables:**
  - Error in Detected Trigger Volume ($\text{mL}$ error compared to ground-truth precision scale).
  - Alert Propagation Latency ($\text{milliseconds}$).
  - Packet Delivery Rate (PDR $\%$).
  - Number of False-Positive Triggers per 100 cycles.
- **Controlled / Constant Variables:**
  - Mock Bottle Geometry: Standard 500 mL semi-rigid translucent plastic infusion container.
  - Test Fluid: Distilled water at room temperature ($22^\circ\text{C} \pm 2^\circ\text{C}$) with $0.01\%$ neutral blue dye for visual contrast.
  - Optical Standoff Distance: Fixed at $100\text{ mm} \pm 2\text{ mm}$.
  - Camera Configuration: OV2640, QQVGA ($160\times 120$), Grayscale mode, fixed exposure.

---

## 3. Test Suites & Blank Experimental Data Protocols

> [!IMPORTANT]
> **Data Integrity Rule:**  
> All values marked **[HYPOTHESIS / EXPECTED]** represent engineering predictions. The table rows provide the standardized blank protocol for recording actual physical measurements during experimental laboratory execution.

---

### Test Suite 1: Static Calibration Linearity & Geometric Precision
- **Procedure:** Fill container to exact graduated increments ($500\text{ mL}, 400\text{ mL}, 300\text{ mL}, 200\text{ mL}, 100\text{ mL}, 50\text{ mL}, 0\text{ mL}$) verified on an analytical weight balance ($\pm 0.1\text{ g}$). Record pixel coordinate $y^*$ reported by ESP32-CAM and evaluate linearity $R^2$.

| Target Volume (mL) | Ground Truth Weight (g) | Detected Pixel Row $y^*$ [MEASURED] | Calculated Volume (mL) [MEASURED] | Absolute Error (mL) [MEASURED] | Signal SNR (DN/DN) [MEASURED] | Status (PASS/FAIL) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **500 mL (100%)** | 500.0 g | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 15$]* | *[Expected: $\ge 3.5$]* | PENDING |
| **400 mL (80%)**  | 400.0 g | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 15$]* | *[Expected: $\ge 3.5$]* | PENDING |
| **300 mL (60%)**  | 300.0 g | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 15$]* | *[Expected: $\ge 3.5$]* | PENDING |
| **200 mL (40%)**  | 200.0 g | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 15$]* | *[Expected: $\ge 3.5$]* | PENDING |
| **100 mL (20%)**  | 100.0 g | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 15$]* | *[Expected: $\ge 3.5$]* | PENDING |
| **50 mL (10%)**   | 50.0 g  | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 15$]* | *[Expected: $\ge 3.5$]* | PENDING |
| **0 mL (Empty)**   | 0.0 g   | *[To be recorded]* | *[To be recorded]* | *[Expected: $\le 10$]* | *[Expected: $\ge 3.5$]* | PENDING |

---

### Test Suite 2: Dynamic Continuous Drain Repeatability (10 Consecutive Trials)
- **Procedure:** Set alert threshold to $20\%$ ($100\text{ mL}$). Open gravity drain valve to steady flow rate ($\sim 30\text{ mL/min}$). Record the exact balance weight when the ESP32 buzzer fires and when the smartphone displays the notification.

| Trial # | Threshold Set (mL) | Balance Weight at Local Buzzer Fire (g) [MEASURED] | Volume Error at Fire (mL) [MEASURED] | Smartphone Alert Latency ($\Delta t$ ms) [MEASURED] | False-Positive Event? (Y/N) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Trial 01** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 02** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 03** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 04** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 05** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 06** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 07** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 08** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 09** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **Trial 10** | 100 mL (20%) | *[To be recorded]* | *[Expected: $\pm 15$]* | *[Expected: $\le 2500$]* | *[Expected: N]* |
| **STATISTICS**| **Target: 100 mL**| **Mean ($\mu$): TBD** | **Std Dev ($\sigma$): TBD** | **Max Latency: TBD** | **Total False: 0** |

---

### Test Suite 3: Ambient Illumination Stress Tests
- **Procedure:** Test edge detection robustness across 4 lighting conditions while fluid is held at $150\text{ mL}$.

| Lighting Environment | Measured Lux at Rig | Meniscus Detected? | Gradient Peak Height $G(y^*)$ | Noise Floor $\sigma_{\text{noise}}$ | Calculated SNR | Pass / Fail Criteria |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Darkroom (Night Simulation)** | $10\text{ lux}$ | *[To be recorded]* | *[To be recorded]* | *[To be recorded]* | *[Expected: $\ge 4.0$]* | Meniscus located within $\pm 2\text{ px}$ |
| **Standard Fluorescent Room** | $300\text{ lux}$ | *[To be recorded]* | *[To be recorded]* | *[To be recorded]* | *[Expected: $\ge 3.5$]* | Meniscus located within $\pm 2\text{ px}$ |
| **Bright Indoor Daylight** | $800\text{ lux}$ | *[To be recorded]* | *[To be recorded]* | *[To be recorded]* | *[Expected: $\ge 3.0$]* | Meniscus located within $\pm 3\text{ px}$ |
| **Directional Flashlight Glare** | $2500\text{ lux}$ | *[To be recorded]* | *[To be recorded]* | *[To be recorded]* | *[Expected: $\ge 2.0$]* | Evaluates optical shroud efficacy |

---

### Test Suite 4: Wireless BLE Range, Packet Loss & Watchdog Latency
- **Procedure:** Maintain active telemetry stream; move mobile terminal progressively away from the bench rig; record RSSI, Packet Delivery Rate over 100 packets, and link loss detection time.

| Distance & Obstacle | Average RSSI (dBm) [MEASURED] | Packet Delivery Rate (%) [MEASURED] | Notification Latency (ms) [MEASURED] | Link Lost Trigger Time (s) [MEASURED] |
| :--- | :--- | :--- | :--- | :--- |
| **1 meter (Bedside)** | *[Expected: $\sim -45\text{ dBm}$]* | *[Expected: $100\%$]* | *[Expected: $< 150\text{ ms}$]* | N/A (Link Healthy) |
| **5 meters (Same Room)** | *[Expected: $\sim -65\text{ dBm}$]* | *[Expected: $\ge 98\%$]* | *[Expected: $< 200\text{ ms}$]* | N/A (Link Healthy) |
| **10 meters (Line of Sight)** | *[Expected: $\sim -78\text{ dBm}$]* | *[Expected: $\ge 92\%$]* | *[Expected: $< 350\text{ ms}$]* | N/A (Link Healthy) |
| **12 meters (Brick Wall Obstacle)** | *[Expected: $\sim -88\text{ dBm}$]* | *[Expected: $\ge 75\%$]* | *[Expected: $< 800\text{ ms}$]* | N/A (Link Marginally Stable)|
| **15 meters (Full Disconnect)**| *[Signal Lost]* | $0\%$ | N/A | *[Expected: $\le 6.0\text{ s}$ via Watchdog]* |

---

### Test Suite 5: Acoustic Output & Electrical Power Validation
- **Procedure:** Measure sound pressure level of buzzer using a calibrated sound meter at $10\text{ cm}$ and $1\text{ m}$. Measure active and sleep current using an inline precision current shunt.

| Parameter | Theoretical / Spec Requirement | Measured Value | Test Condition | Compliance Status |
| :--- | :--- | :--- | :--- | :--- |
| **Buzzer Acoustic Level (10 cm)** | $\ge 75\text{ dBA}$ | *[To be measured]* | $5.0\text{V}$ Active Drive | PENDING |
| **Buzzer Acoustic Level (1 meter)**| $\ge 60\text{ dBA}$ | *[To be measured]* | $5.0\text{V}$ Active Drive | PENDING |
| **Peak Active Current (Frame Capture)**| $< 180\text{ mA}$ | *[To be measured]* | $3.3\text{V}$ Rail, Camera ON | PENDING |
| **Average Monitoring Current** | $< 70\text{ mA}$ | *[To be measured]* | Duty cycled (every 2.0 s) | PENDING |
| **Battery Autonomy (2000 mAh pack)**| $> 24\text{ hours}$ | *[To be calculated]* | Extrapolated from average current | PENDING |
