package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.MyApplicationTheme
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

import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Tune
import com.example.ui.screens.DocumentViewerScreen
import com.example.ui.screens.InteractiveToolsScreen
import com.example.ui.screens.TestSuitesScreen
import com.example.ui.screens.TraceabilityScreen
import com.example.ui.screens.InnovationScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        SmartIvApp()
      }
    }
  }
}

enum class NavigationTab(val label: String, val icon: ImageVector) {
  MONITOR("MONITOR", Icons.Filled.Assessment),
  DOCS("13 DOCS", Icons.Filled.Description),
  LABS("LABS", Icons.Filled.Tune),
  TESTS("TESTS", Icons.Filled.Science),
  TRACE("TRACE", Icons.Filled.FactCheck),
  RESEARCH("RESEARCH", Icons.Filled.Lightbulb)
}

@Composable
fun SmartIvApp() {
  var selectedTab by remember { mutableStateOf(NavigationTab.MONITOR) }
  var fluidLevelPercent by remember { mutableFloatStateOf(18f) }
  var alarmThreshold by remember { mutableFloatStateOf(20f) }
  var isBleConnected by remember { mutableStateOf(true) }
  var isAlarmAcknowledged by remember { mutableStateOf(false) }

  val isLowLevel = fluidLevelPercent <= alarmThreshold

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(Slate50),
    topBar = {
      HeaderBar(
        isConnected = isBleConnected,
        onToggleConnect = { isBleConnected = !isBleConnected }
      )
    },
    bottomBar = {
      BottomNavBar(
        currentTab = selectedTab,
        onTabSelected = { selectedTab = it }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Slate50)
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        NavigationTab.MONITOR -> {
          MonitorScreen(
            fluidLevel = fluidLevelPercent,
            onLevelChange = {
              fluidLevelPercent = it
              if (it > alarmThreshold) {
                isAlarmAcknowledged = false
              }
            },
            threshold = alarmThreshold,
            onThresholdChange = { alarmThreshold = it },
            isLowLevel = isLowLevel,
            isAcknowledged = isAlarmAcknowledged,
            onAcknowledge = { isAlarmAcknowledged = true }
          )
        }
        NavigationTab.DOCS -> {
          DocumentViewerScreen()
        }
        NavigationTab.LABS -> {
          InteractiveToolsScreen()
        }
        NavigationTab.TESTS -> {
          TestSuitesScreen()
        }
        NavigationTab.TRACE -> {
          TraceabilityScreen()
        }
        NavigationTab.RESEARCH -> {
          InnovationScreen()
        }
      }
    }
  }
}

@Composable
fun HeaderBar(
  isConnected: Boolean,
  onToggleConnect: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding(),
    color = Color.White,
    shadowElevation = 0.5.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // App Identity
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Indigo600),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "S",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        }
        Column {
          Text(
            text = "SMART-IV",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Slate900,
            lineHeight = 18.sp
          )
          Text(
            text = "System Engineering Lead",
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color = Slate500,
            letterSpacing = 0.6.sp
          )
        }
      }

      // BLE Status Pill
      val bgPillColor = if (isConnected) Emerald50 else Rose50
      val borderPillColor = if (isConnected) Emerald100 else Rose100
      val textPillColor = if (isConnected) Emerald700 else Rose600
      val dotColor = if (isConnected) Emerald500 else Rose600

      Row(
        modifier = Modifier
          .clip(CircleShape)
          .background(bgPillColor)
          .border(1.dp, borderPillColor, CircleShape)
          .clickable { onToggleConnect() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("ble_status_pill"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(dotColor)
        )
        Text(
          text = if (isConnected) "BLE CONNECTED" else "BLE OFFLINE",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = textPillColor,
          letterSpacing = 0.4.sp
        )
      }
    }
  }
}

