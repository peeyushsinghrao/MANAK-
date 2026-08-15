# FLUID LEVEL DETECTION ENGINEERING SPECIFICATION

**Project:** SMART-IV MONITOR  
**Document:** 07_LEVEL_DETECTION_SPEC.md  
**Version:** 0.2 (Detection Baseline)  
**Status:** APPROVED / ENGINEERING SPECIFICATION  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Comparative Analysis of Candidate Sensing Mechanisms

To establish a defensible engineering choice for a school-level / INSPIRE-MANAK prototype, six transduction modalities were analyzed:

| Modality | Physical Principle | Cost (INR) | Accuracy (Approx.) | Ambient Light Sensitivity | Complexity on ESP32 | Non-Invasive Safety | Demonstrability & Visual Novelty | Overall Suitability |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Vision (ESP32-CAM / OV2640)** | 1D Column Gradient Meniscus Tracking | **₹350 – ₹450** | $\pm 3\%\text{ to }\pm 5\%$ | **High** (Mitigated by backlighting) | Medium (1D signal processing in C++) | **Complete (External clamp)** | **Exceptional** (Live visual ROI & edge plot) | **RECOMMENDED PRIMARY PROTOTYPE** |
| **B. Load Cell (HX711 + Strain Gauge)** | Gravimetric Mass Measurement ($F = mg$) | ₹250 – ₹350 | $\pm 1\%\text{ to }\pm 2\%$ | **Zero** | Low (2-wire bit-bang ADC) | **Complete (External hanging hook)** | Moderate (Numeric grams/mL readout) | **RECOMMENDED BENCHMARK STANDARD** |
| **C. IR Transmissive Array** | Differential IR Refraction Index (Liquid vs Air) | ₹150 – ₹250 | Discrete steps ($\pm 10\%$) | Low | Very Low (Digital GPIO reads) | **Complete (External clip-on)** | Low (Only discrete LED indicators) | Feasible, but lacks fine continuous threshold control |
| **D. Capacitive Ribbon (FPC strip / FDC1004)** | Dielectric Permittivity Shift ($\varepsilon_r \approx 80$ for water vs $\approx 1$ for air) | ₹300 – ₹600 | $\pm 4\%\text{ to }\pm 8\%$ | **Zero** | Medium (I2C capacitance-to-digital) | **Complete (External adhesive strip)** | Low (Invisible electrostatic effect) | Prone to parasitic hand-proximity capacitance |
| **E. Time-of-Flight (VL53L0X / VL53L1X Laser)** | Optical Time of Flight through container top | ₹250 – ₹400 | $\pm 8\%\text{ to }\pm 15\%$ | Medium | Low (I2C) | **Breached** (Requires open top or clear flat lid) | Low | Liquid surface specular reflections cause high false readings |
| **F. Ultrasonic Ranging (HC-SR04)** | Acoustic Echo Reflection | ₹70 – ₹120 | Poor ($\pm 20\%$) | **Zero** | Low (Echo pulse timing) | **Breached** (Echo blocked by sealed bottle top) | Poor | Unusable on sealed sterile medical bottles |

### Final Engineering Selection Rationale:
- **Primary Selection:** **Mechanism A (ESP32-CAM Computer Vision)** is chosen because:
  1. It exploits hardware already owned and available in the workspace.
  2. It achieves continuous ($0\text{--}100\%$) non-contact level resolution without placing mechanical strain on the IV hanging hook.
  3. It offers exceptional educational and scientific demonstrability for science fairs (showing image processing, gradient extraction, and computer vision concepts).
- **Benchmarking Protocol:** A **Mechanism B (Load Cell HX711)** test bench is defined in the Test Plan as a ground-truth measurement device to scientifically calibrate and validate the optical vision system's accuracy.

---

## 2. Mathematical Formulation of Optical Meniscus Detection

