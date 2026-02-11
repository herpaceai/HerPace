# Data Model: HerPace Android App

**Branch**: 001-android-app
**Date**: 2026-02-10
**Purpose**: Define data entities, relationships, and validation rules for Android app

## Architecture Overview

The Android app uses **Clean Architecture** with three layers:

1. **Data Layer**: Room entities (local cache) + API DTOs (network models)
2. **Domain Layer**: Domain models (business logic representations)
3. **Presentation Layer**: UI state models (view-specific data)

**Data Flow**:
```
API DTOs <-> Repository <-> Domain Models <-> ViewModel <-> UI State
    ↕
Room Entities
```

---

## Domain Models

Domain models represent business concepts independent of data source or presentation.

### User

Represents the authenticated user account.

**Fields**:
- `id: String` - Unique user identifier (UUID from backend)
- `email: String` - User's email address (used for login)
- `createdAt: Instant` - Account creation timestamp

**Validation**:
- `email` must be valid email format (regex pattern)
- `id` is immutable once set

**Relationships**:
- Has one `RunnerProfile`

---

### RunnerProfile

Contains personal training information and cycle tracking data.

**Fields**:
- `userId: String` - Foreign key to User
- `name: String` - User's full name
- `age: Int` - Age in years
- `fitnessLevel: FitnessLevel` - Beginner, Intermediate, Advanced
- `currentWeeklyMileage: Double` - Current weekly running distance (km)
- `cycleLength: Int` - Menstrual cycle length in days (21-40)
- `lastPeriodStartDate: LocalDate` - Most recent period start date
- `notificationsEnabled: Boolean` - Push notification preference
- `reminderTimeMorning: LocalTime?` - Morning workout reminder time (optional)
- `reminderTimeEvening: LocalTime?` - Evening pre-workout reminder time (optional)
- `lastUpdated: Instant` - Profile last modified timestamp

**Validation**:
- `age` must be 13-120 (realistic range)
- `currentWeeklyMileage` must be 0-250 km (reasonable max)
- `cycleLength` must be 21-40 days (medical range)
- `lastPeriodStartDate` must not be more than 90 days in the past
- `name` must be 1-100 characters, non-blank
- `reminderTimeMorning` and `reminderTimeEvening` required if `notificationsEnabled` is true

**Relationships**:
- Belongs to one `User`
- Has many `Race`
- Has many `TrainingPlan`

**State Transitions**:
- Profile is created during onboarding (mandatory)
- Cycle data updated when user logs period start
- Notification preferences toggled in settings

---

### Race

Represents a target race event.

**Fields**:
- `id: String` - Unique race identifier (UUID)
- `userId: String` - Foreign key to User
- `name: String` - Race name/title
- `date: LocalDate` - Race date
- `distance: RaceDistance` - 5K, 10K, HalfMarathon, Marathon
- `goalTimeMinutes: Int?` - Target finish time in minutes (optional)
- `createdAt: Instant` - Race entry creation timestamp
- `updatedAt: Instant` - Last modification timestamp

**Validation**:
- `name` must be 1-200 characters, non-blank
- `date` must be in the future (not past)
- `goalTimeMinutes` must be 10-600 minutes (if provided)
- Cannot create race with date less than 4 weeks away for marathon (insufficient training time)

**Relationships**:
- Belongs to one `RunnerProfile`
- Has zero or one `TrainingPlan` (plan is optional until generated)

**State Transitions**:
- Created → Active (when future dated)
- Active → Completed (when date passes)
- Can be edited before training plan is generated
- Editing after plan generation requires plan regeneration

---

### TrainingPlan

AI-generated training schedule for a specific race.

**Fields**:
- `id: String` - Unique plan identifier (UUID)
- `raceId: String` - Foreign key to Race
- `userId: String` - Foreign key to User
- `startDate: LocalDate` - Plan start date
- `endDate: LocalDate` - Plan end date (race day)
- `generatedAt: Instant` - AI generation timestamp
- `totalWeeks: Int` - Number of weeks in plan
- `isActive: Boolean` - Whether this is the current active plan

**Validation**:
- `startDate` must be before `endDate`
- `totalWeeks` must be 4-32 weeks (reasonable training duration)
- Only one active plan per user at a time
- Plan cannot be modified after generation (must regenerate)

