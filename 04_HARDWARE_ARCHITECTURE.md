# HARDWARE ARCHITECTURE DOCUMENT

**Project:** SMART-IV MONITOR  
**Document:** 04_HARDWARE_ARCHITECTURE.md  
**Version:** 0.2 (Hardware Baseline)  
**Status:** APPROVED / ENGINEERING SPECIFICATION  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Hardware Overview & Inventory Classification

The hardware design prioritizes accessible, low-cost, off-the-shelf hobbyist electronics suitable for a school-level science innovation project (INSPIRE-MANAK budget $\le ₹10,000$).

### 1.1 Hardware Inventory Register

| Category | Component Description | Quantity | Estimated Cost (INR) | Source / Status |
| :--- | :--- | :--- | :--- | :--- |
| **ALREADY OWNED** | Espressif ESP32-CAM Module (AI-Thinker with OV2640 camera & onboard PSRAM) | 1 | ₹0 (Owned) | Available in workspace |
| **ALREADY OWNED** | Standard ESP32 DevKit V1 (30-pin Dual-Core Microcontroller) | 1 | ₹0 (Owned) | Available as alternative |
| **ALREADY OWNED** | High-Quality USB-C / Micro-USB Data Cable | 1 | ₹0 (Owned) | Available in workspace |
| **PURCHASE REQUIRED** | 5V Active Magnetic Buzzer ($2.4\text{ kHz}$, $\ge 75\text{ dBA}$) | 1 | ₹25 – ₹45 | Standard electronics hobby store |
| **PURCHASE REQUIRED** | 2N2222 or SS8050 NPN General Purpose Transistor (TO-92) | 1 | ₹5 – ₹10 | Standard electronics hobby store |
| **PURCHASE REQUIRED** | 1N4148 / 1N4007 Flyback Diode (for buzzer inductive spike suppression) | 1 | ₹3 – ₹5 | Standard electronics hobby store |
| **PURCHASE REQUIRED** | 5mm High-Brightness LEDs (1x Red Alert, 1x Green Normal) | 2 | ₹6 – ₹10 | Standard electronics hobby store |
| **PURCHASE REQUIRED** | 1/4W Metal Film Resistors ($2\times 220\,\Omega$ for LEDs, $1\times 1\text{k}\Omega$ for base) | 5 | ₹5 – ₹10 | Standard electronics hobby store |
| **PURCHASE REQUIRED** | 6mm Momentary Tactile Push Button (4-pin) | 1 | ₹5 – ₹10 | Standard electronics hobby store |
| **PURCHASE REQUIRED** | $470\,\mu\text{F}$ 16V Low-ESR Electrolytic Capacitor + $100\text{ nF}$ Ceramic Capacitor | 2 | ₹10 – ₹15 | Power decoupling |
| **PURCHASE REQUIRED** | 400-Point Solderless Breadboard & Male-to-Male / Male-to-Female Jumper Wires | 1 set | ₹120 – ₹180 | Prototyping interconnects |
| **PURCHASE REQUIRED** | FT232RL or CP2102 USB-to-UART Serial Adapter (for flashing ESP32-CAM) | 1 | ₹180 – ₹250 | Firmware flashing tool |
| **PURCHASE REQUIRED** | Mock 500 mL Plastic Saline Container + Infusion Tube Set (for non-medical water demo) | 1 | ₹60 – ₹120 | Educational medical supply / pharmacy |
| **PURCHASE REQUIRED** | Diffuse White LED Strip / Backlight Plate (5V USB powered) | 1 | ₹50 – ₹100 | Contrast enhancement |
| **PURCHASE REQUIRED** | Demonstration Retort Stand / Laboratory Clamp & Acrylic Bracket | 1 | ₹300 – ₹500 | Structural mounting |
| **OPTIONAL (BENCHMARK)** | HX711 24-Bit ADC Module + 1kg/5kg Aluminum Cantilever Load Cell | 1 set | ₹220 – ₹350 | Ground-truth reference comparison |
| **FUTURE EXTENSION** | 3.7V 18650 2000mAh Li-ion Cell + TP4056 USB-C Charging Board | 1 | ₹250 – ₹350 | Autonomous cordless battery operation |
| **FUTURE EXTENSION** | Custom 2-Layer Fr4 PCB + 3D-Printed Snap-Fit Enclosure | 1 | ₹600 – ₹900 | Post-competition miniaturization |

