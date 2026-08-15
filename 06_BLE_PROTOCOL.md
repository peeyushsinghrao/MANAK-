# BLUETOOTH LOW ENERGY (BLE) PROTOCOL SPECIFICATION

**Project:** SMART-IV MONITOR  
**Document:** 06_BLE_PROTOCOL.md  
**Version:** 0.2 (Protocol Baseline)  
**Status:** APPROVED / ENGINEERING SPECIFICATION  
**Last Updated:** 2026-08-15  
**Source of Truth:** Project Systems Engineering Register  

---

## 1. Protocol Architecture & Protocol Selection Rationale

### 1.1 Compact Binary Framing vs. JSON Comparison
During system design, text-based JSON framing was compared against fixed-structure binary frames for BLE GATT characteristics:

| Evaluation Dimension | Compact Binary Framing (Selected) | JSON Text String Framing |
| :--- | :--- | :--- |
| **Payload Size** | **7 to 12 Bytes** (fits comfortably in standard BLE 4.0/4.2 default 23-byte MTU). | **45 to 90 Bytes** (requires dynamic MTU expansion or fragmented ATT packets). |
| **Microcontroller Overhead** | Direct memory struct mapping; zero dynamic heap allocations on ESP32. | Dynamic C++ heap parsing (`ArduinoJson`), risking memory fragmentation. |
| **Battery / Radio Energy** | Transmits in a single $1.2\text{ ms}$ radio burst. | Longer on-air transmit duration; higher RF energy consumption. |
| **Error Detection** | Embedded CRC-8 checksum per packet. | Text syntax validation; harder to detect bit-flip corruptions. |
| **Verdict** | **SELECTED AS MANDATORY STANDARD.** | **REJECTED.** |

---

## 2. GATT Service & Characteristic UUID Definitions

All UUIDs utilized in this project are custom 128-bit UUIDs generated for the SMART-IV project.

> [!NOTE]
> All UUIDs below are **[PROJECT-DEFINED]** for development and demonstration use. They are not assigned by the Bluetooth SIG.

### 2.1 Profile Hierarchy Table

```
SMART-IV Primary Service
UUID: 4fafc201-1fb5-459e-8fcc-c5c9c331914b [PROJECT-DEFINED]
 │
 ├── 1. Telemetry Characteristic (Read / Notify)
 │      UUID: beb5483e-36e1-4688-b7f5-ea07361b26a8 [PROJECT-DEFINED]
 │      Properties: READ | NOTIFY
 │      Client Characteristic Configuration Descriptor (CCCD): 0x2902
 │
 ├── 2. Command / Control Characteristic (Write / Read)
 │      UUID: beb5483e-36e1-4688-b7f5-ea07361b26a9 [PROJECT-DEFINED]
 │      Properties: WRITE | WRITE_WITHOUT_RESPONSE | READ
 │
 └── 3. Calibration Characteristic (Write / Read)
        UUID: beb5483e-36e1-4688-b7f5-ea07361b26aa [PROJECT-DEFINED]
        Properties: WRITE | READ
```

---

## 3. Advertising & Discovery Specification

- **Advertised Local Name:** `SMART-IV-XXXX` (where `XXXX` = 4 uppercase hexadecimal characters from the ESP32 Base MAC address, e.g., `SMART-IV-3A1F`).
- **Advertising Interval:** $200\text{ ms}$ (Advertising channel 37, 38, 39).
- **Service UUID in Advertisement:** The 128-bit Primary Service UUID is included in the Scan Response packet to allow filtered background discovery on Android.
- **TX Power:** Set to $+3\text{ dBm}$ (provides reliable $10\text{ m}$ line-of-sight indoor coverage while mitigating interference).

---

## 4. Packet Framing & Byte Structures

All multi-byte numeric fields are encoded in **Big-Endian (Network Byte Order)**.

### 4.1 Telemetry Packet (Notify / Read) — Characteristic `...26a8`
Emitted by the ESP32 periodically every $2.0\text{ s}$ during active monitoring, and immediately upon state transitions.

**Packet Size:** Exactly 10 Bytes

```
Byte 0      Byte 1      Byte 2      Byte 3      Byte 4..5        Byte 6..7        Byte 8      Byte 9
┌──────────┬──────────┬──────────┬──────────┬────────────────┬────────────────┬──────────┬──────────┐
│ OpCode   │ SeqID    │ Level %  │ State    │ Meniscus Y(px) │ Battery (mV)   │ Reserved │ CRC-8    │
│  0x10    │ 0x00..FF │  0..100  │ Flags    │ uint16_t (px)  │ uint16_t (mV)  │  0x00    │ Checksum │
└──────────┴──────────┴──────────┴──────────┴────────────────┴────────────────┴──────────┴──────────┘
```

#### Field Descriptions:
- **`OpCode` (1 Byte):** Constant `0x10` (`TELEMETRY_UPDATE`).
- **`SeqID` (1 Byte):** 8-bit rolling sequence counter ($0\text{--}255$, rolls over to $0$) to detect dropped RF packets.
- **`Level %` (1 Byte):** Computed fluid remaining percentage ($0\text{--}100\%$, unsigned integer).
- **`State Flags` (1 Byte Bitfield):**
  - `Bit 0` ($0\text{x}01$): Monitoring Active ($1 = \text{Active}$, $0 = \text{Idle/Paused}$)
  - `Bit 1` ($0\text{x}02$): Threshold Reached Warning ($1 = \text{Warning Triggered}$)
  - `Bit 2` ($0\text{x}04$): Alarm Sounding ($1 = \text{Buzzer ON}$)
  - `Bit 3` ($0\text{x}08$): Alarm Acknowledged / Muted ($1 = \text{Muted}$)
  - `Bit 4` ($0\text{x}10$): Calibration Valid ($1 = \text{Calibrated}$, $0 = \text{Uncalibrated}$)
  - `Bit 5` ($0\text{x}20$): Sensor Error ($1 = \text{Camera/Sensor Failure}$)
  - `Bit 6` ($0\text{x}40$): Low Battery Alert ($1 = \text{Voltage} < 3.3\text{V}$)
  - `Bit 7` ($0\text{x}80$): Reserved / Future Expansion