**Relationships**:
- Belongs to one `Race`
- Belongs to one `User`
- Has many `TrainingSession` (sessions ordered by date)

**State Transitions**:
- Generated → Active (when plan is created and becomes current)
- Active → Completed (when endDate passes)
- Active → Archived (when user generates new plan for same/different race)

---

### TrainingSession

Individual workout within a training plan.

**Fields**:
- `id: String` - Unique session identifier (UUID)
- `planId: String` - Foreign key to TrainingPlan
- `date: LocalDate` - Scheduled session date
- `weekNumber: Int` - Week number in plan (1-indexed)
- `dayOfWeek: DayOfWeek` - Monday, Tuesday, etc.
- `workoutType: WorkoutType` - EasyRun, LongRun, TempoRun, Intervals, RestDay
- `distanceKm: Double?` - Planned distance in kilometers (null for rest days)
- `intensityLevel: IntensityLevel` - Low, Moderate, High
- `targetPaceMinPerKm: Double?` - Target pace in min/km (optional)
- `notes: String?` - AI-generated workout notes and guidance
- `cyclePhase: CyclePhase` - Menstrual, Follicular, Ovulatory, Luteal
- `completed: Boolean` - Whether session has been completed
- `completedAt: Instant?` - Completion timestamp (null if not completed)

**Validation**:
- `weekNumber` must be 1-totalWeeks (from parent TrainingPlan)
- `distanceKm` must be 0-50 km for single session
- `targetPaceMinPerKm` must be 3-12 min/km (reasonable pace range)
- `notes` max 500 characters
- `completed` can only transition false → true (no undo)

**Relationships**:
- Belongs to one `TrainingPlan`
- Has zero or one `WorkoutLog` (log is optional even after completion)

**State Transitions**:
- Scheduled → Completed (when user marks complete)
- Completed sessions are immutable (cannot change back to scheduled)

---

### WorkoutLog

User-entered data for completed training sessions.

**Fields**:
- `id: String` - Unique log identifier (UUID)
- `sessionId: String` - Foreign key to TrainingSession
- `userId: String` - Foreign key to User
- `actualDistanceKm: Double` - Actual distance run
- `actualDurationMinutes: Int` - Actual time taken in minutes
- `perceivedEffort: Int` - Rating 1-10 (RPE scale)
- `notes: String?` - Optional user notes about the workout
- `importedFrom: FitnessPlatform?` - Source if auto-imported (Strava, Garmin, HealthConnect, null if manual)
- `loggedAt: Instant` - Log entry creation timestamp

**Validation**:
- `actualDistanceKm` must be 0-100 km (reasonable max)
- `actualDurationMinutes` must be 1-600 minutes (10 hours max)
- `perceivedEffort` must be 1-10 (RPE scale)
- `notes` max 500 characters
- If `importedFrom` is not null, `actualDistanceKm` and `actualDurationMinutes` are required

**Relationships**:
- Belongs to one `TrainingSession`
- Belongs to one `User`

**State Transitions**:
- Created (when user logs workout manually or via platform import)
- Immutable after creation (edit requires delete + recreate)

---

### NotificationSchedule

Configuration for workout reminder notifications.

**Fields**:
- `id: String` - Unique schedule identifier (UUID)
- `userId: String` - Foreign key to User
- `enabled: Boolean` - Master notification toggle
- `morningReminderEnabled: Boolean` - Morning-of reminder toggle
- `morningReminderTime: LocalTime?` - Time for morning reminder (e.g., 7:00 AM)
- `eveningReminderEnabled: Boolean` - Evening-before reminder toggle
- `eveningReminderTime: LocalTime?` - Time for evening reminder (e.g., 6:00 PM)
- `updatedAt: Instant` - Last modification timestamp

**Validation**:
- If `enabled` is true, at least one of `morningReminderEnabled` or `eveningReminderEnabled` must be true
- If `morningReminderEnabled`, `morningReminderTime` is required
- If `eveningReminderEnabled`, `eveningReminderTime` is required
- `morningReminderTime` and `eveningReminderTime` must be different (at least 1 hour apart)

**Relationships**:
- Belongs to one `User`

**State Transitions**:
- Created with defaults during onboarding
- Updated via settings screen
- Changes trigger WorkManager job reschedule

