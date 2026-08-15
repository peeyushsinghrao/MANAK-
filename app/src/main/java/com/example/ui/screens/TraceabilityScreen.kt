package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
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

enum class TraceTab(val title: String) {
  RTM("Traceability Matrix (11)"),
  SOURCES("Source Register (12)"),
  RISKS("Risk & Questions (13)")
}

data class RtmItem(
  val prdId: String,
  val trdId: String,
  val module: String,
  val component: String,
  val testSuite: String,
  val priority: String
)

data class SourceItem(
  val refId: String,
  val title: String,
  val issuer: String,
  val category: String,
  val claim: String
)

data class RiskItem(
  val riskId: String,
  val title: String,
  val severity: String,
  val likelihood: String,
  val mitigation: String
)

@Composable
fun TraceabilityScreen() {
  var selectedTab by remember { mutableStateOf(TraceTab.RTM) }

  val rtmItems = listOf(
    RtmItem("FR-01", "REQ-OPT-001, 002", "Tier 1: Edge Sensing", "ESP32-CAM OV2640 Driver", "TEST-SUITE-01", "CRITICAL"),
    RtmItem("FR-02", "REQ-HW-004, 005, 006", "Tier 1: Local Transducers", "Active Buzzer + Red LED", "TEST-SUITE-02, 05", "CRITICAL"),
    RtmItem("FR-03", "REQ-BLE-001, 003", "Tier 1/2: BLE GATT", "NimBLE & PacketCodec", "TEST-SUITE-04", "HIGH"),
    RtmItem("FR-04", "REQ-APP-004, BLE-002", "Tier 2: Alarm Engine", "AndroidAlarmService", "TEST-SUITE-02", "CRITICAL"),
    RtmItem("FR-05", "REQ-OPT-005, APP-005", "Tier 2: Calibration", "CalibrationWizard & NVS", "TEST-SUITE-01", "MEDIUM"),
    RtmItem("FR-06", "REQ-HW-004, BLE-003", "Tier 1/2: FSM Mute", "Tactile Button (GPIO 15)", "TEST-SUITE-02", "HIGH"),
    RtmItem("NFR-01", "REQ-BLE-004, APP-004", "End-to-End Latency", "Gradient Filter + GATT", "TEST-SUITE-02", "HIGH"),
    RtmItem("NFR-02", "REQ-PWR-001, 002", "Power Management", "470µF Cap + Duty Cycle", "TEST-SUITE-05", "HIGH"),
    RtmItem("NFR-03", "REQ-OPT-002, 003", "Level Accuracy", "1D Central Difference", "TEST-SUITE-02", "HIGH"),
    RtmItem("SAF-01", "REQ-SAF-001, 003", "Mechanical Standoff", "Non-Contact Clamp (100mm)", "Physical Audit", "CRITICAL")
  )

  val sourceItems = listOf(
    SourceItem("SRC-01", "INSPIRE Awards MANAK Guidelines", "DST / NIF India", "[OFFICIAL]", "₹10,000 grant budget & student innovation evaluation guidelines"),
    SourceItem("SRC-02", "ESP32 Technical Reference Manual", "Espressif Systems", "[TECHNICAL]", "Dual-core Xtensa LX6, BLE 4.2/5.0 and current profiles"),
    SourceItem("SRC-03", "OV2640 CMOS Image Sensor", "OmniVision", "[TECHNICAL]", "Grayscale capture & SCCB register control"),
    SourceItem("SRC-04", "Bluetooth Core Specification 5.0", "Bluetooth SIG", "[TECHNICAL]", "GATT attribute hierarchy & CCCD notifications"),
    SourceItem("SRC-05", "Android BLE & Foreground Service Guide", "Google / AOSP", "[TECHNICAL]", "Android 12+ runtime permissions & AlarmManager"),
    SourceItem("SRC-06", "Non-Invasive Infusion Monitoring Review", "MDPI Sensors / IEEE", "[ACADEMIC]", "Trade-offs between drip chamber vs container level"),
    SourceItem("SRC-07", "Monidrop IV Drip Monitor Product Manual", "Clinipower Ltd", "[COMMERCIAL]", "Commercial optical drop counter benchmark (>€400)"),
    SourceItem("SRC-08", "DripAssist Infusion Rate Monitor", "Shift Labs", "[COMMERCIAL]", "Battery operated infusion monitor benchmark ($395)"),
    SourceItem("SRC-09", "Normal Peripheral Venous Pressure Dynamics", "Clinical Anesthesia", "[ACADEMIC]", "10-15 mmHg hydrostatic pressure causing cannula backflow"),
    SourceItem("SRC-10", "Patent IN201841001234A: Smart IV Stand", "Indian Patent Office", "[PATENT]", "Hanging load cell system with GSM SMS alerts")
  )

  val riskItems = listOf(
    RiskItem("RSK-01", "Ambient light glare / fluorescent shadows", "5 (Critical)", "3 (Moderate)", "Fixed diffuse LED backlight strip + 3-sided black optical shroud"),
    RiskItem("RSK-02", "Camera macro lens blur at 10cm", "3 (Moderate)", "3 (Moderate)", "Rotate M7 lens 90° CCW to adjust macro focal plane"),
    RiskItem("RSK-03", "BLE connection drop out of range", "4 (High)", "2 (Low)", "Local autonomous buzzer + 6s heartbeat loss alarm on phone"),
    RiskItem("RSK-04", "ESP32 brownout during camera init", "3 (Moderate)", "1 (Low)", "470µF low-ESR bulk electrolytic capacitor across 3.3V rail"),
    RiskItem("RSK-05", "Confusion with certified medical devices", "5 (Critical)", "1 (Low)", "Prominent permanent label: 'EDUCATIONAL DEMONSTRATION ONLY'")
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Slate50)
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
      Text(
        text = "SYSTEMS TRACEABILITY & GOVERNANCE",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.8.sp
      )
      Text(
        text = "Requirements, Citations & Risks",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Slate900
      )
    }

    // Tabs
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      TraceTab.values().forEach { tab ->
        val isSelected = selectedTab == tab
        val chipBg = if (isSelected) Indigo600 else Color.White
        val chipBorder = if (isSelected) Indigo600 else Slate200
        val chipText = if (isSelected) Color.White else Slate700

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(chipBg)
            .border(1.dp, chipBorder, RoundedCornerShape(20.dp))
            .clickable { selectedTab = tab }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("trace_tab_${tab.name.lowercase()}")
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

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      when (selectedTab) {
        TraceTab.RTM -> {
          items(rtmItems) { item ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Slate100, RoundedCornerShape(16.dp)),
              color = Color.White,
              shadowElevation = 0.5.dp
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Indigo50)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                      Text(item.prdId, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo700)
                    }
                    Text("→ ${item.trdId}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Slate500)
                  }

                  Box(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(if (item.priority == "CRITICAL") Rose50 else Indigo50)
                      .padding(horizontal = 8.dp, vertical = 3.dp)
                  ) {
                    Text(
                      text = item.priority,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (item.priority == "CRITICAL") Rose600 else Indigo700
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "${item.module} — ${item.component}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Verified By: ${item.testSuite}", fontSize = 11.sp, color = Emerald700, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        TraceTab.SOURCES -> {
          items(sourceItems) { src ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Slate100, RoundedCornerShape(16.dp)),
              color = Color.White,
              shadowElevation = 0.5.dp
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(src.refId, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo700)
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Slate100)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(src.category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate700)
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(src.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                Text(src.issuer, fontSize = 11.sp, color = Indigo600)
                Spacer(modifier = Modifier.height(6.dp))
                Text(src.claim, fontSize = 12.sp, color = Slate500, lineHeight = 16.sp)
              }
            }
          }
        }

        TraceTab.RISKS -> {
          items(riskItems) { risk ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Slate100, RoundedCornerShape(16.dp)),
              color = Color.White,
              shadowElevation = 0.5.dp
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(risk.riskId, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Rose600)
                  Text("Sev: ${risk.severity} | Likelihood: ${risk.likelihood}", fontSize = 10.sp, color = Slate500)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(risk.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Mitigation: ${risk.mitigation}", fontSize = 12.sp, color = Emerald700, lineHeight = 16.sp)
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