The edge algorithm treats the 2D camera image as a vertical column of horizontal pixel slices, collapsing 2D computational complexity into a deterministic, ultra-fast 1D signal processing problem.

```
                  2D Camera Frame (QQVGA: 160 x 120)
       ┌────────────────────────────────────────────────────────┐
       │                                                        │
       │                   ROI Bounding Box                     │
       │                ┌────────────────────┐                  │
       │                │      X_start       │                  │
Row 0  │                │         │          │                  │
       │                │         ▼          │                  │
       │                │ ┌────────────────┐ │                  │
       │                │ │  Air Column    │ │  Brightness: I_air
       │                │ │ (High Light)   │ │                  │
       │                │ ├────────────────┤ │ ◄── Meniscus Peak: y*
       │                │ │ Liquid Column  │ │  Brightness: I_liq
       │                │ │ (Refracted)    │ │                  │
       │                │ └────────────────┘ │                  │
       │                │    Width: W_roi    │                  │
Row 119│                └────────────────────┘                  │
       │                                                        │
       └────────────────────────────────────────────────────────┘
```

### 2.1 Step 1: 1D Spatial Intensity Averaging
Within a vertical Region of Interest (ROI) defined by horizontal span $[X_{\text{start}}, X_{\text{start}} + W - 1]$ and vertical span $[Y_{\text{start}}, Y_{\text{end}}]$:

$$I_{\text{avg}}(y) = \frac{1}{W} \sum_{x=X_{\text{start}}}^{X_{\text{start}}+W-1} I_{\text{gray}}(x, y)$$

Where:
- $I_{\text{gray}}(x, y) \in [0, 255]$ is the 8-bit grayscale pixel intensity at coordinate $(x, y)$.
- $W \ge 20$ pixels is the ROI column averaging width, which filters out high-frequency sensor thermal noise and plastic surface scratches.

### 2.2 Step 2: 1D Vertical Gradient Operator
The vertical first-order central difference gradient $G(y)$ is computed along the vertical profile:

$$G(y) = \left| I_{\text{avg}}(y+1) - I_{\text{avg}}(y-1) \right| \quad \text{for } y \in [Y_{\text{start}}+1, Y_{\text{end}}-1]$$

Because water and plastic form a curved optical meniscus, the refractive bending of transmitted backlight produces a localized dark diffraction fringe immediately adjacent to the bright air/liquid interface, generating a sharp, distinctive peak in $G(y)$.

### 2.3 Step 3: Sub-Pixel Meniscus Peak Estimation
The coarse integer meniscus row coordinate $y_0$ corresponds to the global maximum of $G(y)$:

$$y_0 = \arg\max_{y} G(y)$$

To enhance resolution beyond discrete pixel boundaries, a 3-point parabolic sub-pixel interpolation is applied:

$$y^* = y_0 + \frac{G(y_0 - 1) - G(y_0 + 1)}{2 \cdot \left( G(y_0 - 1) - 2G(y_0) + G(y_0 + 1) \right)}$$

### 2.4 Step 4: Signal-to-Noise Ratio (Confidence Metric)
To verify that the detected peak is a genuine liquid meniscus and not ambient background noise, a Confidence Ratio (SNR) is evaluated:

$$\text{SNR} = \frac{G(y_0)}{\frac{1}{N} \sum_{y \ne y_0} G(y)}$$

- If $\text{SNR} \ge 3.5$: Detection is classified as `HIGH_CONFIDENCE`.
- If $2.0 \le \text{SNR} < 3.5$: Detection is classified as `MARGINAL_CONFIDENCE` (flagged in telemetry).
- If $\text{SNR} < 2.0$: Peak is rejected; previous valid reading is held, and `LOW_CONTRAST_FAULT` is asserted.

### 2.5 Step 5: Geometric Calibration to Fluid Volume Percentage
Let $Y_{\text{full}}$ be the calibrated pixel row when the bottle contains $100\%$ fluid ($500\text{ mL}$), and $Y_{\text{empty}}$ be the calibrated pixel row when the bottle is empty ($0\text{ mL}$).

