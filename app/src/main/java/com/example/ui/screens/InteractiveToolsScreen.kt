package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Indigo950
import com.example.ui.theme.Rose100
import com.example.ui.theme.Rose50
import com.example.ui.theme.Rose600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sin

enum class ToolTab(val title: String) {
  GRADIENT_LAB("1D Gradient Lab"),
  BLE_STUDIO("BLE Packet Studio"),
  PIN_BOM("ESP32 Pinout & BOM"),
  FSM_SIMULATOR("FSM State Sim")
}

@Composable
fun InteractiveToolsScreen() {
  var currentTool by remember { mutableStateOf(ToolTab.GRADIENT_LAB) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Slate50)
  ) {
    // Header
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
      Text(
        text = "ENGINEERING LABS & SIMULATORS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.8.sp
      )
      Text(
        text = "Specification Verification Tools",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Slate900
      )
    }

    // Tab Selector
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      ToolTab.values().forEach { tab ->
        val isSelected = currentTool == tab
        val chipBg = if (isSelected) Indigo600 else Color.White
        val chipBorder = if (isSelected) Indigo600 else Slate200
        val chipText = if (isSelected) Color.White else Slate700

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(chipBg)
            .border(1.dp, chipBorder, RoundedCornerShape(20.dp))
            .clickable { currentTool = tab }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("tool_tab_${tab.name.lowercase()}")
        ) {
          Text(
            text = tab.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = chipText
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Content Area
    when (currentTool) {
      ToolTab.GRADIENT_LAB -> GradientLabView()
      ToolTab.BLE_STUDIO -> BlePacketStudioView()
      ToolTab.PIN_BOM -> PinoutAndBomView()
      ToolTab.FSM_SIMULATOR -> FsmSimulatorView()
    }
  }
}

