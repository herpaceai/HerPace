# Research: HerPace Android App Technology Stack

**Date**: 2026-02-10
**Branch**: 001-android-app
**Purpose**: Resolve technical decisions for Android app implementation

## Technology Decisions

### 1. UI Framework: Jetpack Compose

**Decision**: Use **Jetpack Compose** for all UI development

**Rationale**: Jetpack Compose is the current Android standard in 2026, offering declarative UI with excellent Material Design 3 and accessibility support. It integrates seamlessly with Kotlin, reduces boilerplate by eliminating XML layouts, and provides automatic TalkBack integration through semantic properties. The reactive nature simplifies dynamic training plan displays.

**Alternatives Considered**:
- XML Layouts: More mature tooling but deprecated approach for new development
- Hybrid (Compose + XML): Unnecessary complexity for greenfield project

**Implementation Notes**:
- Use Material Design 3 components with dynamic color support
- Leverage semantic modifiers for TalkBack accessibility
- Implement Compose Testing for UI tests

---

### 2. Networking: Retrofit + OkHttp

**Decision**: Use **Retrofit 2.x** with **OkHttp** interceptors

**Rationale**: Retrofit is the industry standard (73% adoption) with mature ecosystem, excellent type safety, and seamless Kotlin coroutines integration. Perfect for consuming the existing HerPace ASP.NET Core REST API. OkHttp interceptors enable JWT token injection for authenticated requests.

**Alternatives Considered**:
- Ktor Client: Better for Kotlin Multiplatform, but HerPace is Android-only
- Volley: Google's library but less feature-rich than Retrofit

**Implementation Notes**:
- Use kotlinx.serialization for JSON parsing (Gson is deprecated)
- Configure OkHttp interceptor for JWT token header injection
- Implement suspend functions for async API calls
- Add retry logic for network failures

---

### 3. Dependency Injection: Hilt

**Decision**: Use **Hilt** for dependency injection

**Rationale**: Hilt provides compile-time safety, 15-20% faster app startup than runtime DI, and seamless Jetpack integration (ViewModel, WorkManager, Navigation). Compile-time dependency graph validation catches errors before release. Critical for fitness app where users expect instant startup.

**Alternatives Considered**:
- Koin: Simpler Kotlin DSL, good for rapid prototyping but runtime DI performance penalty
- Manual DI: No framework overhead but increases boilerplate and error risk

**Implementation Notes**:
- Use @HiltAndroidApp on Application class
- Define modules for API services, repositories, use cases
- Leverage @ViewModelInject for ViewModel injection
- Consider starting with Koin for MVP if team is unfamiliar with Hilt, migrate later

---

### 4. Local Database: Room + SQLCipher

**Decision**: Use **Room** with **SQLCipher** encryption

**Rationale**: Room is the Android standard for local storage with compile-time SQL verification, coroutine/Flow integration, and robust TypeConverter support for complex models (training plans, cycle phases). SQLCipher adds encryption for sensitive cycle tracking and fitness data.

**Alternatives Considered**:
- Realm: Cross-platform but heavier, less Kotlin-idiomatic
- Raw SQLite: No type safety, more boilerplate

**Implementation Notes**:
- Use singleton pattern for database instance
- Implement repository pattern separating UI from data layer
- All database operations via coroutines (off main thread)
- TypeConverters for TrainingPlan, CyclePhase, WorkoutLog entities
- Database versioning with migration strategies from start
- LiveData/Flow for reactive UI updates

**Security**: SQLCipher encrypts cycle tracking data and fitness metrics at rest.

---

### 5. Background Tasks: WorkManager

**Decision**: Use **WorkManager** for notifications and sync tasks

**Rationale**: Modern Android standard for background work with guaranteed execution, excellent battery efficiency, and automatic Doze mode handling. Hilt integration available. Ideal for daily workout reminders that tolerate 15-minute timing flexibility.

**Alternatives Considered**:
- AlarmManager: Only for exact timing (e.g., "6:00 AM sharp"), higher battery drain
- JobScheduler: Deprecated in favor of WorkManager