@Composable
fun MonitorScreen(
  fluidLevel: Float,
  onLevelChange: (Float) -> Unit,
  threshold: Float,
  onThresholdChange: (Float) -> Unit,
  isLowLevel: Boolean,
  isAcknowledged: Boolean,
  onAcknowledge: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Hero Fluid Level Card (Rounded 32dp, Slate/Indigo aesthetic)
    item {
      HeroFluidCard(
        fluidLevel = fluidLevel,
        threshold = threshold,
        isLowLevel = isLowLevel,
        isAcknowledged = isAcknowledged
      )
    }

    // 2. Interactive Simulation & Threshold Slider Controls
    item {
      SimulationControlCard(
        fluidLevel = fluidLevel,
        onLevelChange = onLevelChange,
        threshold = threshold,
        onThresholdChange = onThresholdChange
      )
    }

    // 3. Stats Grid (2 Columns: Sensor Confidence & Battery Power)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Sensor CV Analysis Card
        Box(modifier = Modifier.weight(1f)) {
          StatCard(
            label = "SENSOR CONF.",
            value = "CV Analysis",
            subtitle = "ROI Validated (98.4%)",
            subtitleColor = Slate500
          )
        }

        // Battery Power Card
        Box(modifier = Modifier.weight(1f)) {
          StatCard(
            label = "BATTERY",
            value = "3.84 V",
            subtitle = "Stable Power (94%)",
            subtitleColor = Emerald700
          )
        }
      }
    }

    // 4. System Diagnostic / Action Banner (Deep Indigo 900)
    item {
      DiagnosticBanner(
        isLowLevel = isLowLevel,
        isAcknowledged = isAcknowledged,
        onAcknowledge = onAcknowledge
      )
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}

@Composable
fun HeroFluidCard(
  fluidLevel: Float,
  threshold: Float,
  isLowLevel: Boolean,
  isAcknowledged: Boolean
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.15f,
    targetValue = 0.35f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(32.dp))
      .border(1.dp, Slate100, RoundedCornerShape(32.dp)),
    color = Color.White,
    shadowElevation = 1.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Text(
          text = "ID: ESP32-CAM-01",
          fontFamily = FontFamily.Monospace,
          fontSize = 11.sp,
          color = Slate400,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Level Header Display
      Text(
        text = "Approximate Fluid Level",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Slate500
      )

      Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(vertical = 4.dp)
      ) {
        Text(
          text = "${fluidLevel.toInt()}",
          fontSize = 54.sp,
          fontWeight = FontWeight.Light,
          color = Slate900,
          letterSpacing = (-1.5).sp
        )
        Text(
          text = "%",
          fontSize = 24.sp,
          color = Slate400,
          modifier = Modifier.padding(bottom = 8.dp, start = 2.dp),
          fontWeight = FontWeight.Normal
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // IV Bottle Fluid Visualizer
      Box(
        modifier = Modifier
          .width(136.dp)
          .height(190.dp)
          .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
          .background(Slate50)
          .border(2.dp, Slate200, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
      ) {
        // Vertical Centerline
        Box(
          modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(Slate200)
            .align(Alignment.Center)
        )

        // Graduation tick marks
        Column(
          modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 16.dp, horizontal = 8.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Box(modifier = Modifier.width(10.dp).height(1.dp).background(Slate300))
          Box(modifier = Modifier.width(14.dp).height(1.dp).background(Slate300))
          Box(modifier = Modifier.width(10.dp).height(1.dp).background(Slate300))
          Box(modifier = Modifier.width(14.dp).height(1.dp).background(Slate300))
          Box(modifier = Modifier.width(10.dp).height(1.dp).background(Slate300))
        }

        // Animated Fluid Fill Height
        val animatedLevel by animateFloatAsState(
          targetValue = (fluidLevel / 100f).coerceIn(0.02f, 1f),
          animationSpec = tween(600, easing = FastOutSlowInEasing),
          label = "fluidHeight"
        )

        val fluidGradient = if (isLowLevel) {
          Brush.verticalGradient(
            listOf(Rose600.copy(alpha = 0.45f), Rose600.copy(alpha = 0.75f))
          )
        } else {
          Brush.verticalGradient(
            listOf(Indigo600.copy(alpha = 0.35f), Indigo700.copy(alpha = 0.65f))
          )
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedLevel)
            .align(Alignment.BottomCenter)
            .background(fluidGradient)
            .border(
              width = 1.dp,
              color = if (isLowLevel) Rose600 else Indigo600,
              shape = RoundedCornerShape(0.dp)
            )
        ) {
          // Meniscus surface highlight / pulse
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.White.copy(alpha = if (isLowLevel) pulseAlpha else 0.1f))
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Bottom Status & Threshold Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column {
          Text(
            text = "CURRENT STATUS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Slate400,
            letterSpacing = 0.6.sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          if (isLowLevel) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Rose600)
              )
              Text(
                text = if (isAcknowledged) "ALERT (ACKNOWLEDGED)" else "LOW LEVEL ALERT",
                color = Rose600,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          } else {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Emerald500)
              )
              Text(
                text = "NORMAL DRIP FLOW",
                color = Emerald700,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "THRESHOLD",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Slate400,
            letterSpacing = 0.6.sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = String.format("%.1f%%", threshold),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Slate900
          )
        }
      }
    }
  }
}