- **`Meniscus Y` (2 Bytes, `uint16_t`):** Raw vertical pixel coordinate ($0\text{--}240$) of detected meniscus boundary for diagnostic display.
- **`Battery` (2 Bytes, `uint16_t`):** Estimated supply rail voltage in millivolts (e.g., `4200` for $4.20\text{V}$).
- **`Reserved` (1 Byte):** `0x00`.
- **`CRC-8` (1 Byte):** Checksum computed over Bytes 0 through 8 using CRC-8-CCITT polynomial.

---

### 4.2 Command Packet (Write) — Characteristic `...26a9`
Transmitted by the Mobile App to control device state and configure threshold parameters.

**Packet Size:** Variable (3 to 6 Bytes)

```
Byte 0      Byte 1       Byte 2..N          Byte Last
┌──────────┬───────────┬──────────────────┬──────────┐
│ OpCode   │ PayloadLen│ Payload Data     │ CRC-8    │
└──────────┴───────────┴──────────────────┴──────────┘
```

#### Supported Command OpCodes:

| OpCode | Command Name | Payload Length | Payload Format | Description & Behavior |
| :--- | :--- | :--- | :--- | :--- |
| `0x01` | `CMD_START_MONITOR` | 0 Bytes | None | Arms edge detection loop; green LED begins slow pulse. |
| `0x02` | `CMD_STOP_MONITOR` | 0 Bytes | None | Disarms edge detection; returns to standby state. |
| `0x03` | `CMD_SET_THRESHOLD` | 1 Byte | `uint8_t threshold_pct` ($5\text{--}50$) | Sets trigger threshold (e.g., `0x14` for 20%); stored in NVS. |
| `0x04` | `CMD_ACKNOWLEDGE` | 0 Bytes | None | Mutes active buzzer and clears remote alarm condition. |
| `0x05` | `CMD_FORCE_CAPTURE` | 0 Bytes | None | Triggers immediate single-frame capture for alignment preview. |
| `0x06` | `CMD_RESET_DEVICE` | 0 Bytes | None | Triggers a software reboot of the ESP32 microcontroller. |

---

### 4.3 Calibration Packet (Write / Read) — Characteristic `...26aa`
Configures the coordinate boundary mapping between pixel rows and fluid volume.

**Packet Size:** Exactly 7 Bytes

```
Byte 0      Byte 1          Byte 2..3          Byte 4..5          Byte 6
┌──────────┬──────────────┬──────────────────┬──────────────────┬──────────┐
│ OpCode   │ CalibStep    │ Y_Full (px)      │ Y_Empty (px)     │ CRC-8    │
│  0x20    │ 0x01/0x02/03 │ uint16_t (MSB..LSB) uint16_t (MSB..LSB) Checksum │
└──────────┴──────────────┴──────────────────┴──────────────────┴──────────┘
```

- **`CalibStep`:**
  - `0x01`: Capture & Record Full Level ($100\%$ pixel coordinate $Y_{\text{full}}$).
  - `0x02`: Capture & Record Empty Level ($0\%$ pixel coordinate $Y_{\text{empty}}$).
  - `0x03`: Write Complete Calibration Record directly into ESP32 NVS flash.

---

## 5. CRC-8 Checksum Algorithm Specification

To guarantee integrity over noisy RF channels, every packet incorporates a standard CRC-8 checksum:
- **Polynomial:** $x^8 + x^2 + x + 1$ (Standard Polynomial `0x07`)
- **Initial Value:** `0x00`
- **Final XOR:** `0x00`
- **Reflect Input / Output:** False

### Reference C / C++ Implementation:
```c
uint8_t calculate_crc8(const uint8_t *data, size_t length) {
    uint8_t crc = 0x00;
    for (size_t i = 0; i < length; i++) {
        crc ^= data[i];
        for (uint8_t bit = 0; bit < 8; bit++) {
            if (crc & 0x80) {
                crc = (crc << 1) ^ 0x07;
            } else {
                crc <<= 1;
            }
        }
    }
    return crc;
}
```

---

## 6. Error Codes & Recovery Actions

| Error Code | Identifier | Description | Automatic Firmware Recovery Action |
| :--- | :--- | :--- | :--- |
| `0xE1` | `ERR_CRC_MISMATCH` | Received command failed CRC-8 check. | Discard packet; send NACK indication with expected sequence ID. |
| `0xE2` | `ERR_INVALID_PARAM` | Threshold value $<5\%$ or $>50\%$. | Discard packet; retain previous valid threshold. |
| `0xE3` | `ERR_CAMERA_TIMEOUT` | OV2640 failed to deliver frame buffer within $500\text{ ms}$. | Reinitialize I2C bus and camera driver; log warning flag in telemetry. |
| `0xE4` | `ERR_LOW_CONTRAST` | Meniscus gradient peak below minimum SNR threshold ($<15\text{ DN}$). | Increase auxiliary LED brightness; assert `LOW_CONFIDENCE` flag. |
| `0xE5` | `ERR_NVS_WRITE` | Flash storage write failed. | Fall back to RAM-only calibration; notify client app. |