**Implementation Notes**:
- PeriodicWorkRequest for daily training plan reminders
- OneTimeWorkRequest for data sync tasks
- Constraints: requiresCharging=false, requiresDeviceIdle=false
- Tag work requests for cancellation when user disables notifications
- Exponential backoff retry policy

---

### 6. Fitness Platform Integrations

#### 6.1 Google Fit: Health Connect API

**Decision**: Use **Health Connect API** (Google Fit is deprecated as of May 2024)

**Rationale**: Health Connect is the official replacement for Google Fit, mandated for all new Android development. Provides on-device data storage with privacy-first architecture, available on Android 14+ natively (installable app on Android 9+).

**Alternatives Considered**:
- Google Fit API: Deprecated, no new registrations accepted after May 1, 2024
- Direct device sensor access: No historical data, high battery usage

**Implementation Notes**:
- Required permissions: READ_STEPS, READ_HEART_RATE, READ_SLEEP, READ_EXERCISE, WRITE_EXERCISE
- Standardized schemas for heart rate, steps, sleep, stress
- Import historical fitness data to inform AI training plan generation
- Export completed HerPace workouts to user's health dashboard

#### 6.2 Strava: Strava API v3

**Decision**: Use **Strava API v3** with **OAuth 2.0**

**Rationale**: Official Strava API provides robust workout data access with mobile-optimized OAuth flow using Android Implicit Intents.

**Implementation Notes**:
- OAuth flow via `https://www.strava.com/oauth/mobile/authorize`
- Fallback to mobile web OAuth if Strava app not installed
- Short-lived access tokens + refresh tokens
- Store refresh tokens in EncryptedSharedPreferences
- Always use most recent refresh token (old tokens invalidate immediately)

**Key Endpoints**:
- `GET /athlete/activities`: Fetch completed workouts
- `POST /activities`: Upload HerPace workouts to Strava
- `GET /athlete`: User profile data

**Rate Limits**: 100 requests/15 min, 1000 requests/day

#### 6.3 Garmin: Garmin Connect Developer APIs