Because the standard cylindrical section of the infusion bottle exhibits uniform cross-sectional area $A$, the fluid level percentage $P(y^*)$ is linear with vertical pixel displacement:

$$P(y^*) = \left( \frac{Y_{\text{empty}} - y^*}{Y_{\text{empty}} - Y_{\text{full}}} \right) \times 100\%$$

*Clamping Constraint:* The output is bounded: $P_{\text{clamped}} = \min(100, \max(0, P(y^*)))$.

---

## 3. Optical Setup & Environmental Controls

```
               CROSS-SECTIONAL OPTICAL BENCH GEOMETRY
               
      [ Diffuse Backlight Panel ] (Uniform 5V LED 6000K)
                 │
                 ▼  (Transmitted Diffuse Rays)
       ╭───────────────────╮
       │                   │
       │   Mock Plastic    │ ◄── Translucent Bottle Wall
       │   Saline Bottle   │
       │  (Water Column)   │
       │                   │
       ╰───────────────────╯
                 │
                 ▼  (Refracted Meniscus Contour)
       ┌───────────────────┐
       │   Light Shroud    │ (Black Non-Reflective Cardboard / Baffle)
       └───────────────────┘
                 │
                 ▼ (Standoff: 100 mm ± 10 mm)
            ┌─────────┐
            │ [Lens]  │ OV2640 (Focal Ring Adjusted ~1/4 Turn CCW)
            │ ESP32   │
            │  -CAM   │
            └─────────┘
```

1. **Backlight Transillumination:** A low-cost $5\text{V}$ diffuse white LED panel is positioned directly behind the bottle. The liquid acts as a cylindrical lens, bending light and creating a pronounced high-contrast dark line at the meniscus boundary.
2. **Ambient Light Shielding:** A 3-sided matte black shroud surrounds the optical gap to isolate the sensor from overhead ceiling lights and sunlight glare.
3. **Macro Focus Calibration:** The stock OV2640 camera lens (factory glued for infinity focus) is gently rotated approximately $90^\circ$ ($1/4$ turn) counter-clockwise to shift its sharp focal plane from infinity to $10\text{ cm}$.

---

## 4. Noise Handling & Robustness Filtering

### 4.1 3-Cycle Temporal Confirmation Filter
To prevent false alarms caused by accidental stand bumping, fluid sloshing, or momentary bubbles:
1. When $P(y^*) \le P_{\text{threshold}}$, an internal software counter `alarm_debounce_count` increments by 1.
2. If $P(y^*) > P_{\text{threshold}}$ on any subsequent sample, `alarm_debounce_count` is reset to 0.
3. Only when `alarm_debounce_count` $\ge 3$ consecutive cycles ($6.0\text{ seconds}$ continuous) does the system transition to `ALERT_ACTIVE`.

### 4.2 Outlier Jump Rejection
In a gravity infusion, fluid level only decreases monotonically. Any single-frame measurement indicating a sudden upward jump $> +8\%$ within $2\text{ seconds}$ is classified as an optical artifact (e.g., passing shadow) and suppressed.

---

## 5. Prototype Limitations & Clear Safety Disclaimers

1. **Non-Medical Grade Precision:** This vision algorithm provides approximate container volume monitoring ($\pm 15\text{ mL}$ for a $500\text{ mL}$ bottle) suitable for secondary school science demonstration. It is not intended for micro-dosing or chemotherapy delivery.
2. **Bottle Geometry Dependency:** The linear height-to-volume mapping assumes a cylindrical or gently tapered container body. Irregularly shaped pouches (collapsible soft bags) require multi-point piece-wise calibration.
3. **Tint and Opacity:** Highly turbid or dark opaque medications (e.g., iron infusions) will invert the transmission contrast and require calibration profile switching.