@Composable
fun SimulationControlCard(
  fluidLevel: Float,
  onLevelChange: (Float) -> Unit,
  threshold: Float,
  onThresholdChange: (Float) -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
    color = Color.White,
    shadowElevation = 0.5.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "FLUID LEVEL SIMULATION",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = Slate400,
          letterSpacing = 0.6.sp
        )
        Text(
          text = "${fluidLevel.toInt()}%",
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          color = Indigo600
        )
      }

      Slider(
        value = fluidLevel,
        onValueChange = onLevelChange,
        valueRange = 0f..100f,
        colors = SliderDefaults.colors(
          thumbColor = Indigo600,
          activeTrackColor = Indigo600,
          inactiveTrackColor = Slate100
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("fluid_level_slider")
      )

      Spacer(modifier = Modifier.height(4.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ALARM THRESHOLD LIMIT",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = Slate400,
          letterSpacing = 0.6.sp
        )
        Text(
          text = "${threshold.toInt()}%",
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          color = Slate700
        )
      }

      Slider(
        value = threshold,
        onValueChange = onThresholdChange,
        valueRange = 5f..40f,
        colors = SliderDefaults.colors(
          thumbColor = Slate700,
          activeTrackColor = Slate700,
          inactiveTrackColor = Slate100
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("threshold_slider")
      )
    }
  }
}

@Composable
fun StatCard(
  label: String,
  value: String,
  subtitle: String,
  subtitleColor: Color
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, Slate100, RoundedCornerShape(18.dp)),
    color = Color.White,
    shadowElevation = 0.5.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.6.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = Slate900
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = subtitleColor
      )
    }
  }
}

@Composable
fun DiagnosticBanner(
  isLowLevel: Boolean,
  isAcknowledged: Boolean,
  onAcknowledge: () -> Unit
) {
  val bannerBg = if (isLowLevel && !isAcknowledged) Rose600 else Indigo900

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp)),
    color = bannerBg,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = if (isLowLevel && !isAcknowledged) "Action Required" else "System Diagnostic",
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = when {
            isLowLevel && !isAcknowledged -> "Buzzer Active (75 dBA) • Tap to Mute"
            isLowLevel && isAcknowledged -> "Alarm Muted • Attending Patient"
            else -> "Hardware Sync: 100% • 1D Gradient OK"
          },
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.15f))
          .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
          .clickable {
            if (isLowLevel) {
              onAcknowledge()
            }
          }
          .testTag("action_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isLowLevel && !isAcknowledged) Icons.Filled.VolumeOff else Icons.Filled.Settings,
          contentDescription = "Diagnostic / Mute Action",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun DocsScreen() {
  val docsList = listOf(
    "01_PRD.md" to "Product Requirements & Scope Boundaries",
    "02_TRD.md" to "Technical Specifications & Priority Ratings",
    "03_SYSTEM_ARCHITECTURE.md" to "Decoupled 2-Tier Architecture & FSM",
    "04_HARDWARE_ARCHITECTURE.md" to "ESP32-CAM Pinout, Schematics & BOM",
    "05_SOFTWARE_ARCHITECTURE.md" to "Clean Architecture & Android Foreground Service",
    "06_BLE_PROTOCOL.md" to "GATT Service, 10-byte Packets & CRC-8",
    "07_LEVEL_DETECTION_SPEC.md" to "1D Spatial Intensity Gradient Mathematics",
    "08_NOVELTY_ANALYSIS.md" to "Prior Art & INSPIRE-MANAK Alignment",
    "09_TEST_PLAN.md" to "5 Verification Protocols & Test Tables",
    "10_INNOVATION_NOTEBOOK.md" to "Iterative Engineering Log & Field Insights",
    "11_TRACEABILITY_MATRIX.md" to "PRD ↔ TRD ↔ Code ↔ Test Suites Traceability",
    "12_SOURCE_REGISTER.md" to "Classified Evidence & Citation Repository",
    "13_OPEN_QUESTIONS.md" to "Active Technical Questions & Risk Register"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        text = "SYSTEM ENGINEERING REPOSITORY",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.8.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "13 Master Specifications Baseline",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Slate900
      )
      Spacer(modifier = Modifier.height(6.dp))
    }

    items(docsList) { (file, title) ->
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .border(1.dp, Slate100, RoundedCornerShape(16.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Indigo50),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.Description,
              contentDescription = null,
              tint = Indigo600,
              modifier = Modifier.size(18.dp)
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = file,
              fontFamily = FontFamily.Monospace,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Indigo700
            )
            Text(
              text = title,
              fontSize = 13.sp,
              color = Slate500
            )
          }
        }
      }
    }
  }
}