// ==========================================
// 1. 1D Gradient & Meniscus Lab (07_LEVEL_DETECTION_SPEC.md)
// ==========================================
@Composable
fun GradientLabView() {
  var fluidLevel by remember { mutableFloatStateOf(45f) }
  var ambientLux by remember { mutableFloatStateOf(300f) }
  var opticalNoise by remember { mutableFloatStateOf(4f) }

  // Compute 1D intensity profile and spatial gradient
  val numPoints = 80
  val meniscusIndex = (numPoints * (100f - fluidLevel) / 100f).toInt().coerceIn(4, numPoints - 5)

  val intensityProfile = remember(fluidLevel, ambientLux, opticalNoise) {
    List(numPoints) { i ->
      val baseIntensity = if (i < meniscusIndex) 180f else 60f
      val noise = (sin(i * 1.7f) * opticalNoise).toFloat()
      (baseIntensity + (ambientLux / 10f) + noise).coerceIn(0f, 255f)
    }
  }

  val gradientProfile = remember(intensityProfile) {
    List(numPoints) { i ->
      if (i == 0 || i == numPoints - 1) 0f
      else (intensityProfile[i + 1] - intensityProfile[i - 1]) / 2f
    }
  }

  val peakValue = gradientProfile.minOrNull() ?: 0f
  val peakIndex = gradientProfile.indexOfFirst { it == peakValue }
  val snrDb = remember(peakValue, opticalNoise) {
    val peakMag = abs(peakValue).coerceAtLeast(1f)
    (20 * log10(peakMag / opticalNoise.coerceAtLeast(0.5f))).coerceIn(0f, 40f)
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "1D SPATIAL GRADIENT PROFILE (dI/dy)",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Slate400,
              letterSpacing = 0.6.sp
            )
            Text(
              text = String.format("SNR: %.1f dB", snrDb),
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (snrDb >= 15f) Emerald700 else Rose600
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Canvas Graph Plotting Intensity & Gradient
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Slate900)
              .padding(8.dp)
          ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val w = size.width
              val h = size.height
              val step = w / (numPoints - 1)

              // Draw zero line
              drawLine(
                color = Slate700,
                start = Offset(0f, h / 2),
                end = Offset(w, h / 2),
                strokeWidth = 1.dp.toPx()
              )

              // Draw Intensity curve (Yellow)
              val intensityPath = Path()
              intensityProfile.forEachIndexed { idx, value ->
                val x = idx * step
                val y = h - (value / 255f * h)
                if (idx == 0) intensityPath.moveTo(x, y) else intensityPath.lineTo(x, y)
              }
              drawPath(
                path = intensityPath,
                color = Color(0xFFFBBF24),
                style = Stroke(width = 1.5.dp.toPx())
              )

              // Draw Gradient curve (Indigo / Cyan)
              val gradientPath = Path()
              gradientProfile.forEachIndexed { idx, grad ->
                val x = idx * step
                val y = (h / 2) + (grad / 80f * (h / 2)).coerceIn(-h / 2, h / 2)
                if (idx == 0) gradientPath.moveTo(x, y) else gradientPath.lineTo(x, y)
              }
              drawPath(
                path = gradientPath,
                color = Color(0xFF38BDF8),
                style = Stroke(width = 2.dp.toPx())
              )

              // Mark detected Meniscus peak
              val peakX = peakIndex * step
              drawLine(
                color = Rose600,
                start = Offset(peakX, 0f),
                end = Offset(peakX, h),
                strokeWidth = 1.5.dp.toPx()
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
              Text("■ Intensity I(y)", fontSize = 10.sp, color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold)
              Text("■ Gradient dI/dy", fontSize = 10.sp, color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
              Text("■ Peak Row ($peakIndex px)", fontSize = 10.sp, color = Rose600, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Controls Card
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Simulation Parameters (07_LEVEL_DETECTION_SPEC.md)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)

          Spacer(modifier = Modifier.height(8.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Simulated Fluid Level:", fontSize = 12.sp, color = Slate700)
            Text("${fluidLevel.toInt()}%", fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Indigo600)
          }
          Slider(
            value = fluidLevel,
            onValueChange = { fluidLevel = it },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(thumbColor = Indigo600, activeTrackColor = Indigo600)
          )

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Ambient Light Illumination:", fontSize = 12.sp, color = Slate700)
            Text("${ambientLux.toInt()} Lux", fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Slate900)
          }
          Slider(
            value = ambientLux,
            onValueChange = { ambientLux = it },
            valueRange = 30f..1200f,
            colors = SliderDefaults.colors(thumbColor = Slate700, activeTrackColor = Slate700)
          )

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Optical High-Frequency Noise:", fontSize = 12.sp, color = Slate700)
            Text(String.format("σ = %.1f", opticalNoise), fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Slate900)
          }
          Slider(
            value = opticalNoise,
            onValueChange = { opticalNoise = it },
            valueRange = 1f..15f,
            colors = SliderDefaults.colors(thumbColor = Slate700, activeTrackColor = Slate700)
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

// ==========================================
// 2. BLE Packet Studio & CRC-8 Engine (06_BLE_PROTOCOL.md)
// ==========================================
@Composable
fun BlePacketStudioView() {
  var opCode by remember { mutableIntStateOf(0x01) }
  var sequenceId by remember { mutableIntStateOf(42) }
  var levelPercent by remember { mutableIntStateOf(18) }
  var isLowAlert by remember { mutableStateOf(true) }
  var isAck by remember { mutableStateOf(false) }
  var isSensorValid by remember { mutableStateOf(true) }
  var isBattLow by remember { mutableStateOf(false) }
  var batteryVoltageMv by remember { mutableIntStateOf(3840) }
  var meniscusRowPx by remember { mutableIntStateOf(84) }
  var snrDb by remember { mutableIntStateOf(24) }

  // State flags bitfield
  val stateFlags = (if (isLowAlert) 0x01 else 0) or
    (if (isAck) 0x02 else 0) or
    (if (isSensorValid) 0x04 else 0) or
    (if (isBattLow) 0x08 else 0)

  // Construct 10-byte packet
  val packetBytes = remember(opCode, sequenceId, levelPercent, stateFlags, batteryVoltageMv, meniscusRowPx, snrDb) {
    val b = IntArray(10)
    b[0] = opCode and 0xFF
    b[1] = sequenceId and 0xFF
    b[2] = levelPercent.coerceIn(0, 100) and 0xFF
    b[3] = stateFlags and 0xFF
    b[4] = (batteryVoltageMv shr 8) and 0xFF
    b[5] = batteryVoltageMv and 0xFF
    b[6] = (meniscusRowPx shr 8) and 0xFF
    b[7] = meniscusRowPx and 0xFF
    b[8] = snrDb.coerceIn(0, 255) and 0xFF

    // Compute CRC-8-CCITT (polynomial 0x07: x^8 + x^2 + x + 1)
    var crc = 0x00
    for (i in 0 until 9) {
      crc = crc xor b[i]
      for (bit in 0 until 8) {
        crc = if ((crc and 0x80) != 0) {
          ((crc shl 1) xor 0x07) and 0xFF
        } else {
          (crc shl 1) and 0xFF
        }
      }
    }
    b[9] = crc and 0xFF
    b
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Indigo950),
        color = Indigo950
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text("LIVE 10-BYTE BINARY GATT PAYLOAD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Indigo100, letterSpacing = 0.8.sp)
          Spacer(modifier = Modifier.height(8.dp))

          // Hex representation
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            packetBytes.forEachIndexed { index, byteVal ->
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (index == 9) Rose600.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f))
                  .padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "B$index",
                  fontSize = 9.sp,
                  fontFamily = FontFamily.Monospace,
                  color = Slate400
                )
                Text(
                  text = String.format("0x%02X", byteVal),
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold,
                  color = if (index == 9) Color(0xFFF43F5E) else Color.White
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Calculated CRC-8 Checksum: 0x${String.format("%02X", packetBytes[9])} (Polynomial: x⁸ + x² + x + 1)",
            fontSize = 11.sp,
            color = Emerald500,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // Packet Configuration Controls
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Payload Field Modifiers", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("OpCode: 0x01 (TELEMETRY_UPDATE)", fontSize = 12.sp, color = Slate700)
            Button(
              onClick = { sequenceId = (sequenceId + 1) % 256 },
              colors = ButtonDefaults.buttonColors(containerColor = Indigo50),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Inc Seq ($sequenceId)", fontSize = 11.sp, color = Indigo700, fontWeight = FontWeight.Bold)
            }
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Fluid Level % ($levelPercent%)", fontSize = 12.sp, color = Slate700)
          }
          Slider(
            value = levelPercent.toFloat(),
            onValueChange = { levelPercent = it.toInt() },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(thumbColor = Indigo600, activeTrackColor = Indigo600)
          )

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Bit 0: Low Level Alert", fontSize = 12.sp, color = Slate700)
            Switch(
              checked = isLowAlert,
              onCheckedChange = { isLowAlert = it },
              colors = SwitchDefaults.colors(checkedThumbColor = Rose600, checkedTrackColor = Rose100)
            )
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Bit 1: User Acknowledged", fontSize = 12.sp, color = Slate700)
            Switch(
              checked = isAck,
              onCheckedChange = { isAck = it },
              colors = SwitchDefaults.colors(checkedThumbColor = Emerald500, checkedTrackColor = Emerald100)
            )
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Bit 2: Sensor Valid Flag", fontSize = 12.sp, color = Slate700)
            Switch(
              checked = isSensorValid,
              onCheckedChange = { isSensorValid = it },
              colors = SwitchDefaults.colors(checkedThumbColor = Indigo600, checkedTrackColor = Indigo100)
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

// ==========================================
// 3. ESP32 Pinout & BOM Cost Lab (04_HARDWARE_ARCHITECTURE.md)
// ==========================================
@Composable
fun PinoutAndBomView() {
  val pins = listOf(
    Triple("GPIO 0", "Camera XCLK / Flashing Boot", "Reserved / Jumper Only"),
    Triple("GPIO 1 / 3", "UART0 TX / RX Console", "Serial Programming (FTDI)"),
    Triple("GPIO 12", "Buzzer Output (NPN Driver)", "SAFE USER GPIO (Active HIGH)"),
    Triple("GPIO 13", "Green Monitoring LED", "SAFE USER GPIO (Active HIGH)"),
    Triple("GPIO 14", "Red Alert LED Indicator", "SAFE USER GPIO (Active HIGH)"),
    Triple("GPIO 15", "Acknowledge Tactile Push Button", "SAFE USER GPIO (Internal Pull-Up)"),
    Triple("GPIO 16", "PSRAM Chip Select CS", "RESTRICTED (8MB PSRAM Memory)"),
    Triple("GPIO 26, 27", "SCCB I2C Video Control Bus", "RESTRICTED (OV2640 Sensor)"),
    Triple("GPIO 32..39", "Parallel D0..D7 Video Bus", "RESTRICTED (Camera DMA Data)")
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("ESP32-CAM (AI-Thinker) Pin Hazard Matrix", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
          Spacer(modifier = Modifier.height(4.dp))
          Text("Guarantees zero hardware conflicts with camera DMA and PSRAM bus", fontSize = 12.sp, color = Slate500)

          Spacer(modifier = Modifier.height(12.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            pins.forEach { (pin, name, status) ->
              val isSafe = status.startsWith("SAFE")
              val isRestricted = status.startsWith("RESTRICTED")

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSafe) Emerald50 else if (isRestricted) Rose50 else Slate100)
                  .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(text = pin, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                  Text(text = name, fontSize = 11.sp, color = Slate500)
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSafe) Emerald100 else if (isRestricted) Rose100 else Slate200)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = if (isSafe) "SAFE" else if (isRestricted) "CAMERA/PSRAM" else "RESERVED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSafe) Emerald700 else if (isRestricted) Rose600 else Slate700
                  )
                }
              }
            }
          }
        }
      }
    }

    // Bill of Materials Summary Card
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Indigo900),
        color = Indigo900
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text("TOTAL PROTOTYPE BOM COST AUDIT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Indigo100, letterSpacing = 0.8.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Text("Total Out-of-Pocket Purchase:", fontSize = 12.sp, color = Slate300)
              Text("₹1,185 INR", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("INSPIRE Grant Budget:", fontSize = 11.sp, color = Slate300)
              Text("₹10,000 INR (11.8% used)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Emerald500)
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

// ==========================================
// 4. FSM State Simulator (03_SYSTEM_ARCHITECTURE.md)
// ==========================================
@Composable
fun FsmSimulatorView() {
  var currentState by remember { mutableStateOf("MONITORING") }
  var sampleCount by remember { mutableIntStateOf(0) }

  val states = listOf(
    "BOOT", "ADVERTISING", "CONNECTED", "CALIBRATING",
    "MONITORING", "STANDALONE_MONITORING", "THRESHOLD_REACHED",
    "ALERTING", "ACKNOWLEDGED", "ERROR_STATE"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Deterministic 10-State Machine Controller", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
          Spacer(modifier = Modifier.height(4.dp))
          Text("Running on ESP32 FreeRTOS Core 1 Task", fontSize = 12.sp, color = Slate500)

          Spacer(modifier = Modifier.height(12.dp))

          // Active State Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(if (currentState == "ALERTING") Rose50 else Indigo50)
              .border(1.dp, if (currentState == "ALERTING") Rose100 else Indigo100, RoundedCornerShape(14.dp))
              .padding(16.dp)
          ) {
            Column {
              Text("CURRENT FSM STATE:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
              Text(
                text = currentState,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (currentState == "ALERTING") Rose600 else Indigo700
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text("Manual Transition Triggers:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { currentState = "MONITORING" },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
            ) {
              Text("Monitor", fontSize = 11.sp)
            }

            Button(
              onClick = { currentState = "THRESHOLD_REACHED" },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = Slate700)
            ) {
              Text("Debounce 3x", fontSize = 11.sp)
            }

            Button(
              onClick = { currentState = "ALERTING" },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = Rose600)
            ) {
              Text("Alert", fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { currentState = "ACKNOWLEDGED" },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
            ) {
              Text("Acknowledge", fontSize = 11.sp)
            }

            Button(
              onClick = { currentState = "STANDALONE_MONITORING" },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = Slate500)
            ) {
              Text("Offline Failsafe", fontSize = 11.sp)
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
