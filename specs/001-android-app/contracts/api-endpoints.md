# API Endpoints: HerPace Backend

**Branch**: 001-android-app
**Date**: 2026-02-10
**Base URL (Production)**: `https://herpace-api-330702404265.us-central1.run.app`
**Base URL (Development)**: `https://localhost:7001`

## Overview

The Android app consumes the existing HerPace ASP.NET Core REST API. All endpoints use JSON for request/response bodies and JWT tokens for authentication.

### Authentication

**Header Format**:
```http
Authorization: Bearer <JWT_TOKEN>
```

**Token Acquisition**: Obtained from `/api/auth/login` or `/api/auth/signup` endpoints.

**Token Expiration**: Tokens expire after 24 hours (backend configured). App must handle 401 Unauthorized responses and prompt re-login.

---

## Endpoints

### 1. Authentication

#### POST /api/auth/signup

Create a new user account.

**Request**:
```http
POST /api/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "confirmPassword": "SecurePassword123!"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid email format, weak password, or passwords don't match
  ```json
  {
    "errors": {
      "Email": ["Invalid email format"],
      "Password": ["Password must be at least 8 characters"]
    }
  }
  ```
- `409 Conflict`: Email already registered
  ```json
  {
    "message": "An account with this email already exists"
  }
  ```

**Android Implementation**:
```kotlin
@POST("api/auth/signup")
suspend fun signup(@Body request: SignupRequest): SignupResponse

data class SignupRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)

data class SignupResponse(
    val token: String,
    val userId: String,
    val email: String
)
```

---

#### POST /api/auth/login

Authenticate existing user.

**Request**:
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com"
}
```

**Error Responses**:
- `401 Unauthorized`: Invalid credentials
  ```json
  {
    "message": "Invalid email or password"
  }
  ```
- `400 Bad Request`: Missing required fields

**Android Implementation**:
```kotlin
@POST("api/auth/login")
suspend fun login(@Body request: LoginRequest): LoginResponse

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val email: String
)
```

---

### 2. Runner Profile

#### GET /api/profiles/me

Get the authenticated user's runner profile.

**Request**:
```http
GET /api/profiles/me
Authorization: Bearer <TOKEN>
```

**Response** (200 OK):
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Jane Doe",
  "age": 32,
  "fitnessLevel": "Intermediate",
  "currentWeeklyMileage": 25.5,
  "cycleLength": 28,
  "lastPeriodStartDate": "2026-02-01",
  "notificationsEnabled": true,
  "reminderTimeMorning": "07:00:00",
  "reminderTimeEvening": "18:00:00",
  "lastUpdated": "2026-02-10T14:30:00Z"
}
```

**Response** (404 Not Found):
Profile doesn't exist (user hasn't completed onboarding)
```json
{
  "message": "Profile not found"
}
```

**Android Implementation**:
```kotlin
@GET("api/profiles/me")
suspend fun getProfile(): RunnerProfileResponse?

@Serializable
data class RunnerProfileResponse(
    val userId: String,
    val name: String,
    val age: Int,
    val fitnessLevel: String, // "Beginner", "Intermediate", "Advanced"
    val currentWeeklyMileage: Double,
    val cycleLength: Int,
    val lastPeriodStartDate: String, // ISO 8601 date
    val notificationsEnabled: Boolean,
    val reminderTimeMorning: String?, // ISO 8601 time
    val reminderTimeEvening: String?,
    val lastUpdated: String // ISO 8601 timestamp
)
```

---

#### POST /api/profiles/me

Create or update the authenticated user's runner profile.

**Request**:
```http
POST /api/profiles/me
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "name": "Jane Doe",
  "age": 32,
  "fitnessLevel": "Intermediate",
  "currentWeeklyMileage": 25.5,
  "cycleLength": 28,
  "lastPeriodStartDate": "2026-02-01",
  "notificationsEnabled": true,
  "reminderTimeMorning": "07:00:00",
  "reminderTimeEvening": "18:00:00"
}
```

**Response** (200 OK):
```json
{
  "message": "Profile saved successfully",
  "profile": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Jane Doe",
    "age": 32,
    "fitnessLevel": "Intermediate",
    "currentWeeklyMileage": 25.5,
    "cycleLength": 28,
    "lastPeriodStartDate": "2026-02-01",
    "notificationsEnabled": true,
    "reminderTimeMorning": "07:00:00",
    "reminderTimeEvening": "18:00:00",
    "lastUpdated": "2026-02-10T14:35:00Z"
  }
}
```

**Error Responses**:
- `400 Bad Request`: Invalid data (age out of range, cycle length invalid, etc.)
  ```json
  {
    "errors": {
      "CycleLength": ["Cycle length must be between 21 and 40 days"],
      "Age": ["Age must be between 13 and 120"]
    }
  }
  ```

**Android Implementation**:
```kotlin
@POST("api/profiles/me")
suspend fun saveProfile(@Body request: RunnerProfileRequest): SaveProfileResponse