---

### FitnessPlatformConnection

Tracks user's connected fitness platforms for workout import.

**Fields**:
- `id: String` - Unique connection identifier (UUID)
- `userId: String` - Foreign key to User
- `platform: FitnessPlatform` - Strava, Garmin, HealthConnect
- `connected: Boolean` - Whether platform is currently connected
- `accessToken: String?` - OAuth access token (encrypted in storage)
- `refreshToken: String?` - OAuth refresh token (encrypted in storage)
- `tokenExpiresAt: Instant?` - Access token expiration timestamp
- `lastSyncAt: Instant?` - Last successful workout data sync timestamp
- `connectedAt: Instant` - Initial connection timestamp
- `disconnectedAt: Instant?` - Disconnection timestamp (null if still connected)

**Validation**:
- `platform` must be unique per user (cannot connect same platform twice)
- If `connected` is true, `accessToken` is required
- `accessToken` and `refreshToken` are encrypted before storage (EncryptedSharedPreferences)
- `tokenExpiresAt` must be in the future for connected platforms

**Relationships**:
- Belongs to one `User`

**State Transitions**:
- Disconnected → Connected (OAuth flow completes successfully)
- Connected → Disconnected (user revokes access or token refresh fails)
- Connected platforms sync workouts via background WorkManager job

**Security**:
- Tokens stored in EncryptedSharedPreferences, not Room database
- Only connection metadata persisted in Room

---

## Enums

### FitnessLevel
- `BEGINNER`: New to running or returning after long break
- `INTERMEDIATE`: Regular runner with some race experience
- `ADVANCED`: Experienced runner with multiple race completions

### RaceDistance
- `FIVE_K`: 5 kilometers
- `TEN_K`: 10 kilometers
- `HALF_MARATHON`: 21.1 kilometers
- `MARATHON`: 42.2 kilometers

### WorkoutType
- `EASY_RUN`: Low-intensity aerobic run
- `LONG_RUN`: Extended distance at easy pace
- `TEMPO_RUN`: Sustained effort at lactate threshold
- `INTERVALS`: High-intensity repeated efforts with recovery
- `REST_DAY`: No running activity, recovery day

### IntensityLevel
- `LOW`: Easy conversational pace, RPE 3-4
- `MODERATE`: Comfortably hard, RPE 5-6
- `HIGH`: Hard effort, RPE 7-8

### CyclePhase
- `MENSTRUAL`: Days 1-5 (low energy, recovery focus)
- `FOLLICULAR`: Days 6-13 (increasing energy, good for hard workouts)
- `OVULATORY`: Days 14-16 (peak energy, ideal for tempo/intervals)
- `LUTEAL`: Days 17-28 (decreasing energy, moderate intensity)

### FitnessPlatform
- `STRAVA`: Strava workout import
- `GARMIN`: Garmin Connect workout import
- `HEALTH_CONNECT`: Google Health Connect (Google Fit replacement)

---

## Room Database Entities

Room entities mirror domain models with additional metadata for offline caching and sync.

### Common Fields for All Entities

All Room entities include:
- `syncStatus: SyncStatus` - NotSynced, Syncing, Synced, SyncFailed
- `lastModified: Instant` - Local modification timestamp
- `serverTimestamp: Instant?` - Server-side timestamp (null if not synced)
- `version: Int` - Optimistic locking version number

### SyncStatus Enum
- `NOT_SYNCED`: Local-only data not yet uploaded to server
- `SYNCING`: Sync in progress (WorkManager job active)
- `SYNCED`: Successfully synced with server, local matches remote
- `SYNC_FAILED`: Sync failed, retry needed (conflict or network error)

### Entity Mapping Examples

**UserEntity** (Room):
```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val createdAt: Instant,
    // Sync metadata
    val syncStatus: SyncStatus,
    val lastModified: Instant,
    val serverTimestamp: Instant?,
    val version: Int
)
```

