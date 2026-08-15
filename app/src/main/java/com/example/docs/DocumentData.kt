package com.example.docs

data class DocMetadata(
  val id: String,
  val filename: String,
  val title: String,
  val subtitle: String,
  val version: String,
  val status: String,
  val category: DocCategory,
  val iconName: String,
  val summary: String,
  val sections: List<DocSection>
)

enum class DocCategory(val label: String) {
  REQUIREMENTS("Requirements"),
  ARCHITECTURE("Architecture"),
  PROTOCOLS("Protocols & Vision"),
  TESTING("Testing & Validation"),
  RESEARCH("Research & Governance")
}

data class DocSection(
  val title: String,
  val content: String = "",
  val tableData: List<List<String>>? = null,
  val codeSnippet: String? = null
)

object DocumentRepository {
  val documents: List<DocMetadata> = listOf(
    DocMetadata(
      id = "DOC-01",
      filename = "01_PRD.md",
      title = "Product Requirements Document (PRD)",
      subtitle = "Scope, Boundaries, User Scenarios & MVP Gate",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.REQUIREMENTS,
      iconName = "Description",
      summary = "Defines the educational/bench prototype scope, non-contact safety boundaries, nurse user journeys, functional requirements (FR-01 to FR-06), and strict non-goals.",
      sections = listOf(
        DocSection(
          title = "1. Product Identity & Purpose",
          content = "The SMART-IV MONITOR is an external, non-invasive fluid level monitoring and alerting system for hanging drip containers. It investigates how a low-cost optical setup on an ESP32 can detect the descending fluid meniscus and emit decoupled audio-visual and BLE alerts before reservoir depletion."
        ),
        DocSection(
          title = "2. Critical Problem Statement",
          content = "In rural primary health centers (PHCs), nurse-to-patient disparities lead to delayed bottle changeovers. When an IV runs dry, venous pressure (10-15 mmHg) causes retrograde blood flow, cannula clotting, and patient distress. Commercial volumetric pumps cost ₹40,000–₹1,50,000, creating a steep cost barrier."
        ),
        DocSection(
          title = "3. Strict System Boundaries & Safety Rules",
          content = "SAFE BENCH ZONE: Mounts externally on IV stand with 100mm air gap to mock bottle. Zero contact with fluid, needle, or tubing. PROHIBITED ZONE: Never connected to human/animal; no motorized pinch valves; no medical certification claimed."
        ),
        DocSection(
          title = "4. Functional Requirements Summary",
          tableData = listOf(
            listOf("Req ID", "Description", "Target Metric"),
            listOf("FR-01", "Continuous Level Sampling", "Every 2.0s ± 0.1s"),
            listOf("FR-02", "Local Audio-Visual Alert", "≥75 dBA at 10cm, 2Hz Red LED"),
            listOf("FR-03", "Wireless Telemetry Stream", "BLE GATT 10-byte binary packet"),
            listOf("FR-04", "Remote Mobile Alert", "Android AlarmManager notification"),
            listOf("FR-05", "Interactive Calibration", "Full (100%) and Zero (0%) registration"),
            listOf("FR-06", "Alarm Mute / Acknowledge", "Hardware button & In-app mute")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-02",
      filename = "02_TRD.md",
      title = "Technical Requirements Document (TRD)",
      subtitle = "Engineering Specifications, Pinouts & Verifications",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.REQUIREMENTS,
      iconName = "Engineering",
      summary = "Low-level engineering specifications categorized by HW, OPT, BLE, APP, PWR, and SAF with prioritized verification protocols.",
      sections = listOf(
        DocSection(
          title = "1. System & Hardware Specifications",
          tableData = listOf(
            listOf("Req ID", "Specification", "Priority", "Verification"),
            listOf("REQ-HW-001", "ESP32 Core at ≤160MHz with PSRAM", "CRITICAL", "Boot log inspection"),
            listOf("REQ-HW-002", "5.0V USB / 3.7V Li-ion + LDO 3.3V", "CRITICAL", "Oscilloscope probe"),
            listOf("REQ-HW-003", "470µF Bulk capacitor on 3.3V rail", "HIGH", "Schematic & transient probe"),
            listOf("REQ-HW-004", "Tactile Button with HW interrupt (GPIO 15)", "HIGH", "50ms debounce test"),
            listOf("REQ-HW-005", "Active Buzzer via 2N2222 NPN (GPIO 12)", "HIGH", "SPL meter ≥75 dBA"),
            listOf("REQ-HW-006", "Dual LED (Green GPIO 13, Red GPIO 14)", "MEDIUM", "Visual & current check")
          )
        ),
        DocSection(
          title = "2. Optical & BLE Requirements",
          tableData = listOf(
            listOf("Req ID", "Specification", "Priority", "Verification"),
            listOf("REQ-OPT-001", "OV2640 QQVGA (160x120) Grayscale", "CRITICAL", "Buffer profiling"),
            listOf("REQ-OPT-002", "1D Vertical Gradient across ROI", "HIGH", "Algorithm plot"),
            listOf("REQ-OPT-003", "3-Cycle Confirmation Debounce", "HIGH", "Vibration step drain"),
            listOf("REQ-BLE-001", "BLE Peripheral 'SMART-IV-XXXX'", "CRITICAL", "nRF Connect inspection"),
            listOf("REQ-BLE-003", "Compact 10-byte binary packet with CRC-8", "HIGH", "Byte stream test")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-03",
      filename = "03_SYSTEM_ARCHITECTURE.md",
      title = "System Architecture Document",
      subtitle = "Decoupled 2-Tier Architecture & Deterministic FSM",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.ARCHITECTURE,
      iconName = "AccountTree",
      summary = "Defines Tier 1 (ESP32 Edge Core) and Tier 2 (Mobile Terminal), 4-modality sensing trade-off analysis, and deterministic 10-state FSM.",
      sections = listOf(
        DocSection(
          title = "1. Decoupled Two-Tier Architecture",
          content = "Tier 1: ESP32 Edge Core with OV2640 camera, local piezobuzzer, dual LED, tactile acknowledge switch, and power management. Operates autonomously if BLE is absent. Tier 2: Android companion mobile app for live visual gauge, configuration, calibration, and high-priority alarms."
        ),
        DocSection(
          title = "2. Transduction Modality Evaluation",
          tableData = listOf(
            listOf("Modality", "Pros", "Cons", "Decision"),
            listOf("Option A: Optical Vision", "Non-contact, continuous %, highly demonstrative", "Requires fixed focal alignment", "SELECTED (Primary)"),
            listOf("Option B: Load Cell / HX711", "Linear, ambient light immune", "Swing noise, cable drag", "BENCHMARK STANDARD"),
            listOf("Option C: IR Optocoupler Array", "Low power, simple GPIO", "Discrete coarse steps (25%)", "REJECTED for MVP"),
            listOf("Option D: Ultrasonic Ranging", "Continuous level", "Breaches bottle seal, curved wall noise", "REJECTED (Invasive)")
          )
        ),
        DocSection(
          title = "3. Deterministic Finite State Machine (FSM)",
          content = "States: 1. BOOT/SENSOR_INIT -> 2. ADVERTISING -> 3. CONNECTED -> 4. CALIBRATING -> 5. MONITORING -> 6. STANDALONE_MONITORING -> 7. THRESHOLD_REACHED (3 cycles) -> 8. ALERTING -> 9. ACKNOWLEDGED -> 10. ERROR_STATE."
        )
      )
    ),
    DocMetadata(
      id = "DOC-04",
      filename = "04_HARDWARE_ARCHITECTURE.md",
      title = "Hardware Architecture & Schematics",
      subtitle = "BOM Inventory, Pin Hazard Audit & Bench Rig",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.ARCHITECTURE,
      iconName = "Memory",
      summary = "Complete BOM under ₹1,250 INR, ESP32-CAM pin hazard isolation, NPN buzzer driver schematic, LED circuits, and optical bench test jig.",
      sections = listOf(
        DocSection(
          title = "1. Bill of Materials (BOM) & Cost Audit",
          tableData = listOf(
            listOf("Component", "Qty", "Cost (INR)", "Status"),
            listOf("ESP32-CAM AI-Thinker Module + OV2640", "1", "₹0", "ALREADY OWNED"),
            listOf("ESP32 DevKit V1 30-Pin", "1", "₹0", "ALREADY OWNED"),
            listOf("5V Active Magnetic Buzzer 2.4kHz", "1", "₹35", "PURCHASE REQUIRED"),
            listOf("2N2222 NPN Transistor + 1N4148 Diode", "1 set", "₹12", "PURCHASE REQUIRED"),
            listOf("Red Alert LED + Green Monitor LED + Resistors", "2 sets", "₹18", "PURCHASE REQUIRED"),
            listOf("6mm Tactile Button + 470µF Capacitor", "1 set", "₹20", "PURCHASE REQUIRED"),
            listOf("Breadboard + Jumper Wires + FTDI Adapter", "1 set", "₹350", "PURCHASE REQUIRED"),
            listOf("Mock 500mL Bottle + Diffuse LED Backlight", "1 set", "₹160", "PURCHASE REQUIRED"),
            listOf("Retort Stand + Acrylic Standoff Clamp", "1 set", "₹380", "PURCHASE REQUIRED")
          )
        ),
        DocSection(
          title = "2. ESP32-CAM Pin Allocation & Hazards",
          tableData = listOf(
            listOf("Pin", "Internal Assignment", "Smart-IV Function", "Circuit Requirement"),
            listOf("GPIO 0", "Camera XCLK / Boot", "Flashing Jumper Only", "Reserved during run"),
            listOf("GPIO 1/3", "UART0 TX/RX", "Console & Programming", "Connect to FTDI"),
            listOf("GPIO 12", "Free GPIO (Must be LOW at boot)", "Buzzer Driver (Active HIGH)", "1kΩ to 2N2222 Base"),
            listOf("GPIO 13", "Free GPIO", "Green Monitoring LED", "220Ω series resistor"),
            listOf("GPIO 14", "Free GPIO", "Red Alert LED", "220Ω series resistor"),
            listOf("GPIO 15", "Free GPIO (Internal Pull-Up)", "Acknowledge Button (Active LOW)", "Button to GND"),
            listOf("GPIO 16", "PSRAM CS", "DO NOT USE", "Reserved for 8MB PSRAM")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-05",
      filename = "05_SOFTWARE_ARCHITECTURE.md",
      title = "Software Architecture & Android Integration",
      subtitle = "Clean Architecture, Bloc/Cubit & Foreground Services",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.ARCHITECTURE,
      iconName = "Code",
      summary = "Evaluates mobile frameworks, defines Clean Architecture layers (Presentation, Application, Domain, Infrastructure), BLE state management, and Android 12+ permissions.",
      sections = listOf(
        DocSection(
          title = "1. Mobile Framework Evaluation",
          tableData = listOf(
            listOf("Framework", "BLE Integration", "Dev Velocity", "Alarm Reliability", "Verdict"),
            listOf("Flutter + flutter_blue_plus", "High", "Very High", "Strong", "SELECTED"),
            listOf("Native Android (Kotlin/Compose)", "Native", "High", "Native", "PARALLEL STANDARD"),
            listOf("React Native", "Medium (Bridge lock)", "Medium", "Moderate", "REJECTED")
          )
        ),
        DocSection(
          title = "2. Clean Architecture Layering",
          content = "Presentation (Dashboard, Scan, Calibration, Dialogs, Gauges) -> Application (MonitorService, ThresholdManager, NotificationEngine) -> Domain (IvDevice, TelemetryData, CalibrationProfile, IBleRepository) -> Infrastructure (BleScanner, ConnectionManager, PacketCodec, LocalStorage, AndroidAlarmService)."
        )
      )
    ),
    DocMetadata(
      id = "DOC-06",
      filename = "06_BLE_PROTOCOL.md",
      title = "BLE GATT Protocol Specification",
      subtitle = "Custom UUIDs, 10-Byte Binary Packet & CRC-8",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.PROTOCOLS,
      iconName = "Bluetooth",
      summary = "Details GATT service hierarchy, custom 128-bit project UUIDs, binary packet byte formats, bitfield flags, big-endian payloads, and CRC-8-CCITT algorithm.",
      sections = listOf(
        DocSection(
          title = "1. Custom 128-Bit GATT UUIDs",
          tableData = listOf(
            listOf("Attribute", "UUID", "Properties"),
            listOf("SMART-IV Primary Service", "0000FF00-1212-EFDE-1523-785FEABCD123", "Primary Service"),
            listOf("Telemetry Characteristic", "0000FF01-1212-EFDE-1523-785FEABCD123", "NOTIFY, READ"),
            listOf("Command/Control Char", "0000FF02-1212-EFDE-1523-785FEABCD123", "WRITE, WRITE_NO_RESP"),
            listOf("Calibration Char", "0000FF03-1212-EFDE-1523-785FEABCD123", "WRITE, READ")
          )
        ),
        DocSection(
          title = "2. 10-Byte Telemetry Packet Structure",
          tableData = listOf(
            listOf("Byte Index", "Field Name", "Type / Range", "Description"),
            listOf("Byte 0", "OpCode", "uint8 (0x01)", "Telemetry update opcode"),
            listOf("Byte 1", "Sequence ID", "uint8 (0..255)", "Rolling packet sequence"),
            listOf("Byte 2", "Fluid Level %", "uint8 (0..100)", "Estimated fluid percentage"),
            listOf("Byte 3", "State Flags", "uint8 bitfield", "Bit 0: Low, Bit 1: Ack, Bit 2: Valid, Bit 3: Batt Low"),
            listOf("Bytes 4-5", "Battery Voltage", "uint16 BE (mV)", "e.g., 3840 mV = 3.84V"),
            listOf("Bytes 6-7", "Meniscus Row Y", "uint16 BE (px)", "Pixel row coordinate"),
            listOf("Byte 8", "Optical SNR", "uint8 (dB)", "Signal-to-noise ratio"),
            listOf("Byte 9", "CRC-8 Checksum", "uint8 (0..255)", "CRC-8-CCITT over Bytes 0-8")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-07",
      filename = "07_LEVEL_DETECTION_SPEC.md",
      title = "Level Detection & Gradient Math Specification",
      subtitle = "1D Spatial Gradient, Sub-Pixel Parabolic Peak & Transillumination",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.PROTOCOLS,
      iconName = "Timeline",
      summary = "Formulates 1D spatial intensity gradient dI/dy, sub-pixel parabolic peak interpolation, SNR metrics, optical transillumination geometry, and 3-cycle temporal debouncing.",
      sections = listOf(
        DocSection(
          title = "1. Transillumination Optical Physics",
          content = "A translucent plastic bottle acts as a cylindrical fluid lens. Light traversing the air cavity experiences lower refraction than light through the liquid column. The liquid-air meniscus creates a dark refraction boundary, producing a steep step in the vertical intensity profile."
        ),
        DocSection(
          title = "2. 1D Central Difference Gradient Formula",
          content = "Average column intensity: I_avg(y) = (1/W) * sum(I(x, y), x in ROI). Discrete 1D spatial gradient: G(y) = (I_avg(y+1) - I_avg(y-1)) / 2. Parabolic peak interpolation: y* = y0 + (G(y0-1) - G(y0+1)) / (2 * (G(y0-1) - 2*G(y0) + G(y0+1)))."
        ),
        DocSection(
          title = "3. Confidence Metric & SNR",
          content = "Optical Signal-to-Noise Ratio: SNR_dB = 20 * log10(|G_max| / sigma_noise). If SNR < 12 dB, the measurement is flagged as UNRELIABLE and temporal filter ignores the sample."
        )
      )
    ),
    DocMetadata(
      id = "DOC-08",
      filename = "08_NOVELTY_ANALYSIS.md",
      title = "Novelty, Prior Art & MANAK Alignment",
      subtitle = "Commercial Benchmarking, Patents & Combinatorial Novelty",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.RESEARCH,
      iconName = "Lightbulb",
      summary = "Exhaustively benchmarks commercial devices (Monidrop, DripAssist, Volumetric Pumps), patents, and student prototypes. Demonstrates alignment with DST/NIF criteria.",
      sections = listOf(
        DocSection(
          title = "1. Commercial Benchmark Comparison",
          tableData = listOf(
            listOf("Device", "Principle", "Est. Cost", "Portability", "Fluid Contact"),
            listOf("Monidrop (Clinipower)", "IR Drip Chamber Counter", "> €400 (₹35k+)", "Compact", "Non-invasive clamp"),
            listOf("DripAssist (Shift Labs)", "IR Drop Interval Sensor", "~ $395 (₹32k+)", "Battery", "Non-invasive clamp"),
            listOf("Volumetric Pumps (B.Braun)", "Motorized Peristalsis", "> ₹80,000", "Stationary / AC", "Special tube set"),
            listOf("SMART-IV MONITOR (Ours)", "ESP32 1D Edge Vision", "< ₹1,250 INR", "Ultra-Portable", "Zero Fluid Contact")
          )
        ),
        DocSection(
          title = "2. INSPIRE-MANAK Criteria Scoring",
          tableData = listOf(
            listOf("MANAK Criterion", "Project Fulfillment", "Defense Statement"),
            listOf("Novelty", "HIGH (Combinatorial)", "First <₹1.5k 1D edge-vision continuous container level monitor with dual failsafe"),
            listOf("Social Applicability", "VERY HIGH", "Directly targets high patient-to-nurse ratios in underfunded rural PHCs"),
            listOf("User Friendliness", "HIGH", "External toolless clamp + smartphone 2-step wizard"),
            listOf("Comparative Advantage", "VERY HIGH", "96% cost reduction vs commercial drop counters; no fluid pathway alteration")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-09",
      filename = "09_TEST_PLAN.md",
      title = "Experimental Validation & Test Plan",
      subtitle = "5 Structured Experimental Protocols & Data Tables",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.TESTING,
      iconName = "Science",
      summary = "Detailed experimental test protocols for static meniscus calibration, dynamic continuous drain repeatability, ambient light invariance, BLE packet loss, and power autonomy.",
      sections = listOf(
        DocSection(
          title = "1. Master Test Suite Matrix",
          tableData = listOf(
            listOf("Suite ID", "Target Objective", "Independent Var", "Pass Criterion"),
            listOf("TEST-SUITE-01", "Static Meniscus Pixel Calibration", "Fluid Volume (0-500mL)", "Linearity R² ≥ 0.98, error ≤ ±10mL"),
            listOf("TEST-SUITE-02", "Dynamic Drain Repeatability", "10 Consecutive Drains", "Alert trigger volume σ ≤ 8mL"),
            listOf("TEST-SUITE-03", "Ambient Light Invariance", "Lux (30 to 1200 Lux)", "Zero false triggers across 4 light conditions"),
            listOf("TEST-SUITE-04", "BLE Telemetry & CRC-8 Check", "Distance (1m to 12m)", "Zero CRC errors, Packet loss < 2% at 8m"),
            listOf("TEST-SUITE-05", "Power Consumption & Battery", "Operating Mode", "Active < 160mA, Battery life > 12 hours")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-10",
      filename = "10_INNOVATION_NOTEBOOK.md",
      title = "Innovation Notebook & Engineering Log",
      subtitle = "Clinical Observations, Failed Prototypes & Evolution",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.RESEARCH,
      iconName = "MenuBook",
      summary = "Engineering diary documenting the 4 development phases: clinical observation, failure of invasive floats and swinging load cells, edge vision breakthrough, and failsafe integration.",
      sections = listOf(
        DocSection(
          title = "Phase 1: Observation & Invasive Float Failure",
          content = "Observed rural PHC nurse managing 14 beds alone. Initial idea: magnetic float inside bottle. FAILED: Breaches sterile barrier and risks introducing endotoxins. Principle established: ZERO FLUID CONTACT."
        ),
        DocSection(
          title = "Phase 2: Load Cell Stand Oscillations",
          content = "Built S-beam load cell under hanging hook. FAILED: Whenever stand was wheeled or bumped, pendulum swinging caused mass readings to oscillate by ±45g, triggering false alarms. Required mechanical redesign."
        ),
        DocSection(
          title = "Phase 3: ESP32-CAM 1D Edge Vision Breakthrough",
          content = "Discovered that cylindrical bottle with diffuse LED backlighting forms a crisp refractive dark meniscus line. 1D vertical central difference derivative extracts the peak coordinate in <12ms on ESP32."
        ),
        DocSection(
          title = "Phase 4: Decoupled Failsafe Redundancy",
          content = "Integrated standalone hardware buzzer + dual LED on ESP32 GPIOs with a 3-cycle debounce window to ensure alerts sound even if BLE disconnects."
        )
      )
    ),
    DocMetadata(
      id = "DOC-11",
      filename = "11_TRACEABILITY_MATRIX.md",
      title = "Requirements Traceability Matrix (RTM)",
      subtitle = "Bi-directional Mapping: PRD ↔ TRD ↔ Code ↔ Test Suites",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.REQUIREMENTS,
      iconName = "FactCheck",
      summary = "Complete bi-directional traceability linking Product Requirements (PRD), Technical Requirements (TRD), Architecture Modules, Components, and Test Verification Protocols.",
      sections = listOf(
        DocSection(
          title = "1. Master Traceability Register",
          tableData = listOf(
            listOf("PRD ID", "TRD ID", "Architecture Module", "Component", "Test Suite"),
            listOf("FR-01", "REQ-OPT-001, 002", "Tier 1: Edge Sensing Core", "ESP32-CAM OV2640 Driver", "TEST-SUITE-01"),
            listOf("FR-02", "REQ-HW-004, 005, 006", "Tier 1: Local Transducer", "Active Buzzer, Red LED", "TEST-SUITE-02, 05"),
            listOf("FR-03", "REQ-BLE-001, 003", "Tier 1 & 2: BLE Protocol", "NimBLE & PacketCodec", "TEST-SUITE-04"),
            listOf("FR-04", "REQ-APP-004, BLE-002", "Tier 2: Alarm Engine", "AndroidAlarmService", "TEST-SUITE-02"),
            listOf("FR-05", "REQ-OPT-005, APP-005", "Tier 2: Calibration Wizard", "CalibrationScreen, NVS", "TEST-SUITE-01"),
            listOf("FR-06", "REQ-HW-004, BLE-003", "Tier 1 & 2: FSM Control", "Push Button (GPIO 15)", "TEST-SUITE-02"),
            listOf("NFR-01", "REQ-BLE-004, APP-004", "End-to-End Loop", "Gradient Filter + GATT", "TEST-SUITE-02"),
            listOf("NFR-02", "REQ-PWR-001, 002", "Power Management", "470µF Cap + Duty Cycle", "TEST-SUITE-05"),
            listOf("NFR-03", "REQ-OPT-002, 003", "Gradient Extraction", "1D Central Difference", "TEST-SUITE-02"),
            listOf("NFR-04", "REQ-HW-001, 002", "Bill of Materials", "Hobbyist Parts (₹800-₹1.2k)", "BOM Audit"),
            listOf("SAF-01", "REQ-SAF-001, 003", "Mechanical Standoff", "Non-contact clamp (100mm)", "Physical Inspection")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-12",
      filename = "12_SOURCE_REGISTER.md",
      title = "Source Register & Evidence Repository",
      subtitle = "Classified Citations (Official, Academic, Patents, Technical)",
      version = "v0.2 Baseline",
      status = "Approved Specification",
      category = DocCategory.RESEARCH,
      iconName = "Bookmark",
      summary = "Classifies all citations under standardized evidence tags ([OFFICIAL], [ACADEMIC], [TECHNICAL], [COMMERCIAL], [PATENT], [INFERENCE], [ASSUMPTION]).",
      sections = listOf(
        DocSection(
          title = "1. Evidence Category Taxonomy",
          content = "[OFFICIAL]: Statutory health agencies (DST, NIF, WHO). [ACADEMIC]: Peer-reviewed journals (IEEE, MDPI). [TECHNICAL]: Manufacturer datasheets (Espressif, OmniVision). [COMMERCIAL]: Device manuals (Monidrop, DripAssist). [PATENT]: Published patent applications. [INFERENCE]: Physics deductions."
        ),
        DocSection(
          title = "2. Master Citations Register",
          tableData = listOf(
            listOf("Ref ID", "Source Title", "Issuing Body", "Category", "Key Claim"),
            listOf("SRC-01", "INSPIRE Awards MANAK Guidelines", "DST / NIF India", "[OFFICIAL]", "₹10,000 prototype award & selection criteria"),
            listOf("SRC-02", "ESP32 Technical Reference Manual", "Espressif Systems", "[TECHNICAL]", "Xtensa LX6 architecture & current profiles"),
            listOf("SRC-03", "OV2640 CMOS Sensor Datasheet", "OmniVision", "[TECHNICAL]", "QQVGA grayscale mode & SCCB control"),
            listOf("SRC-04", "Bluetooth Core Spec 5.0", "Bluetooth SIG", "[TECHNICAL]", "GATT, ATT 23-byte MTU & CCCD descriptors"),
            listOf("SRC-05", "Android BLE & Foreground Services", "Google / AOSP", "[TECHNICAL]", "Android 12+ permissions & notification channels"),
            listOf("SRC-06", "Non-Invasive Infusion Monitoring", "MDPI Sensors / IEEE", "[ACADEMIC]", "Drop counting vs container mass sensing"),
            listOf("SRC-07", "Monidrop IV Drip Monitor Manual", "Clinipower Ltd", "[COMMERCIAL]", "Commercial drop counter (>€400 cost)"),
            listOf("SRC-08", "DripAssist Infusion Monitor", "Shift Labs", "[COMMERCIAL]", "Gravity infusion monitor ($395 cost)"),
            listOf("SRC-09", "Normal Peripheral Venous Pressure", "Clinical Anesthesia", "[ACADEMIC]", "10-15 mmHg venous pressure & retrograde flow"),
            listOf("SRC-10", "Patent IN201841001234A", "Indian Patent Office", "[PATENT]", "Load cell under hook with GSM SMS alerts")
          )
        )
      )
    ),
    DocMetadata(
      id = "DOC-13",
      filename = "13_OPEN_QUESTIONS.md",
      title = "Open Technical Questions & Risk Register",
      subtitle = "5x5 Risk Matrix, Technical Hypotheses & Action Plan",
      version = "v0.2 Baseline",
      status = "Active Working Register",
      category = DocCategory.RESEARCH,
      iconName = "ReportProblem",
      summary = "Tracks open technical investigations (OTQ-01 to OTQ-05), severity/likelihood risk matrix (RSK-01 to RSK-05), and immediate engineering action steps.",
      sections = listOf(
        DocSection(
          title = "1. Open Technical Questions (OTQ)",
          tableData = listOf(
            listOf("Question ID", "Topic", "Working Hypothesis", "Status"),
            listOf("OTQ-01", "Bottle label stickers in ROI", "Position ROI bounding box in clear vertical strip", "IN PROGRESS"),
            listOf("OTQ-02", "Buzzer inductive flyback spike", "Added 1N4148 diode + 1kΩ base resistor", "RESOLVED"),
            listOf("OTQ-03", "Android battery saver BLE kill", "Foreground service with WakeLock", "IN PROGRESS"),
            listOf("OTQ-04", "Clamp rigidity & thermal drift", "Aluminum laboratory bosshead clamp", "OPEN"),
            listOf("OTQ-05", "FreeRTOS SRAM for QQVGA", "18.75 KB required vs 300 KB available SRAM", "RESOLVED")
          )
        ),
        DocSection(
          title = "2. Risk Severity & Mitigation Register",
          tableData = listOf(
            listOf("Risk ID", "Risk Description", "Severity", "Likelihood", "Mitigation"),
            listOf("RSK-01", "Ambient light glare / fluorescent flicker", "5 (Critical)", "3 (Moderate)", "Fixed diffuse LED backlight + 3-sided black shroud"),
            listOf("RSK-02", "Camera macro lens blur at 10cm", "3 (Moderate)", "3 (Moderate)", "Rotate M7 lens 90° CCW for macro focus"),
            listOf("RSK-03", "BLE packet drop in RF noise", "4 (High)", "2 (Low)", "Local autonomous buzzer + 6s link-loss alarm"),
            listOf("RSK-04", "ESP32 brownout during camera boot", "3 (Moderate)", "1 (Low)", "470µF low-ESR bulk decoupling capacitor")
          )
        )
      )
    )
  )
}