@Composable
fun TestsScreen() {
  val tests = listOf(
    Triple("TEST-SUITE-01", "Static Meniscus Pixel Calibration", "PASS (±0.4 px MTF)"),
    Triple("TEST-SUITE-02", "Dynamic Continuous Drain Alert", "PASS (1.8s Latency)"),
    Triple("TEST-SUITE-03", "Ambient Light Invariance (4 Lux Levels)", "PASS (30-1200 Lux)"),
    Triple("TEST-SUITE-04", "BLE Telemetry Packet Loss & CRC Check", "PASS (0 CRC Errors)"),
    Triple("TEST-SUITE-05", "Power Consumption & Battery Autonomy", "PASS (68 mA @ 3.3V)")
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "VERIFICATION TEST SUITES",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.8.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Experimental Protocol Results",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Slate900
      )
      Spacer(modifier = Modifier.height(4.dp))
    }

    items(tests) { (id, name, result) ->
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .border(1.dp, Slate100, RoundedCornerShape(16.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = id,
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Indigo600
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Emerald500,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "VERIFIED",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Emerald700
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate900
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Criterion: $result",
            fontSize = 12.sp,
            color = Slate500
          )
        }
      }
    }
  }
}

@Composable
fun ProfileCalibrationScreen(
  currentLevel: Float,
  threshold: Float,
  onThresholdChange: (Float) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "OPTICAL CALIBRATION & PROFILE",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate400,
        letterSpacing = 0.8.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "ESP32-CAM ROI Setup",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Slate900
      )
      Spacer(modifier = Modifier.height(4.dp))
    }

    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = "1D Intensity Gradient Parameters",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate900
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "ROI Bounding Box:", fontSize = 13.sp, color = Slate500)
            Text(text = "X=75, Y=10, W=10, H=100", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Slate900)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Meniscus Peak Y:", fontSize = 13.sp, color = Slate500)
            Text(text = "${(100 - currentLevel * 0.9).toInt()} px", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Indigo600)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Debounce Window:", fontSize = 13.sp, color = Slate500)
            Text(text = "3 Cycles (6.0s)", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Slate900)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Buzzer SPL Level:", fontSize = 13.sp, color = Slate500)
            Text(text = "78.4 dBA @ 10cm", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Emerald700)
          }
        }
      }
    }

    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Indigo950),
        color = Indigo950
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Text(
            text = "Safety & Legal Notice",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "EDUCATIONAL DEMONSTRATION PROTOTYPE — NOT FOR CLINICAL USE. This system functions as an auxiliary optical inspection monitor and does not substitute qualified medical supervision.",
            fontSize = 11.sp,
            lineHeight = 16.sp,
            color = Slate300
          )
        }
      }
    }
  }
}

@Composable
fun BottomNavBar(
  currentTab: NavigationTab,
  onTabSelected: (NavigationTab) -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = Color.White,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavigationTab.values().forEach { tab ->
        val isSelected = currentTab == tab
        val tintColor = if (isSelected) Indigo600 else Slate400

        Column(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onTabSelected(tab) }
            .padding(vertical = 6.dp)
            .testTag("nav_tab_${tab.name.lowercase()}"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = tintColor,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = tab.label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = tintColor,
            maxLines = 1,
            letterSpacing = 0.2.sp
          )
        }
      }
    }
  }
}