@Serializable
data class RunnerProfileRequest(
    val name: String,
    val age: Int,
    val fitnessLevel: String,
    val currentWeeklyMileage: Double,
    val cycleLength: Int,
    val lastPeriodStartDate: String,
    val notificationsEnabled: Boolean,
    val reminderTimeMorning: String?,
    val reminderTimeEvening: String?
)

@Serializable
data class SaveProfileResponse(
    val message: String,
    val profile: RunnerProfileResponse
)
```

---

### 3. Races

#### GET /api/races

Get all races for the authenticated user.

**Request**:
```http
GET /api/races
Authorization: Bearer <TOKEN>
```

**Response** (200 OK):
```json
{
  "races": [
    {
      "id": "race-uuid-1",
      "userId": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Spring Marathon 2026",
      "date": "2026-04-15",
      "distance": "Marathon",
      "goalTimeMinutes": 240,
      "createdAt": "2026-02-01T10:00:00Z",
      "updatedAt": "2026-02-01T10:00:00Z"
    },
    {
      "id": "race-uuid-2",
      "userId": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Summer 10K",
      "date": "2026-06-20",
      "distance": "10K",
      "goalTimeMinutes": 50,
      "createdAt": "2026-02-05T12:00:00Z",
      "updatedAt": "2026-02-05T12:00:00Z"
    }
  ]
}
```

**Response** (200 OK - Empty):
```json
{
  "races": []
}
```

**Android Implementation**:
```kotlin
@GET("api/races")
suspend fun getRaces(): RacesResponse

@Serializable
data class RacesResponse(
    val races: List<RaceResponse>
)