**Total Estimated Out-of-Pocket Purchase Cost for Prototype MVP:** **₹800 – ₹1,250 INR** (well within ₹10,000 INSPIRE grant).

---

## 2. ESP32-CAM (AI-Thinker) Pin Allocation & Hardware Constraints

> [!CAUTION]
> **ESP32-CAM Pin Hazard Audit:**  
> The ESP32-CAM board multiplexes most of its GPIOs to the OV2640 camera interface and external PSRAM chip. Specifying reserved camera pins for external peripherals will corrupt image capture or cause boot failure.

### 2.1 Hardware Pin Mapping Table

| Board Pin | Pin Function | Internal Connection / Restriction | Assigned Function in SMART-IV | Notes & Circuit Requirements |
| :--- | :--- | :--- | :--- | :--- |
| **GPIO 0** | Camera XCLK / Boot Strapping | Used for clock generation during run; must be pulled to GND only during flashing. | **Reserved for Flashing / Camera XCLK** | Connected to flashing jumper only. |
| **GPIO 1** | U0TXD (UART0 TX) | Serial debug output during development. | **UART Debug Console** | Connected to FTDI RXD during debugging. |
| **GPIO 3** | U0RXD (UART0 RX) | Serial programming input. | **UART Programming** | Connected to FTDI TXD during programming. |
| **GPIO 2** | Camera D0 / Strapping Pin | Connected to Camera data bus D0 and onboard 47k pull-down. | **Reserved for Camera** | Do NOT connect external loads. |
| **GPIO 4** | Onboard Flash Light LED | Connected to bright flash LED via NPN transistor. | **Auxiliary Illuminator (Optional)** | Controlled via PWM for scene illumination. |
| **GPIO 33** | Onboard Red Indicator LED | Active LOW LED on the module rear. | **Firmware Heartbeat / Status LED** | Software controlled (Inverted: 0=ON, 1=OFF). |
| **GPIO 12** | Free GPIO / HS2_DATA2 | Must be LOW at boot for standard 3.3V flash voltage. | **Buzzer Driver Output (Active HIGH)** | Pulled LOW through $1\text{k}\Omega$ base resistor at boot. |
| **GPIO 13** | Free GPIO / HS2_DATA3 | Available general purpose digital I/O. | **Green Monitoring LED (Active HIGH)** | Connected via $220\,\Omega$ series resistor. |
| **GPIO 14** | Free GPIO / HS2_CLK | Available general purpose digital I/O. | **Red Alert LED (Active HIGH)** | Connected via $220\,\Omega$ series resistor. |
| **GPIO 15** | Free GPIO / HS2_CMD | Available general purpose digital I/O (Pulled HIGH internally).| **Physical Acknowledge Button (Active LOW)** | Connected between GPIO 15 and GND with internal pull-up. |
| **GPIO 16** | PSRAM CS | **DEDICATED TO 8MB PSRAM CHIP** | **DO NOT USE** | Reserved for DMA frame buffers. |
| **GPIO 26, 27** | SCCB SIOD / SIOC | I2C control bus for OV2640 camera registers. | **Dedicated to Camera Control** | Reserved. |
| **GPIO 32..39** | Camera Bus (VSYNC, HREF, PCLK, D1-D7) | High-speed parallel video data lines. | **Dedicated to OV2640 Video Bus** | Reserved. |

---

## 3. Detailed Circuit Schematics & Interfacing

