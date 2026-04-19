# Ameen Project: Adaptive Surveying System

**Objective:** A high-precision agricultural surveying and navigation tool for Android. 
It enables users to define a "Straight Line" (Point A to Point B) across large fields and maintain a straight path using real-time GPS correction and a visual "Lightbar" guidance system.

## 1. Operation Modes
The system MUST support a state-managed switch between two primary modes:

### A. STANDALONE_MODE (Single Device)
- **Target:** Quick surveys or single-user operation.
- **Logic:** - User "Locks" Point A: App collects 30 samples at 1Hz and calculates the centroid.
  - User "Locks" Point B: App collects 30 samples at 1Hz and calculates the centroid.
  - Navigation follows the static line AB using raw GPS smoothed by a Compass/Gyro fusion.

### B. TEAM_MODE (Dual Device / DGPS Emulation)
- **Role: BASE STATION (Anchor):**
  - Stays 100% stationary at Point A.
  - Calculates real-time **Drift Vector**: `(Current_Raw_Lat/Lon - Initial_Locked_Lat/Lon)`.
  - Pushes this Drift Vector to Firebase RTDB every second.
- **Role: ROVER (Navigation):**
  - Receives Drift Vector from Base via Firebase.
  - Applies **Correction**: `Corrected_Pos = Local_Raw_Pos - Base_Drift`.
  - Calculates Cross-Track Error (XTE) based on corrected coordinates.

## 2. Tech Stack & Infrastructure
- **Language:** Kotlin (1.9+) with Jetpack Compose (Material 3).
- **Persistence:** - **Local:** Room DB for session management and point history.
  - **Remote:** Firebase Realtime Database (RTDB) for low-latency drift syncing.
- **Location Engine:** - `FusedLocationProviderClient` (High Accuracy).
  - `GnssStatus.Callback` to detect L1/L5 frequency bands.
- **Architecture:** Clean Architecture with MVVM. 
- **Networking:** Firebase `setPersistenceEnabled(true)` to handle 4G fluctuations in rural fields.

## 3. Engineering Constraints & Math
- **Precision:** Use `java.lang.Math` with `Double` precision. **STRICTLY PROHIBIT** casting to `Float` for Latitude/Longitude.
- **Cross-Track Error (XTE) Formula:** - $d_{xtk} = \arcsin(\sin(\text{DistanceToOrigin}/R) \cdot \sin(\text{BearingToOrigin} - \text{BearingAB})) \cdot R$
- **Sensor Fusion:** - Use `Sensor.TYPE_ROTATION_VECTOR` for orientation. 
  - If ground speed is < 0.5 m/s, the UI must prioritize the Magnetometer over GPS Heading.
- **NavIC Support:** Explicitly prioritize NavIC constellations for improved accuracy in the Indian subcontinent.

## 4. UI/UX Specifications
- **Mode Toggle:** Clear landing screen to choose between Standalone, Host (Base), or Join (Rover).
- **The Lightbar:** - A central horizontal meter. 
  - Center Green ($<20\text{cm}$ deviation).
  - Progressive Red segments for Left/Right deviation.
- **Locking Animation:** A progress bar/circle for the 30-second averaging process.

## 5. Data & Export
- **KML Exporter:** Generate a standard `.kml` file containing:
  - `<Point>` for A and B.
  - `<LineString>` for the intended path.
  - `<LineString>` for the actual walked path.
- **CSV Exporter:** Log `timestamp, raw_lat, raw_lon, corrected_lat, corrected_lon, xte, gnss_band`.

## 6. Directory & Naming Conventions
- **`domain.math`**: All Geo-spatial and XTE logic.
- **`data.location`**: LocationServices and GnssStatus handling.
- **`data.firebase`**: DGPS sync and session handshake.
- **`presentation.components`**: Custom Compose views for the Lightbar and Satellite Status.

***