@Serializable
data class RaceResponse(
    val id: String,
    val userId: String,
    val name: String,
    val date: String, // ISO 8601 date
    val distance: String, // "5K", "10K", "HalfMarathon", "Marathon"
    val goalTimeMinutes: Int?,
    val createdAt: String,
    val updatedAt: String
)
```

---

#### POST /api/races

Create a new race.

**Request**:
```http
POST /api/races
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "name": "Spring Marathon 2026",
  "date": "2026-04-15",
  "distance": "Marathon",
  "goalTimeMinutes": 240
}
```

**Response** (201 Created):
```json
{
  "id": "race-uuid-1",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Spring Marathon 2026",
  "date": "2026-04-15",
  "distance": "Marathon",
  "goalTimeMinutes": 240,
  "createdAt": "2026-02-10T15:00:00Z",
  "updatedAt": "2026-02-10T15:00:00Z"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid data (date in past, invalid distance, etc.)
  ```json
  {
    "errors": {
      "Date": ["Race date must be in the future"],
      "Name": ["Name is required"]
    }
  }
  ```

**Android Implementation**:
```kotlin
@POST("api/races")
suspend fun createRace(@Body request: CreateRaceRequest): RaceResponse

@Serializable
data class CreateRaceRequest(
    val name: String,
    val date: String,
    val distance: String,
    val goalTimeMinutes: Int?
)
```

---

#### GET /api/races/{id}

Get a specific race by ID.

**Request**:
```http
GET /api/races/race-uuid-1
Authorization: Bearer <TOKEN>
```

**Response** (200 OK):
```json
{
  "id": "race-uuid-1",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Spring Marathon 2026",
  "date": "2026-04-15",
  "distance": "Marathon",
  "goalTimeMinutes": 240,
  "createdAt": "2026-02-01T10:00:00Z",
  "updatedAt": "2026-02-01T10:00:00Z"
}
```

**Error Responses**:
- `404 Not Found`: Race doesn't exist or doesn't belong to user

**Android Implementation**:
```kotlin
@GET("api/races/{id}")
suspend fun getRace(@Path("id") raceId: String): RaceResponse
```

---

#### PUT /api/races/{id}

Update an existing race.

**Request**:
```http
PUT /api/races/race-uuid-1
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "name": "Updated Marathon Name",
  "date": "2026-04-20",
  "distance": "Marathon",
  "goalTimeMinutes": 230
}
```

**Response** (200 OK):
```json
{
  "id": "race-uuid-1",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Updated Marathon Name",
  "date": "2026-04-20",
  "distance": "Marathon",
  "goalTimeMinutes": 230,
  "createdAt": "2026-02-01T10:00:00Z",
  "updatedAt": "2026-02-10T15:30:00Z"
}
```

**Error Responses**:
- `404 Not Found`: Race doesn't exist
- `400 Bad Request`: Invalid update data

**Android Implementation**:
```kotlin
@PUT("api/races/{id}")
suspend fun updateRace(
    @Path("id") raceId: String,
    @Body request: UpdateRaceRequest
): RaceResponse

@Serializable
data class UpdateRaceRequest(
    val name: String,
    val date: String,
    val distance: String,
    val goalTimeMinutes: Int?
)
```

---

#### DELETE /api/races/{id}

Delete a race.

**Request**:
```http
DELETE /api/races/race-uuid-1
Authorization: Bearer <TOKEN>
```

**Response** (204 No Content):
Empty response body on successful deletion.

**Error Responses**:
- `404 Not Found`: Race doesn't exist
- `409 Conflict`: Race has associated training plan (delete plan first)

**Android Implementation**:
```kotlin
@DELETE("api/races/{id}")
suspend fun deleteRace(@Path("id") raceId: String): Response<Unit>
```

---

### 4. Training Plans

#### POST /api/plans

Generate an AI training plan for a race.

**Request**:
```http
POST /api/plans
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "raceId": "race-uuid-1"
}
```

**Response** (201 Created):
```json
{
  "id": "plan-uuid-1",
  "raceId": "race-uuid-1",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "startDate": "2026-02-17",
  "endDate": "2026-04-15",
  "generatedAt": "2026-02-10T16:00:00Z",
  "totalWeeks": 8,
  "sessions": [
    {
      "id": "session-uuid-1",
      "date": "2026-02-17",
      "weekNumber": 1,
      "dayOfWeek": "Monday",
      "workoutType": "EasyRun",
      "distanceKm": 5.0,
      "intensityLevel": "Low",
      "targetPaceMinPerKm": 6.5,
      "notes": "Easy warm-up run to start the week. Focus on comfortable breathing.",
      "cyclePhase": "Follicular"
    },
    {
      "id": "session-uuid-2",
      "date": "2026-02-18",
      "weekNumber": 1,
      "dayOfWeek": "Tuesday",
      "workoutType": "RestDay",
      "distanceKm": null,
      "intensityLevel": "Low",
      "targetPaceMinPerKm": null,
      "notes": "Rest and recovery day. Light stretching recommended.",
      "cyclePhase": "Follicular"
    }
    // ... more sessions (typically 50-80 sessions for 8-week plan)
  ]
}
```

**Error Responses**:
- `404 Not Found`: Race doesn't exist
- `400 Bad Request`: User profile incomplete (missing cycle data)
- `409 Conflict`: Plan already exists for this race
  ```json
  {
    "message": "A training plan already exists for this race. Delete it first to regenerate."
  }
  ```
- `500 Internal Server Error`: AI generation failed
  ```json
  {
    "message": "Failed to generate training plan. Please try again."
  }
  ```
- `503 Service Unavailable`: Gemini API unavailable

**Android Implementation**:
```kotlin
@POST("api/plans")
suspend fun generatePlan(@Body request: GeneratePlanRequest): TrainingPlanResponse

@Serializable
data class GeneratePlanRequest(
    val raceId: String
)

@Serializable
data class TrainingPlanResponse(
    val id: String,
    val raceId: String,
    val userId: String,
    val startDate: String,
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
    val dayOfWeek: String,
    val workoutType: String,
    val distanceKm: Double?,
    val intensityLevel: String,
    val targetPaceMinPerKm: Double?,
    val notes: String?,
    val cyclePhase: String
)
```

**Note**: This endpoint can take 10-30 seconds to complete due to AI generation time. Implement appropriate loading UI and timeout handling (30s recommended).

---

#### GET /api/plans/active

Get the user's active training plan.

**Request**:
```http
GET /api/plans/active
Authorization: Bearer <TOKEN>
```

**Response** (200 OK):
```json
{
  "id": "plan-uuid-1",
  "raceId": "race-uuid-1",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "startDate": "2026-02-17",
  "endDate": "2026-04-15",
  "generatedAt": "2026-02-10T16:00:00Z",
  "totalWeeks": 8,
  "sessions": [/* ... all sessions ... */]
}
```

**Response** (404 Not Found):
```json
{
  "message": "No active training plan found"
}
```

**Android Implementation**:
```kotlin
@GET("api/plans/active")
suspend fun getActivePlan(): TrainingPlanResponse?
```

---

## Error Handling

### Standard Error Response Format

```json
{
  "message": "Human-readable error message",
  "errors": {
    "FieldName": ["Validation error 1", "Validation error 2"]
  }
}
```

### HTTP Status Codes

| Code | Meaning | Android Action |
|------|---------|----------------|
| 200 | OK | Parse response |
| 201 | Created | Parse response, update UI |
| 204 | No Content | Success, no body |
| 400 | Bad Request | Show validation errors to user |
| 401 | Unauthorized | Clear token, redirect to login |
| 404 | Not Found | Show "not found" message |
| 409 | Conflict | Show conflict message, offer resolution |
| 500 | Internal Server Error | Show generic error, offer retry |
| 503 | Service Unavailable | Show "service down" message, retry later |

---

## Android Retrofit Configuration

### API Service Interface

```kotlin
interface HerPaceApiService {
    // Auth
    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Profile
    @GET("api/profiles/me")
    suspend fun getProfile(): RunnerProfileResponse?

    @POST("api/profiles/me")
    suspend fun saveProfile(@Body request: RunnerProfileRequest): SaveProfileResponse

    // Races
    @GET("api/races")
    suspend fun getRaces(): RacesResponse

    @POST("api/races")
    suspend fun createRace(@Body request: CreateRaceRequest): RaceResponse

    @GET("api/races/{id}")
    suspend fun getRace(@Path("id") raceId: String): RaceResponse

    @PUT("api/races/{id}")
    suspend fun updateRace(
        @Path("id") raceId: String,
        @Body request: UpdateRaceRequest
    ): RaceResponse

    @DELETE("api/races/{id}")
    suspend fun deleteRace(@Path("id") raceId: String): Response<Unit>

    // Training Plans
    @POST("api/plans")
    suspend fun generatePlan(@Body request: GeneratePlanRequest): TrainingPlanResponse

    @GET("api/plans/active")
    suspend fun getActivePlan(): TrainingPlanResponse?
}
```

### Retrofit Builder with JWT Interceptor

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authTokenProvider: AuthTokenProvider
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()

                // Add JWT token to all requests except auth endpoints
                val token = authTokenProvider.getToken()
                if (token != null && !chain.request().url.encodedPath.contains("/api/auth/")) {
                    request.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(request.build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideHerPaceApiService(retrofit: Retrofit): HerPaceApiService {
        return retrofit.create(HerPaceApiService::class.java)
    }
}
```

### Error Handling Extension

```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String?) : ApiResult<Nothing>()
    object NetworkError : ApiResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: HttpException) {
        ApiResult.Error(e.code(), e.message())
    } catch (e: IOException) {
        ApiResult.NetworkError
    } catch (e: Exception) {
        ApiResult.Error(-1, e.message)
    }
}