**Decision**: Use **Garmin Connect Developer Program APIs** (Activity API + Health API + Women's Health API)

**Rationale**: Official Garmin SDK provides device control, data streaming, and critically for HerPace - **Women's Health API** for menstrual cycle tracking data.

**Implementation Notes**:
- OAuth 2.0 authentication (similar to Strava)
- Activity API: Detailed fitness data from workouts
- Health API: All-day metrics (steps, heart rate, sleep, stress)
- Women's Health API: Menstrual cycle tracking for cycle phase validation
- Event-driven notifications for real-time updates within seconds of device sync
- Integration timeline: 1-4 weeks

**Alternative**: Terra API for unified multi-platform integration (supports 20+ platforms with single SDK)

---

### 7. Architecture: MVVM + StateFlow + Single-Activity

#### 7.1 State Management: StateFlow

**Decision**: Use **StateFlow** (not LiveData)

**Rationale**: StateFlow is the modern Android standard in 2026 with better Kotlin coroutines integration, built-in hot flow behavior, and type-safe null handling. Works seamlessly with Jetpack Compose reactive UI.

**Alternatives Considered**:
- LiveData: Legacy approach, less Kotlin-idiomatic
- SharedFlow: For one-time events, not state

**Implementation Pattern**:
```kotlin
class TrainingPlanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TrainingPlanUiState())
    val uiState: StateFlow<TrainingPlanUiState> = _uiState.asStateFlow()
}

// Collect with lifecycle awareness
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state -> /* update UI */ }
    }
}
```

**Key Differences from LiveData**:
- StateFlow requires initial state; LiveData does not
- StateFlow collected with `repeatOnLifecycle` for lifecycle safety
- Native Kotlin Flow operators (map, filter, combine)

#### 7.2 Navigation: Single-Activity Architecture

**Decision**: Use **Single-Activity** with **Jetpack Navigation Component**

**Rationale**: Google's official recommendation for new Android apps. Improved performance, reduced memory consumption, simplified navigation, easier state sharing between screens.

**Alternatives Considered**:
- Multi-Activity: Legacy approach, higher memory overhead
- Manual fragment transactions: Error-prone, complex back stack management

**Implementation**:
```
MainActivity (Single Activity)
├── AuthFragment (login/signup)
├── OnboardingFragment (profile setup)
├── DashboardFragment (home screen)
├── TrainingPlanFragment (workout calendar)
├── WorkoutDetailFragment (daily workout)
└── ProfileFragment (settings/cycle tracking)
```

**Key Components**:
- Navigation Graph (XML): Defines destinations and actions
- NavHost: Container in MainActivity hosting fragments
- NavController: Programmatic navigation control
- Safe Args plugin: Type-safe argument passing

**Benefits**:
- Automatic back stack management
- Simplified deep linking for notification navigation
- Shared ViewModels across fragments via `by activityViewModels()`

---

## Additional Recommendations

### Security
- **EncryptedSharedPreferences**: Store OAuth tokens and API keys
- **SQLCipher**: Encrypt Room database
- **Certificate Pinning**: OkHttp for API communication
- **Biometric Authentication**: BiometricPrompt API for app lock

### Testing
- **JUnit 5**: Unit tests
- **Compose Testing**: UI tests (`createComposeRule()`)
- **Turbine**: Testing Kotlin Flows/StateFlow
- **MockK**: Mocking (native Kotlin support)
- **Robolectric**: Android framework tests without emulator

### Offline-First Strategy
- Repository Pattern with Room as single source of truth
- WorkManager for background data sync
- Sync metadata on entities: `syncStatus`, `lastModified`, `serverTimestamp`
- Conflict resolution: last-write-wins (server takes precedence)
- Display sync status to users for pending uploads

### Monitoring
- **Firebase Crashlytics**: Crash reporting
- **Firebase Analytics**: User behavior tracking
- **Firebase Performance Monitoring**: Network/startup metrics

---

## Technology Stack Summary

| Category | Technology | Version |
|----------|-----------|---------|
| Language | Kotlin | 1.9+ |
| UI Framework | Jetpack Compose | Latest stable |
| Networking | Retrofit + OkHttp | 2.x |
| DI | Hilt | Latest stable |
| Database | Room + SQLCipher | Latest stable |
| Background Tasks | WorkManager | Latest stable |
| State Management | StateFlow | Kotlin 1.6+ |
| Navigation | Navigation Component | Latest stable |
| Google Fit | Health Connect API | Android 9+ |
| Strava | Strava API v3 | - |
| Garmin | Garmin Developer APIs | - |
| Security | EncryptedSharedPreferences + SQLCipher | - |
| Testing | JUnit 5 + Compose Test + MockK | - |
| Analytics | Firebase (Crashlytics, Analytics, Performance) | - |

---

## Next Steps for Implementation

1. **Project Setup**:
   - Create Android Studio project with Jetpack Compose template
   - Configure Hilt dependency injection
   - Set up Navigation Component with single-activity architecture

2. **Core Infrastructure**:
   - Implement Room database with SQLCipher encryption
   - Configure Retrofit API client with OkHttp interceptors
   - Set up repository pattern for data layer

3. **Authentication Flow**:
   - Build login/signup UI with Compose
   - Implement JWT token storage with EncryptedSharedPreferences
   - Create authentication repository and use cases

4. **Fitness Platform Integration**:
   - Register for Health Connect, Strava, Garmin developer accounts
   - Implement OAuth flows for Strava and Garmin
   - Test workout data import/export flows

5. **Notification System**:
   - Set up Firebase Cloud Messaging
   - Implement WorkManager for scheduled reminders
   - Configure deep linking for notification navigation

6. **Accessibility Testing**:
   - Set up TalkBack testing pipeline
   - Configure accessibility scanners (Lighthouse, axe)
   - Implement semantic modifiers for Compose components

---

## References

See agent a8480e4 research output for comprehensive source list covering:
- UI Framework (Jetpack Compose vs XML)
- Networking libraries (Retrofit vs Ktor)
- Dependency injection (Hilt vs Koin)
- Database (Room best practices)
- Background tasks (WorkManager vs AlarmManager)
- Health Connect migration (Google Fit deprecation)
- Strava and Garmin API documentation
- MVVM architecture (StateFlow vs LiveData)
- Navigation architecture (Single-Activity pattern)
- Accessibility (Material Design 3, TalkBack)
- Offline-first architecture patterns