### 3.1 Buzzer Driver Circuit (GPIO 12)
Because the ESP32 GPIOs can source a maximum of only $12\text{ mA}$ (insufficient for a $30\text{ mA}$ active buzzer), an NPN transistor buffer is mandatory:

```
                  +5V (or +3.3V)
                     │
                     ├────────────┐
                     │            │
                   ┌─┴─┐        ┌─┴─┐
                   │ + │        │   │
               [Active Buzzer]  ▲  [1N4148 Diode]
                   │ - │        │   │
                   └─┬─┘        └───┘
                     │            │
                     ├────────────┘
                     │
                  Collector
                  ┌──┴──┐
GPIO 12 ──[ 1kΩ ]─┤ Q1  │ 2N2222 / SS8050 NPN
                  └──┬──┘
                  Emitter
                     │
                    GND
```

### 3.2 Status & Alert LED Circuits (GPIO 13, GPIO 14)
```
GPIO 13 (Green) ──[ 220Ω ]──▶|── GND  (Forward Voltage ≈ 2.1V, Current ≈ 5.5mA)
GPIO 14 (Red)   ──[ 220Ω ]──▶|── GND  (Forward Voltage ≈ 1.9V, Current ≈ 6.3mA)
```

### 3.3 Acknowledge Button Circuit (GPIO 15)
```
GPIO 15 ──────┬──────[ Normally Open Push Button ]────── GND
              │
        (Internal 45kΩ Pull-Up to 3.3V)
```
*Debounce Filter:* Firmware implements a software debounce filter of $50\text{ ms}$ on GPIO 15 falling edge interrupt.

### 3.4 Power Decoupling Network
```
5V / 3.3V In ──┬──────┬──────[ ESP32 VDD Pin ]
               │      │
             ┌─┴─┐  ┌─┴─┐
             │ + │  │   │
       470µF ───    ─── 100nF Ceramic
        16V  │ - │  │   │
             └─┬─┘  └─┬─┘
               │      │
GND ───────────┴──────┴──────[ ESP32 GND Pin ]
```

---

## 4. Optical Standoff & Mechanical Clamping Rig

```
  ┌─────────────────────────────────────────────────────────────┐
  │                   BENCH TEST JIG SCHEMATIC                  │
  │                                                             │
  │     [ Retort Stand Post ]                                   │
  │               │                                             │
  │        ┌──────┴──────┐                                      │
  │        │ Mock Bottle │ (Clear plastic 500 mL)               │
  │        │   Hanger    │                                      │
  │        └──────┬──────┘                                      │
  │               │                                             │
  │         ┌───────────┐         ◄── Light Transmission        │
  │         │   100%    │  ┌─────────────────────────┐          │
  │         │    75%    │  │   Diffuse LED Panel     │          │
  │         │  - 50% -  │  │   (Uniform Backlight)   │          │
  │         │  - 25% -  │  └─────────────────────────┘          │
  │         │  Threshold│              ▲                        │
  │         │   ===0%== │              │                        │
  │         └─────┬─────┘              │                        │
  │               │                    │ (Standoff: 100mm ±10mm)│
  │               │                    │                        │
  │               │             ┌──────┴──────┐                 │
  │         [Drain Tube]        │  ESP32-CAM  │                 │
  │               │             │  Sensor Rig │                 │
  │               ▼             └──────┬──────┘                 │
  │         [Waste Valve]              │                        │
  │                               [Stand Clamp]                 │
  └─────────────────────────────────────────────────────────────┘
```

- **Focal Distance:** Fixed at $100\text{ mm} \pm 10\text{ mm}$ to match the manual macro focal plane of the OV2640 lens.
- **Angle of Incidence:** Orthogonal ($90^\circ \pm 2^\circ$) to eliminate optical perspective distortion along the vertical liquid column.
- **Optical Shielding:** A 3-sided black cardboard or laser-cut acrylic shroud surrounds the optical path to block stray overhead classroom fluorescent lights.