// Usage in Repository
override suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
    return safeApiCall {
        apiService.login(LoginRequest(email, password))
    }
}
```

---

## Rate Limiting

The backend does not currently implement rate limiting, but the Android app should implement:

1. **Request Debouncing**: Prevent rapid repeated requests (e.g., button double-taps)
2. **Retry Logic**: Exponential backoff for failed requests (max 3 retries)
3. **Caching**: Cache responses where appropriate (race list, profile data)

---

## Testing Endpoints

Use these base URLs for different environments:

- **Local Development**: `https://localhost:7001` (requires backend running locally)
- **Production**: `https://herpace-api-330702404265.us-central1.run.app`

**Build Configuration** (build.gradle.kts):
```kotlin
buildTypes {
    debug {
        buildConfigField("String", "API_BASE_URL", "\"https://localhost:7001\"")
    }
    release {
        buildConfigField("String", "API_BASE_URL", "\"https://herpace-api-330702404265.us-central1.run.app\"")
    }
}
```

---

## Future Endpoints (Not Yet Implemented)

These endpoints may be added in future backend updates:

- `PUT /api/sessions/{id}/complete`: Mark session complete with workout log data
- `GET /api/sessions/{id}`: Get single session details
- `POST /api/integrations/strava/connect`: Connect Strava account
- `POST /api/integrations/garmin/connect`: Connect Garmin account
- `GET /api/notifications/schedule`: Get notification schedule
- `PUT /api/notifications/schedule`: Update notification times

**Workaround**: For now, workout completion and platform integration data will be stored locally in Room until backend endpoints are available.

---

## Summary

The Android app consumes a RESTful API with JWT authentication. Key integration points:

1. **Authentication**: Token obtained at login, stored securely, injected via OkHttpClient interceptor
2. **Profile Management**: POST to create/update, GET to retrieve
3. **Race Management**: Full CRUD operations
4. **Training Plans**: POST to generate (long-running), GET to retrieve active plan
5. **Error Handling**: Standard HTTP status codes with structured error responses
6. **Offline Support**: Cache all GET responses in Room for offline viewing

All endpoints are documented with request/response examples and Kotlin data class definitions for easy Retrofit integration.