**TrainingSessionEntity** (Room):
```kotlin
@Entity(
    tableName = "training_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TrainingPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("planId"), Index("date")]
)
data class TrainingSessionEntity(
    @PrimaryKey val id: String,
    val planId: String,
    val date: LocalDate,
    val weekNumber: Int,
    val dayOfWeek: DayOfWeek,
    val workoutType: WorkoutType,
    val distanceKm: Double?,
    val intensityLevel: IntensityLevel,
    val targetPaceMinPerKm: Double?,
    val notes: String?,
    val cyclePhase: CyclePhase,
    val completed: Boolean,
    val completedAt: Instant?,
    // Sync metadata
    val syncStatus: SyncStatus,
    val lastModified: Instant,
    val serverTimestamp: Instant?,
    val version: Int
)
```

### TypeConverters

Room requires TypeConverters for non-primitive types:

```kotlin
class Converters {
    // Instant (timestamp)
    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()
    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    // LocalDate
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // LocalTime
    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? = value?.toString()
    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? = value?.let { LocalTime.parse(it) }

    // Enums (FitnessLevel, RaceDistance, WorkoutType, etc.)
    @TypeConverter
    fun fromFitnessLevel(value: FitnessLevel): String = value.name
    @TypeConverter
    fun toFitnessLevel(value: String): FitnessLevel = FitnessLevel.valueOf(value)

    // ... additional enum converters
}
```

---

## API DTOs (Data Transfer Objects)

DTOs represent API request/response payloads matching the backend's C# models.

### Naming Convention
- Request DTOs: `{Entity}CreateRequest`, `{Entity}UpdateRequest`
- Response DTOs: `{Entity}Response`

### Example DTOs

**RunnerProfileResponse** (from `GET /api/profiles/me`):
```kotlin
@Serializable
data class RunnerProfileResponse(
    val userId: String,
    val name: String,
    val age: Int,
    val fitnessLevel: String, // "Beginner", "Intermediate", "Advanced"
    val currentWeeklyMileage: Double,
    val cycleLength: Int,
    val lastPeriodStartDate: String, // ISO 8601 date string
    val notificationsEnabled: Boolean,
    val reminderTimeMorning: String?, // ISO 8601 time string
    val reminderTimeEvening: String?,
    val lastUpdated: String // ISO 8601 timestamp
)
```

**TrainingPlanResponse** (from `POST /api/plans`):
```kotlin
@Serializable
data class TrainingPlanResponse(
    val id: String,
    val raceId: String,
    val userId: String,
    val startDate: String, // ISO 8601
    val endDate: String,
    val generatedAt: String,
    val totalWeeks: Int,
    val sessions: List<TrainingSessionResponse>
)

@Serializable
data class TrainingSessionResponse(
    val id: String,
    val date: String,
    val weekNumber: Int,
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
    val workoutType: String, // "EasyRun", "LongRun", etc.
    val distanceKm: Double?,
    val intensityLevel: String, // "Low", "Moderate", "High"
    val targetPaceMinPerKm: Double?,
    val notes: String?,
    val cyclePhase: String // "Menstrual", "Follicular", etc.
)
```

### DTO Mapping

Repositories handle conversion between DTOs and domain models:

```kotlin
// DTO → Domain Model
fun TrainingPlanResponse.toDomain(): TrainingPlan {
    return TrainingPlan(
        id = id,
        raceId = raceId,
        userId = userId,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        generatedAt = Instant.parse(generatedAt),
        totalWeeks = totalWeeks,
        isActive = true
    )
}

// Domain Model → DTO
fun RunnerProfile.toUpdateRequest(): RunnerProfileUpdateRequest {
    return RunnerProfileUpdateRequest(
        name = name,
        age = age,
        fitnessLevel = fitnessLevel.name,
        currentWeeklyMileage = currentWeeklyMileage,
        cycleLength = cycleLength,
        lastPeriodStartDate = lastPeriodStartDate.toString(),
        notificationsEnabled = notificationsEnabled,
        reminderTimeMorning = reminderTimeMorning?.toString(),
        reminderTimeEvening = reminderTimeEvening?.toString()
    )
}
```

---

## Data Layer Architecture

### Repository Pattern

Repositories abstract data sources, providing a single source of truth:

```
                   ViewModel
                      ↓
               Repository Interface
                      ↓
          Repository Implementation
                 ↙         ↘
         RemoteDataSource   LocalDataSource
              (API)            (Room)
```

