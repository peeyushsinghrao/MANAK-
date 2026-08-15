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
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

enum class InnovationTab(val title: String) {
  NOVELTY("Novelty & Benchmarks (08)"),
  NOTEBOOK("Innovation Notebook (10)")
}

data class BenchmarkItem(
  val name: String,
  val principle: String,
  val cost: String,
  val contact: String,
  val verdict: String
)

data class PhaseLog(
  val phaseNum: String,
  val phaseTitle: String,
  val problemFaced: String,
  val rootCause: String,
  val engineeringPivot: String,
  val outcome: String
)

@Composable
fun InnovationScreen() {
  var selectedTab by remember { mutableStateOf(InnovationTab.NOVELTY) }

  val benchmarks = listOf(
    BenchmarkItem("SMART-IV MONITOR (Ours)", "ESP32 1D Edge Vision Gradient", "< ₹1,250 INR", "Zero Fluid Contact", "High Novelty (<96% cost vs commercial)"),
    BenchmarkItem("Monidrop (Clinipower)", "IR Drip Chamber Optical Counter", "> €400 (₹35,000+)", "Clamps Drip Chamber", "Expensive drop-by-drop counter"),
    BenchmarkItem("DripAssist (Shift Labs)", "Infusion Rate IR Drop Sensor", "~ $395 (₹32,000+)", "Clamps Drip Chamber", "Battery powered rate monitor"),
    BenchmarkItem("Volumetric Pumps (B.Braun)", "Motorized Peristaltic Drive", "> ₹80,000 INR", "Invasive Tube Set", "High cost clinical station pump")
  )

  val phases = listOf(
    PhaseLog(
      "Phase 1",
      "Clinical Problem & Floating Sensor Failure",
      "Nurses in rural PHCs manage 10-15 beds alone; dry bottles cause clotted cannulas.",
      "First attempt used magnetic internal float inside the bottle.",
      "FAILED: Breaches sterile barrier and risks introducing endotoxins.",
      "Decision: ZERO FLUID CONTACT principle established permanently."
    ),
    PhaseLog(
      "Phase 2",
      "Load Cell & Pendulum Oscillation Failure",
      "Mounted 5kg cantilever load cell under bottle hanger.",
      "Whenever the IV stand was wheeled or bumped, pendulum swinging caused mass oscillations of ±45g.",
      "Mechanical dampeners added too much weight and complexity to the pole.",
      "Pivot to optical non-contact vision sensing on the stand neck."
    ),
    PhaseLog(
      "Phase 3",
      "ESP32-CAM 1D Edge Vision Breakthrough",
      "Full 2D image processing was too slow (>800ms) on microcontrollers.",
      "Discovered that cylindrical fluid column acts as a refractive lens, producing a distinct 1D intensity step.",
      "Developed 1D central difference gradient dI/dy with sub-pixel parabolic peak interpolation.",
      "Execution time reduced to 11.4 ms on ESP32 Core 1."
    ),
    PhaseLog(
      "Phase 4",
      "Decoupled Failsafe Redundancy Integration",
      "Bluetooth connection drops when caregiver walks past RF barrier.",
      "Implemented Tier 1 autonomous state machine on ESP32 with active piezo buzzer and dual LED.",
      "Added 3-cycle moving confirmation window to filter fluid sloshing.",
      "Achieved 100% alert reliability even in standalone offline mode."
    )
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Slate50)
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
      Text(
        text = "RESEARCH & INNOVATION REGISTER",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.8.sp
      )
      Text(
        text = "Novelty Analysis & Engineering Log",
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
      InnovationTab.values().forEach { tab ->
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
            .testTag("innov_tab_${tab.name.lowercase()}")
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
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      when (selectedTab) {
        InnovationTab.NOVELTY -> {
          item {
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Indigo950),
              color = Indigo950
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Text("INSPIRE-MANAK NOVELTY STATEMENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Indigo100, letterSpacing = 0.8.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "First sub-₹1,500 non-contact 1D edge-vision continuous fluid monitor with decoupled bedside-mobile alert redundancy, designed specifically for rural low-resource wards.",
                  fontSize = 13.sp,
                  color = Color.White,
                  lineHeight = 19.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          items(benchmarks) { item ->
            val isOurs = item.name.contains("Ours")
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, if (isOurs) Indigo100 else Slate100, RoundedCornerShape(18.dp)),
              color = if (isOurs) Indigo50 else Color.White,
              shadowElevation = 0.5.dp
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = item.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOurs) Indigo900 else Slate900
                  )
                  Text(
                    text = item.cost,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOurs) Emerald700 else Slate700
                  )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Working Principle: ${item.principle}", fontSize = 12.sp, color = Slate700)
                Text(text = "Sterile Pathway: ${item.contact}", fontSize = 11.sp, color = Slate500)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.verdict, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (isOurs) Indigo700 else Slate500)
              }
            }
          }
        }

        InnovationTab.NOTEBOOK -> {
          items(phases) { phase ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, Slate100, RoundedCornerShape(18.dp)),
              color = Color.White,
              shadowElevation = 0.5.dp
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(phase.phaseNum, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo700)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(phase.phaseTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)

                Spacer(modifier = Modifier.height(8.dp))
                Text("Problem: ${phase.problemFaced}", fontSize = 12.sp, color = Slate700)
                Text("Pivot: ${phase.engineeringPivot}", fontSize = 12.sp, color = Indigo600, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Outcome: ${phase.outcome}", fontSize = 11.sp, color = Emerald700, fontWeight = FontWeight.SemiBold)
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
