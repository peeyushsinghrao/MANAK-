package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

data class TestSuiteItem(
  val id: String,
  val name: String,
  val independentVar: String,
  val passCriteria: String,
  var status: String,
  var measuredMetric: String
)

@Composable
fun TestSuitesScreen() {
  var suites by remember {
    mutableStateOf(
      listOf(
        TestSuiteItem(
          "TEST-SUITE-01",
          "Static Meniscus Pixel Calibration",
          "Mock Fluid Volume (0mL to 500mL in 50mL steps)",
          "Linearity R² ≥ 0.98, Max Error ≤ ±10 mL",
          "PASSED",
          "R² = 0.994, Max Error = ±4.2 mL"
        ),
        TestSuiteItem(
          "TEST-SUITE-02",
          "Dynamic Drain Repeatability",
          "10 Consecutive Mock Drain Runs (Flow: 15 mL/min)",
          "Trigger Volume σ ≤ 8 mL, End-to-End Latency ≤ 3.0s",
          "PASSED",
          "Mean = 74.6 mL, σ = 3.8 mL, Latency = 1.4s"
        ),
        TestSuiteItem(
          "TEST-SUITE-03",
          "Ambient Light Invariance",
          "Illumination (30 Lux Dark to 1200 Lux Direct Fluorescent)",
          "Zero False Triggers across 4 Ambient Conditions",
          "PASSED",
          "SNR ≥ 18.2 dB (Diffuser Backlight active)"
        ),
        TestSuiteItem(
          "TEST-SUITE-04",
          "BLE Telemetry & CRC-8 Packet Integrity",
          "Distance (1m, 3m, 6m, 9m, 12m line of sight)",
          "Zero CRC-8 Checksum Errors, Packet Loss < 2% at 8m",
          "PASSED",
          "0 CRC errors across 2,400 packets, 0.4% loss"
        ),
        TestSuiteItem(
          "TEST-SUITE-05",
          "Power Consumption & Autonomy",
          "ESP32-CAM Active Stream vs 2.0s Intermittent Duty Cycle",
          "Average Active Current < 160mA at 5V, Autonomy > 12h",
          "PASSED",
          "Active = 124 mA, Standby = 48 mA, Battery = 16.4 hrs"
        )
      )
    )
  }

  var isRunningAll by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Slate50)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "EXPERIMENTAL VALIDATION (09_TEST_PLAN.md)",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Slate400,
          letterSpacing = 0.8.sp
        )
        Text(
          text = "5 Verification Suites",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Slate900
        )
      }

      Button(
        onClick = {
          // Re-run simulation
          suites = suites.map { it.copy(status = "PASSED") }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("run_all_tests_btn")
      ) {
        Text("Re-Run All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(suites) { suite ->
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
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = suite.id,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Indigo700
              )

              Box(
                modifier = Modifier
                  .clip(CircleShape)
                  .background(Emerald50)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = suite.status,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Emerald700,
                  letterSpacing = 0.4.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = suite.name,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Slate900
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Test Vector: ${suite.independentVar}",
              fontSize = 12.sp,
              color = Slate500
            )
            Text(
              text = "Pass Rule: ${suite.passCriteria}",
              fontSize = 12.sp,
              color = Slate700,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
              color = Indigo50
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Empirical Log:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo950)
                Text(
                  text = suite.measuredMetric,
                  fontFamily = FontFamily.Monospace,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Indigo700
                )
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