**Example: TrainingPlanRepository**
```kotlin
interface TrainingPlanRepository {
    suspend fun generatePlan(raceId: String): Result<TrainingPlan>
    fun getActivePlan(): Flow<TrainingPlan?>
    fun getPlanById(planId: String): Flow<TrainingPlan?>
    suspend fun markSessionComplete(sessionId: String): Result<Unit>
}

class TrainingPlanRepositoryImpl(
    private val apiService: HerPaceApiService,
    private val planDao: TrainingPlanDao,
    private val sessionDao: TrainingSessionDao,
    private val networkMonitor: NetworkMonitor
) : TrainingPlanRepository {

    override suspend fun generatePlan(raceId: String): Result<TrainingPlan> {
        // 1. Call API to generate plan
        val response = apiService.generatePlan(raceId)

        // 2. Map DTO to domain model
        val plan = response.toDomain()

        // 3. Save to Room for offline access
        planDao.insert(plan.toEntity())
        sessionDao.insertAll(plan.sessions.map { it.toEntity() })

        return Result.success(plan)
    }

    override fun getActivePlan(): Flow<TrainingPlan?> {
        // Return Room Flow (single source of truth)
        return planDao.getActivePlan()
            .map { it?.toDomain() }
    }
}
```

### Offline-First Strategy

1. **Read**: Always read from Room (local cache)
2. **Write**: Write to Room immediately, sync to server in background
3. **Sync**: WorkManager jobs sync pending changes when online
4. **Conflict Resolution**: Server wins on conflicts (last-write-wins)

**Sync Metadata Flow**:
```
User Action → Local Write (Room)
           ↓
    syncStatus = NOT_SYNCED
           ↓
WorkManager Job Triggers
           ↓
    syncStatus = SYNCING
           ↓
API Call Succeeds → syncStatus = SYNCED
                 ↓
          serverTimestamp = now()
```

---

## Validation Rules Summary

| Entity | Field | Validation |
|--------|-------|------------|
| User | email | Valid email format |
| RunnerProfile | age | 13-120 years |
| RunnerProfile | cycleLength | 21-40 days |
| RunnerProfile | currentWeeklyMileage | 0-250 km |
| RunnerProfile | lastPeriodStartDate | Not more than 90 days ago |
| Race | date | Must be in future |
| Race | goalTimeMinutes | 10-600 minutes (if provided) |
| Race | name | 1-200 characters |
| TrainingSession | distanceKm | 0-50 km |
| TrainingSession | targetPaceMinPerKm | 3-12 min/km |
| WorkoutLog | perceivedEffort | 1-10 (RPE scale) |
| WorkoutLog | actualDurationMinutes | 1-600 minutes |

All validations enforced at:
1. **UI Layer**: Immediate feedback with Compose field validators
2. **Domain Layer**: Business rule enforcement in use cases
3. **Data Layer**: Database constraints and TypeConverter validation

---

## Database Schema Migrations

Room migrations ensure data integrity across app versions.

**Initial Schema Version 1**:
- All entities defined above
- Foreign keys enforced with CASCADE delete
- Indices on foreign keys and frequently queried fields (date, userId)

**Future Migration Example** (Version 1 → 2):
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: Add new column to RunnerProfile
        database.execSQL(
            "ALTER TABLE runner_profiles ADD COLUMN preferred_units TEXT NOT NULL DEFAULT 'metric'"
        )
    }
}
```

**Migration Strategy**:
- Always provide migration paths (never use `fallbackToDestructiveMigration()` in production)
- Test migrations with pre-populated databases
- Version schema in `@Database` annotation
- Document all schema changes in migration comments

---

## Summary

The HerPace Android app data model follows **Clean Architecture** with clear separation between:
- **API DTOs**: Network communication (kotlinx.serialization)
- **Room Entities**: Local persistence with sync metadata
- **Domain Models**: Business logic representations

**Key Design Decisions**:
1. **Offline-First**: Room is single source of truth, API syncs in background
2. **Repository Pattern**: Abstracts data sources for testability
3. **StateFlow**: Reactive data propagation from Room to UI
4. **Sync Metadata**: All entities track sync status for offline support
5. **Type Safety**: Kotlin sealed classes, enums, and TypeConverters prevent errors
6. **Validation**: Multi-layer validation (UI, domain, data) ensures data integrity

This model supports the 8 prioritized user stories with robust offline capabilities, platform integrations, and cycle-aware training adaptations.
