# Tasks: HerPace Android App

**Input**: Design documents from `/specs/001-android-app/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/api-endpoints.md

**Tests**: Tests are OPTIONAL for this project per the Iterative Excellence principle (manual testing for MVP). Test tasks are NOT included in this task list.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Android project**: `android/app/src/main/java/com/herpace/`
- **Data layer**: `android/app/src/main/java/com/herpace/data/`
- **Domain layer**: `android/app/src/main/java/com/herpace/domain/`
- **Presentation layer**: `android/app/src/main/java/com/herpace/presentation/`
- **Resources**: `android/app/src/main/res/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create Android Studio project with Empty Activity (Compose) template in android/ directory per quickstart.md
- [x] T002 Configure project-level build.gradle.kts with Kotlin, Compose, Hilt, KSP plugins
- [x] T003 Configure app-level build.gradle.kts with all dependencies (Compose, Hilt, Retrofit, Room, WorkManager, Firebase, Health Connect) per quickstart.md
- [x] T004 [P] Create gradle/libs.versions.toml version catalog with all library versions per quickstart.md
- [x] T005 [P] Create HerPaceApplication.kt with @HiltAndroidApp annotation in android/app/src/main/java/com/herpace/
- [x] T006 Update AndroidManifest.xml to reference HerPaceApplication and add INTERNET and POST_NOTIFICATIONS permissions
- [x] T007 [P] Create project directory structure (data/, domain/, presentation/, di/, notification/, util/) in android/app/src/main/java/com/herpace/
- [x] T008 [P] Configure ProGuard rules for release builds in android/app/proguard-rules.pro
- [x] T009 [P] Add string resources for app name and common labels in android/app/src/main/res/values/strings.xml
- [x] T010 [P] Configure Material Design 3 theme in android/app/src/main/java/com/herpace/presentation/theme/

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Network Infrastructure

- [x] T011 Create Json provider in android/app/src/main/java/com/herpace/di/NetworkModule.kt with kotlinx.serialization configuration
- [x] T012 Create AuthTokenProvider interface in android/app/src/main/java/com/herpace/data/repository/AuthTokenProvider.kt
- [x] T013 Implement AuthTokenProviderImpl with EncryptedSharedPreferences in android/app/src/main/java/com/herpace/data/repository/AuthTokenProviderImpl.kt
- [x] T014 Create OkHttpClient provider with JWT interceptor and logging in android/app/src/main/java/com/herpace/di/NetworkModule.kt
- [x] T015 Create Retrofit provider with base URL configuration in android/app/src/main/java/com/herpace/di/NetworkModule.kt
- [x] T016 Create HerPaceApiService interface with all endpoints per contracts/api-endpoints.md in android/app/src/main/java/com/herpace/data/remote/HerPaceApiService.kt
- [x] T017 [P] Create API DTOs (SignupRequest, SignupResponse, LoginRequest, LoginResponse) in android/app/src/main/java/com/herpace/data/remote/dto/
- [x] T018 [P] Create ApiResult sealed class for network error handling in android/app/src/main/java/com/herpace/data/remote/ApiResult.kt
- [x] T019 [P] Create safeApiCall extension function for error handling in android/app/src/main/java/com/herpace/data/remote/SafeApiCall.kt

### Database Infrastructure

- [x] T020 Create Converters class with TypeConverters for Instant, LocalDate, LocalTime, and Enums in android/app/src/main/java/com/herpace/data/local/Converters.kt
- [x] T021 Create HerPaceDatabase abstract class with @Database annotation in android/app/src/main/java/com/herpace/data/local/HerPaceDatabase.kt
- [x] T022 Create DatabaseModule providing Room database instance with SQLCipher in android/app/src/main/java/com/herpace/di/DatabaseModule.kt
- [x] T023 [P] Create SyncStatus enum (NotSynced, Syncing, Synced, SyncFailed) in android/app/src/main/java/com/herpace/data/local/SyncStatus.kt

### Domain Enums and Constants

- [x] T024 [P] Create FitnessLevel enum (Beginner, Intermediate, Advanced) in android/app/src/main/java/com/herpace/domain/model/FitnessLevel.kt
- [x] T025 [P] Create RaceDistance enum (FiveK, TenK, HalfMarathon, Marathon) in android/app/src/main/java/com/herpace/domain/model/RaceDistance.kt
- [x] T026 [P] Create WorkoutType enum (EasyRun, LongRun, TempoRun, Intervals, RestDay) in android/app/src/main/java/com/herpace/domain/model/WorkoutType.kt
- [x] T027 [P] Create IntensityLevel enum (Low, Moderate, High) in android/app/src/main/java/com/herpace/domain/model/IntensityLevel.kt
- [x] T028 [P] Create CyclePhase enum (Menstrual, Follicular, Ovulatory, Luteal) in android/app/src/main/java/com/herpace/domain/model/CyclePhase.kt
- [x] T029 [P] Create FitnessPlatform enum (Strava, Garmin, HealthConnect) in android/app/src/main/java/com/herpace/domain/model/FitnessPlatform.kt

### Navigation Infrastructure

- [x] T030 Create MainActivity with NavHost setup in android/app/src/main/java/com/herpace/MainActivity.kt
- [x] T031 Create NavGraph.kt with Navigation Component routes in android/app/src/main/java/com/herpace/presentation/navigation/NavGraph.kt
- [x] T032 Create Screen sealed class defining all navigation destinations in android/app/src/main/java/com/herpace/presentation/navigation/Screen.kt

### Common UI Components

- [x] T033 [P] Create LoadingIndicator composable in android/app/src/main/java/com/herpace/presentation/common/LoadingIndicator.kt
- [x] T034 [P] Create ErrorMessage composable in android/app/src/main/java/com/herpace/presentation/common/ErrorMessage.kt
- [x] T035 [P] Create HerPaceButton composable with Material Design 3 styling in android/app/src/main/java/com/herpace/presentation/common/HerPaceButton.kt
- [x] T036 [P] Create HerPaceTextField composable with validation support in android/app/src/main/java/com/herpace/presentation/common/HerPaceTextField.kt

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Account Creation and Onboarding (Priority: P1) 🎯 MVP

**Goal**: Enable new users to create accounts and complete their runner profile including menstrual cycle information

**Independent Test**: Create a new account, enter profile details (age, fitness level, cycle length, last period date), verify profile is saved and retrievable on app restart

### Domain Layer (User Story 1)

- [x] T037 [P] [US1] Create User domain model in android/app/src/main/java/com/herpace/domain/model/User.kt
- [x] T038 [P] [US1] Create RunnerProfile domain model in android/app/src/main/java/com/herpace/domain/model/RunnerProfile.kt
- [x] T039 [P] [US1] Create UserEntity (Room) in android/app/src/main/java/com/herpace/data/local/entity/UserEntity.kt
- [x] T040 [P] [US1] Create RunnerProfileEntity (Room) in android/app/src/main/java/com/herpace/data/local/entity/RunnerProfileEntity.kt
- [x] T041 [P] [US1] Create RunnerProfileResponse DTO in android/app/src/main/java/com/herpace/data/remote/dto/RunnerProfileResponse.kt
- [x] T042 [P] [US1] Create RunnerProfileRequest DTO in android/app/src/main/java/com/herpace/data/remote/dto/RunnerProfileRequest.kt

### Data Layer (User Story 1)

- [x] T043 [US1] Create UserDao with insert, get, and delete operations in android/app/src/main/java/com/herpace/data/local/dao/UserDao.kt
- [x] T044 [US1] Create RunnerProfileDao with insert, get, and update operations in android/app/src/main/java/com/herpace/data/local/dao/RunnerProfileDao.kt
- [x] T045 [US1] Update HerPaceDatabase to include UserDao and RunnerProfileDao
- [x] T046 [US1] Create AuthRepository interface in android/app/src/main/java/com/herpace/domain/repository/AuthRepository.kt
- [x] T047 [US1] Create ProfileRepository interface in android/app/src/main/java/com/herpace/domain/repository/ProfileRepository.kt
- [x] T048 [US1] Implement AuthRepositoryImpl with signup, login, logout operations in android/app/src/main/java/com/herpace/data/repository/AuthRepositoryImpl.kt
- [x] T049 [US1] Implement ProfileRepositoryImpl with save and get profile operations in android/app/src/main/java/com/herpace/data/repository/ProfileRepositoryImpl.kt
- [x] T050 [US1] Create RepositoryModule providing repository implementations in android/app/src/main/java/com/herpace/di/RepositoryModule.kt

### Use Cases (User Story 1)

- [x] T051 [P] [US1] Create SignupUseCase in android/app/src/main/java/com/herpace/domain/usecase/SignupUseCase.kt
- [x] T052 [P] [US1] Create LoginUseCase in android/app/src/main/java/com/herpace/domain/usecase/LoginUseCase.kt
- [x] T053 [P] [US1] Create SaveProfileUseCase with validation in android/app/src/main/java/com/herpace/domain/usecase/SaveProfileUseCase.kt
- [x] T054 [P] [US1] Create GetProfileUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetProfileUseCase.kt
- [x] T055 [P] [US1] Create LogoutUseCase in android/app/src/main/java/com/herpace/domain/usecase/LogoutUseCase.kt

### Presentation Layer - Auth (User Story 1)

- [x] T056 [US1] Create AuthUiState data class in android/app/src/main/java/com/herpace/presentation/auth/AuthUiState.kt
- [x] T057 [US1] Create LoginViewModel with StateFlow in android/app/src/main/java/com/herpace/presentation/auth/LoginViewModel.kt
- [x] T058 [US1] Create LoginScreen composable with email/password fields and validation in android/app/src/main/java/com/herpace/presentation/auth/LoginScreen.kt
- [x] T059 [US1] Create SignupViewModel with StateFlow in android/app/src/main/java/com/herpace/presentation/auth/SignupViewModel.kt
- [x] T060 [US1] Create SignupScreen composable with email, password, confirm password fields in android/app/src/main/java/com/herpace/presentation/auth/SignupScreen.kt

### Presentation Layer - Onboarding (User Story 1)

- [x] T061 [US1] Create OnboardingUiState data class in android/app/src/main/java/com/herpace/presentation/auth/OnboardingUiState.kt
- [x] T062 [US1] Create OnboardingViewModel with profile data StateFlow in android/app/src/main/java/com/herpace/presentation/auth/OnboardingViewModel.kt
- [x] T063 [US1] Create OnboardingScreen composable with all profile fields (name, age, fitness level, weekly mileage, cycle length, last period date, notification preferences) in android/app/src/main/java/com/herpace/presentation/auth/OnboardingScreen.kt
- [x] T064 [US1] Add validation logic to OnboardingViewModel (age 13-120, cycle length 21-40, etc.)
- [x] T065 [US1] Create FitnessLevelPicker composable for selecting fitness level in android/app/src/main/java/com/herpace/presentation/common/FitnessLevelPicker.kt
- [x] T066 [US1] Create DatePicker composable for last period start date in android/app/src/main/java/com/herpace/presentation/common/DatePicker.kt

### Navigation & Session Management (User Story 1)

- [x] T067 [US1] Update NavGraph to include Login, Signup, and Onboarding destinations
- [x] T068 [US1] Implement automatic login check in MainActivity (if token exists, navigate to Dashboard)
- [x] T069 [US1] Add navigation from Signup → Onboarding → Dashboard in OnboardingViewModel
- [x] T070 [US1] Add navigation from Login → Dashboard (skip onboarding if profile exists) in LoginViewModel

**Checkpoint**: At this point, User Story 1 should be fully functional - users can create accounts, complete onboarding, and automatically log back in on app restart

---

## Phase 4: User Story 2 - Create and Manage Race Goals (Priority: P1)

**Goal**: Enable users to add, view, edit, and delete races with details like distance, date, and goal time

**Independent Test**: Create a race (e.g., "Spring Marathon - April 15, 2026 - 42.2km - Goal: 4:00:00"), view it in a list, edit details, delete race

### Domain Layer (User Story 2)

- [x] T071 [P] [US2] Create Race domain model in android/app/src/main/java/com/herpace/domain/model/Race.kt
- [x] T072 [P] [US2] Create RaceEntity (Room) with foreign key to UserEntity in android/app/src/main/java/com/herpace/data/local/entity/RaceEntity.kt
- [x] T073 [P] [US2] Create RaceResponse DTO in android/app/src/main/java/com/herpace/data/remote/dto/RaceResponse.kt
- [x] T074 [P] [US2] Create CreateRaceRequest DTO in android/app/src/main/java/com/herpace/data/remote/dto/CreateRaceRequest.kt
- [x] T075 [P] [US2] Create UpdateRaceRequest DTO in android/app/src/main/java/com/herpace/data/remote/dto/UpdateRaceRequest.kt

### Data Layer (User Story 2)

- [x] T076 [US2] Create RaceDao with CRUD operations and getAll sorted by date in android/app/src/main/java/com/herpace/data/local/dao/RaceDao.kt
- [x] T077 [US2] Update HerPaceDatabase to include RaceDao
- [x] T078 [US2] Create RaceRepository interface in android/app/src/main/java/com/herpace/domain/repository/RaceRepository.kt
- [x] T079 [US2] Implement RaceRepositoryImpl with create, getAll, getById, update, delete operations in android/app/src/main/java/com/herpace/data/repository/RaceRepositoryImpl.kt
- [x] T080 [US2] Update RepositoryModule to provide RaceRepository

### Use Cases (User Story 2)

- [x] T081 [P] [US2] Create CreateRaceUseCase with validation (date in future, name 1-200 chars) in android/app/src/main/java/com/herpace/domain/usecase/CreateRaceUseCase.kt
- [x] T082 [P] [US2] Create GetRacesUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetRacesUseCase.kt
- [x] T083 [P] [US2] Create GetRaceByIdUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetRaceByIdUseCase.kt
- [x] T084 [P] [US2] Create UpdateRaceUseCase with validation in android/app/src/main/java/com/herpace/domain/usecase/UpdateRaceUseCase.kt
- [x] T085 [P] [US2] Create DeleteRaceUseCase in android/app/src/main/java/com/herpace/domain/usecase/DeleteRaceUseCase.kt

### Presentation Layer (User Story 2)

- [x] T086 [US2] Create RaceUiState data class in android/app/src/main/java/com/herpace/presentation/races/RaceUiState.kt
- [x] T087 [US2] Create RacesViewModel with StateFlow<List<Race>> in android/app/src/main/java/com/herpace/presentation/races/RacesViewModel.kt
- [x] T088 [US2] Create RacesListScreen composable displaying races sorted by date in android/app/src/main/java/com/herpace/presentation/races/RacesListScreen.kt
- [x] T089 [US2] Create RaceCard composable showing race details (name, date, distance, goal time) in android/app/src/main/java/com/herpace/presentation/races/RaceCard.kt
- [x] T090 [US2] Create AddEditRaceViewModel with form state and validation in android/app/src/main/java/com/herpace/presentation/races/AddEditRaceViewModel.kt
- [x] T091 [US2] Create AddEditRaceScreen composable with form fields (name, date, distance, goal time) in android/app/src/main/java/com/herpace/presentation/races/AddEditRaceScreen.kt
- [x] T092 [US2] Create RaceDistancePicker composable for selecting race distance in android/app/src/main/java/com/herpace/presentation/common/RaceDistancePicker.kt
- [x] T093 [US2] Add delete confirmation dialog to RaceCard in android/app/src/main/java/com/herpace/presentation/races/DeleteRaceDialog.kt

### Navigation (User Story 2)

- [x] T094 [US2] Update NavGraph to include RacesList, AddRace, and EditRace destinations
- [x] T095 [US2] Add navigation from Dashboard to RacesList
- [x] T096 [US2] Add navigation from RacesList to AddRace and EditRace
- [x] T097 [US2] Add bottom navigation bar to MainActivity with Dashboard and Races tabs in android/app/src/main/java/com/herpace/presentation/navigation/BottomNavBar.kt

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently - users can create accounts and manage races

---

## Phase 5: User Story 3 - Generate Hormone-Aware Training Plan (Priority: P1)

**Goal**: Enable users to select a race and generate a personalized training plan that adapts workouts based on menstrual cycle phases

**Independent Test**: Select an existing race, tap "Generate Plan", wait for AI generation, view resulting weekly training schedule with cycle phase indicators

### Domain Layer (User Story 3)

- [x] T098 [P] [US3] Create TrainingPlan domain model in android/app/src/main/java/com/herpace/domain/model/TrainingPlan.kt
- [x] T099 [P] [US3] Create TrainingSession domain model in android/app/src/main/java/com/herpace/domain/model/TrainingSession.kt
- [x] T100 [P] [US3] Create TrainingPlanEntity (Room) with foreign key to RaceEntity in android/app/src/main/java/com/herpace/data/local/entity/TrainingPlanEntity.kt
- [x] T101 [P] [US3] Create TrainingSessionEntity (Room) with foreign key to TrainingPlanEntity in android/app/src/main/java/com/herpace/data/local/entity/TrainingSessionEntity.kt
- [x] T102 [P] [US3] Create TrainingPlanResponse DTO in android/app/src/main/java/com/herpace/data/remote/dto/TrainingPlanResponse.kt
- [x] T103 [P] [US3] Create TrainingSessionResponse DTO in android/app/src/main/java/com/herpace/data/remote/dto/TrainingSessionResponse.kt
- [x] T104 [P] [US3] Create GeneratePlanRequest DTO in android/app/src/main/java/com/herpace/data/remote/dto/GeneratePlanRequest.kt

### Data Layer (User Story 3)

- [x] T105 [US3] Create TrainingPlanDao with insert, getActivePlan, and getByRaceId in android/app/src/main/java/com/herpace/data/local/dao/TrainingPlanDao.kt
- [x] T106 [US3] Create TrainingSessionDao with insertAll, getByPlanId, and getByDate in android/app/src/main/java/com/herpace/data/local/dao/TrainingSessionDao.kt
- [x] T107 [US3] Update HerPaceDatabase to include TrainingPlanDao and TrainingSessionDao
- [x] T108 [US3] Create TrainingPlanRepository interface in android/app/src/main/java/com/herpace/domain/repository/TrainingPlanRepository.kt
- [x] T109 [US3] Implement TrainingPlanRepositoryImpl with generatePlan, getActivePlan, getSessionsByDate operations in android/app/src/main/java/com/herpace/data/repository/TrainingPlanRepositoryImpl.kt
- [x] T110 [US3] Update RepositoryModule to provide TrainingPlanRepository

### Use Cases (User Story 3)

- [x] T111 [P] [US3] Create GenerateTrainingPlanUseCase with 30-second timeout handling in android/app/src/main/java/com/herpace/domain/usecase/GenerateTrainingPlanUseCase.kt
- [x] T112 [P] [US3] Create GetActiveTrainingPlanUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetActiveTrainingPlanUseCase.kt
- [x] T113 [P] [US3] Create GetSessionsByWeekUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetSessionsByWeekUseCase.kt

### Presentation Layer (User Story 3)

- [x] T114 [US3] Create TrainingPlanUiState data class in android/app/src/main/java/com/herpace/presentation/plan/TrainingPlanUiState.kt
- [x] T115 [US3] Create TrainingPlanViewModel with plan generation state (Loading, Success, Error) in android/app/src/main/java/com/herpace/presentation/plan/TrainingPlanViewModel.kt
- [x] T116 [US3] Create GeneratePlanButton composable in RaceCard with confirmation dialog in android/app/src/main/java/com/herpace/presentation/races/GeneratePlanButton.kt
- [x] T117 [US3] Create TrainingPlanScreen composable with weekly calendar view in android/app/src/main/java/com/herpace/presentation/plan/TrainingPlanScreen.kt
- [x] T118 [US3] Create WeekCard composable showing sessions for one week in android/app/src/main/java/com/herpace/presentation/plan/WeekCard.kt
- [x] T119 [US3] Create SessionCard composable with workout type, distance, and cycle phase indicator in android/app/src/main/java/com/herpace/presentation/plan/SessionCard.kt
- [x] T120 [US3] Create CyclePhaseIndicator composable with icon and color in android/app/src/main/java/com/herpace/presentation/common/CyclePhaseIndicator.kt
- [x] T121 [US3] Add loading indicator for plan generation (10-30 seconds) to TrainingPlanScreen
- [x] T122 [US3] Add error handling with retry option to TrainingPlanViewModel

### Navigation (User Story 3)

- [x] T123 [US3] Update NavGraph to include TrainingPlan destination with raceId parameter
- [x] T124 [US3] Add navigation from RaceCard "Generate Plan" button to TrainingPlanScreen
- [x] T125 [US3] Add "Training Plan" tab to bottom navigation bar

**Checkpoint**: At this point, User Stories 1, 2, AND 3 should all work independently - MVP is complete with auth, races, and AI plan generation

---

## Phase 6: User Story 4 - View Daily Training Sessions (Priority: P2)

**Goal**: Enable users to view their training schedule for each day with workout details, intensity, and cycle phase alignment

**Independent Test**: Navigate to calendar/dashboard view, select different dates, view session details (workout type, distance, pace, notes)

### Use Cases (User Story 4)

- [x] T126 [P] [US4] Create GetSessionByDateUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetSessionByDateUseCase.kt
- [x] T127 [P] [US4] Create GetTodaySessionUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetTodaySessionUseCase.kt

### Presentation Layer (User Story 4)

- [x] T128 [US4] Create DashboardUiState data class in android/app/src/main/java/com/herpace/presentation/dashboard/DashboardUiState.kt
- [x] T129 [US4] Create DashboardViewModel with today's session StateFlow in android/app/src/main/java/com/herpace/presentation/dashboard/DashboardViewModel.kt
- [x] T130 [US4] Create DashboardScreen composable showing today's workout prominently in android/app/src/main/java/com/herpace/presentation/dashboard/DashboardScreen.kt
- [x] T131 [US4] Create TodayWorkoutCard composable with detailed session information in android/app/src/main/java/com/herpace/presentation/dashboard/TodayWorkoutCard.kt
- [x] T132 [US4] Create SessionDetailUiState data class in android/app/src/main/java/com/herpace/presentation/session/SessionDetailUiState.kt
- [x] T133 [US4] Create SessionDetailViewModel in android/app/src/main/java/com/herpace/presentation/session/SessionDetailViewModel.kt
- [x] T134 [US4] Create SessionDetailScreen composable with full workout details (type, distance, pace, notes, cycle phase) in android/app/src/main/java/com/herpace/presentation/session/SessionDetailScreen.kt
- [x] T135 [US4] Create CalendarView composable for selecting dates in android/app/src/main/java/com/herpace/presentation/plan/CalendarView.kt
- [x] T136 [US4] Add rest day display with recovery guidance to SessionDetailScreen

### Navigation (User Story 4)

- [ ] T137 [US4] Update NavGraph to include SessionDetail destination with sessionId parameter
- [ ] T138 [US4] Add navigation from DashboardScreen to SessionDetailScreen when tapping today's workout
- [ ] T139 [US4] Add navigation from TrainingPlanScreen SessionCard to SessionDetailScreen

**Checkpoint**: User Story 4 complete - users can view daily training sessions with full details

---

## Phase 7: User Story 5 - Complete and Track Workouts (Priority: P2)

**Goal**: Enable users to mark workouts as completed and optionally log actual performance data

**Independent Test**: Mark a session as complete, optionally enter actual distance/time/effort, verify completion persists and shows in plan view

### Domain Layer (User Story 5)

- [ ] T140 [P] [US5] Create WorkoutLog domain model in android/app/src/main/java/com/herpace/domain/model/WorkoutLog.kt
- [ ] T141 [P] [US5] Create WorkoutLogEntity (Room) with foreign key to TrainingSessionEntity in android/app/src/main/java/com/herpace/data/local/entity/WorkoutLogEntity.kt

### Data Layer (User Story 5)

- [ ] T142 [US5] Create WorkoutLogDao with insert, getBySessionId, and delete operations in android/app/src/main/java/com/herpace/data/local/dao/WorkoutLogDao.kt
- [ ] T143 [US5] Update HerPaceDatabase to include WorkoutLogDao
- [ ] T144 [US5] Update TrainingSessionEntity to include completed boolean and completedAt timestamp
- [ ] T145 [US5] Create WorkoutLogRepository interface in android/app/src/main/java/com/herpace/domain/repository/WorkoutLogRepository.kt
- [ ] T146 [US5] Implement WorkoutLogRepositoryImpl in android/app/src/main/java/com/herpace/data/repository/WorkoutLogRepositoryImpl.kt
- [ ] T147 [US5] Update RepositoryModule to provide WorkoutLogRepository

### Use Cases (User Story 5)

- [ ] T148 [P] [US5] Create MarkSessionCompleteUseCase in android/app/src/main/java/com/herpace/domain/usecase/MarkSessionCompleteUseCase.kt
- [ ] T149 [P] [US5] Create LogWorkoutDetailsUseCase with validation (distance 0-100km, duration 1-600min, effort 1-10) in android/app/src/main/java/com/herpace/domain/usecase/LogWorkoutDetailsUseCase.kt
- [ ] T150 [P] [US5] Create UndoSessionCompletionUseCase in android/app/src/main/java/com/herpace/domain/usecase/UndoSessionCompletionUseCase.kt

### Presentation Layer (User Story 5)

- [ ] T151 [US5] Update SessionDetailViewModel to include markComplete, logDetails, and undoCompletion actions
- [ ] T152 [US5] Add "Mark as Complete" button to SessionDetailScreen
- [ ] T153 [US5] Create LogWorkoutDialog composable with optional fields (actual distance, time, perceived effort 1-10) in android/app/src/main/java/com/herpace/presentation/session/LogWorkoutDialog.kt
- [ ] T154 [US5] Update SessionCard to show completion checkmark for completed sessions
- [ ] T155 [US5] Update TrainingPlanScreen to visually distinguish completed sessions (e.g., grayed out, checkmark badge)
- [ ] T156 [US5] Add "Undo Completion" option to completed sessions in SessionDetailScreen

**Checkpoint**: User Story 5 complete - users can mark workouts complete and log performance data

---

## Phase 8: User Story 6 - Receive Training Reminders (Priority: P2)

**Goal**: Enable users to receive push notifications for upcoming workouts with motivational messages

**Independent Test**: Enable notifications, schedule them for specific times, verify device receives notifications with correct workout details

### Firebase & Notification Setup

- [ ] T157 [US6] Create Firebase project and add google-services.json to android/app/
- [ ] T158 [US6] Add Firebase Cloud Messaging service to AndroidManifest.xml
- [ ] T159 [US6] Create HerPaceFirebaseMessagingService extending FirebaseMessagingService in android/app/src/main/java/com/herpace/notification/HerPaceFirebaseMessagingService.kt
- [ ] T160 [US6] Implement onMessageReceived to handle notification display in HerPaceFirebaseMessagingService
- [ ] T161 [US6] Implement onNewToken to update FCM token on server in HerPaceFirebaseMessagingService

### Domain Layer (User Story 6)

- [ ] T162 [P] [US6] Create NotificationSchedule domain model in android/app/src/main/java/com/herpace/domain/model/NotificationSchedule.kt
- [ ] T163 [P] [US6] Create NotificationScheduleEntity (Room) in android/app/src/main/java/com/herpace/data/local/entity/NotificationScheduleEntity.kt

### Data Layer (User Story 6)

- [ ] T164 [US6] Create NotificationScheduleDao in android/app/src/main/java/com/herpace/data/local/dao/NotificationScheduleDao.kt
- [ ] T165 [US6] Update HerPaceDatabase to include NotificationScheduleDao
- [ ] T166 [US6] Create NotificationRepository interface in android/app/src/main/java/com/herpace/domain/repository/NotificationRepository.kt
- [ ] T167 [US6] Implement NotificationRepositoryImpl in android/app/src/main/java/com/herpace/data/repository/NotificationRepositoryImpl.kt
- [ ] T168 [US6] Update RepositoryModule to provide NotificationRepository

### WorkManager Notification Scheduling

- [ ] T169 [US6] Create WorkoutReminderWorker extending Worker in android/app/src/main/java/com/herpace/notification/WorkoutReminderWorker.kt
- [ ] T170 [US6] Implement doWork to fetch tomorrow's/today's session and show notification in WorkoutReminderWorker
- [ ] T171 [US6] Create NotificationScheduler utility class for WorkManager scheduling in android/app/src/main/java/com/herpace/notification/NotificationScheduler.kt
- [ ] T172 [US6] Implement scheduleEveningReminder (6 PM day before workout) in NotificationScheduler
- [ ] T173 [US6] Implement scheduleMorningReminder (7 AM day of workout) in NotificationScheduler
- [ ] T174 [US6] Add deep link handling in MainActivity to navigate to SessionDetailScreen when tapping notification

### Use Cases (User Story 6)

- [ ] T175 [P] [US6] Create GetNotificationScheduleUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetNotificationScheduleUseCase.kt
- [ ] T176 [P] [US6] Create UpdateNotificationScheduleUseCase in android/app/src/main/java/com/herpace/domain/usecase/UpdateNotificationScheduleUseCase.kt

### Presentation Layer (User Story 6)

- [ ] T177 [US6] Update RunnerProfileEntity to include notificationScheduleId
- [ ] T178 [US6] Create NotificationSettingsScreen composable with enable toggle and time pickers in android/app/src/main/java/com/herpace/presentation/profile/NotificationSettingsScreen.kt
- [ ] T179 [US6] Create NotificationSettingsViewModel in android/app/src/main/java/com/herpace/presentation/profile/NotificationSettingsViewModel.kt
- [ ] T180 [US6] Update settings when user changes notification preferences and reschedule WorkManager jobs
- [ ] T181 [US6] Add notification permission request to OnboardingScreen for Android 13+

### Navigation (User Story 6)

- [ ] T182 [US6] Update NavGraph to include NotificationSettings destination
- [ ] T183 [US6] Add navigation from ProfileScreen to NotificationSettingsScreen

**Checkpoint**: User Story 6 complete - users receive push notifications for upcoming workouts

---

## Phase 9: User Story 7 - Manage Cycle Data (Priority: P3)

**Goal**: Enable users to update cycle information to keep predictions accurate

**Independent Test**: Navigate to profile settings, update cycle information (period start date, cycle length), verify future session cycle phases update

### Use Cases (User Story 7)

- [ ] T184 [P] [US7] Create UpdateCycleDataUseCase with validation in android/app/src/main/java/com/herpace/domain/usecase/UpdateCycleDataUseCase.kt
- [ ] T185 [P] [US7] Create RecalculateCyclePhasesUseCase in android/app/src/main/java/com/herpace/domain/usecase/RecalculateCyclePhasesUseCase.kt

### Presentation Layer (User Story 7)

- [ ] T186 [US7] Create ProfileUiState data class in android/app/src/main/java/com/herpace/presentation/profile/ProfileUiState.kt
- [ ] T187 [US7] Create ProfileViewModel with profile data StateFlow in android/app/src/main/java/com/herpace/presentation/profile/ProfileViewModel.kt
- [ ] T188 [US7] Create ProfileScreen composable showing profile details in android/app/src/main/java/com/herpace/presentation/profile/ProfileScreen.kt
- [ ] T189 [US7] Create CycleTrackingScreen composable with cycle length and last period date fields in android/app/src/main/java/com/herpace/presentation/profile/CycleTrackingScreen.kt
- [ ] T190 [US7] Create CycleTrackingViewModel in android/app/src/main/java/com/herpace/presentation/profile/CycleTrackingViewModel.kt
- [ ] T191 [US7] Add "Log Period Start" quick action to ProfileScreen
- [ ] T192 [US7] Implement cycle phase recalculation when user updates cycle data in CycleTrackingViewModel
- [ ] T193 [US7] Add gentle reminder dialog when user hasn't logged period in 60+ days to DashboardScreen

### Navigation (User Story 7)

- [ ] T194 [US7] Update NavGraph to include Profile and CycleTracking destinations
- [ ] T195 [US7] Add "Profile" tab to bottom navigation bar
- [ ] T196 [US7] Add navigation from ProfileScreen to CycleTrackingScreen

**Checkpoint**: User Story 7 complete - users can manage cycle data and see updated predictions

---

## Phase 10: User Story 8 - Sync Data Across Sessions (Priority: P3)

**Goal**: Enable automatic cloud sync so users can access data across devices or after reinstallation

**Independent Test**: Create data (race, complete workout), logout or clear app data, login again, verify all data is restored from server

### Sync Infrastructure

- [ ] T197 [US8] Create SyncWorker extending Worker for background sync in android/app/src/main/java/com/herpace/data/sync/SyncWorker.kt
- [ ] T198 [US8] Implement syncPendingChanges to upload all NOT_SYNCED entities in SyncWorker
- [ ] T199 [US8] Implement fetchServerData to download latest data from server in SyncWorker
- [ ] T200 [US8] Create SyncManager utility class in android/app/src/main/java/com/herpace/data/sync/SyncManager.kt
- [ ] T201 [US8] Implement scheduleSyncWork with constraints (network available) in SyncManager
- [ ] T202 [US8] Add sync status metadata updates to all repository write operations (create, update, delete)

### Use Cases (User Story 8)

- [ ] T203 [P] [US8] Create SyncDataUseCase in android/app/src/main/java/com/herpace/domain/usecase/SyncDataUseCase.kt
- [ ] T204 [P] [US8] Create GetSyncStatusUseCase in android/app/src/main/java/com/herpace/domain/usecase/GetSyncStatusUseCase.kt

### Presentation Layer (User Story 8)

- [ ] T205 [US8] Add sync status indicator to DashboardScreen showing last sync time
- [ ] T206 [US8] Add manual sync button to ProfileScreen
- [ ] T207 [US8] Implement conflict resolution (server wins) when sync conflict detected
- [ ] T208 [US8] Add sync notification to user when local changes are overwritten by server data
- [ ] T209 [US8] Show offline indicator when no network connectivity in DashboardScreen

### Lifecycle Integration (User Story 8)

- [ ] T210 [US8] Trigger sync when app comes to foreground in MainActivity
- [ ] T211 [US8] Trigger sync after user makes changes (create race, complete workout, update profile)
- [ ] T212 [US8] Queue changes for later sync when offline in all repositories

**Checkpoint**: User Story 8 complete - data automatically syncs with backend, accessible across devices

---

## Phase 11: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

### Accessibility

- [ ] T213 [P] Add contentDescription to all ImageViews, Icons, and non-text UI elements
- [ ] T214 [P] Verify minimum touch target size (48dp) for all interactive elements
- [ ] T215 [P] Add semantic modifiers to all Compose components for TalkBack support
- [ ] T216 [P] Test app with TalkBack enabled and fix accessibility issues
- [ ] T217 [P] Ensure color contrast ratios meet WCAG 2.1 AA standards (4.5:1 normal text, 3:1 large text)

### Performance & Optimization

- [ ] T218 [P] Add ProGuard/R8 rules for release builds in android/app/proguard-rules.pro
- [ ] T219 [P] Optimize Room database queries with indices on frequently queried columns
- [ ] T220 [P] Add pagination for large lists (races, training sessions)
- [ ] T221 [P] Implement image caching for user avatars (if added)

### Error Handling & Logging

- [ ] T222 [P] Add Firebase Crashlytics for crash reporting
- [ ] T223 [P] Add Firebase Analytics for user behavior tracking
- [ ] T224 [P] Implement comprehensive error logging in all ViewModels
- [ ] T225 [P] Add retry logic with exponential backoff for failed API requests

### Security

- [ ] T226 [P] Verify all sensitive data uses EncryptedSharedPreferences
- [ ] T227 [P] Verify Room database is encrypted with SQLCipher
- [ ] T228 [P] Add certificate pinning for production API in OkHttpClient
- [ ] T229 [P] Implement biometric authentication for app lock (optional enhancement)

### Documentation & Testing

- [ ] T230 [P] Create README.md for android/ directory with setup instructions
- [ ] T231 [P] Update quickstart.md based on actual implementation experience
- [ ] T232 [P] Document API integration patterns in docs/
- [ ] T233 [P] Run manual testing through all 8 user stories per quickstart.md
- [ ] T234 [P] Verify app works on multiple Android versions (API 26, 30, 34)

### Final QA

- [ ] T235 Test app on physical device with various screen sizes
- [ ] T236 Test app in airplane mode (offline functionality)
- [ ] T237 Verify notification delivery on different Android versions
- [ ] T238 Test deep links from notifications
- [ ] T239 Verify data persistence across app restarts
- [ ] T240 Load test with large dataset (50+ races, 200+ training sessions)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-10)**: All depend on Foundational phase completion
  - User Stories 1, 2, 3 (P1) are independent - can proceed in parallel after Foundation
  - User Story 4 (P2) depends on User Story 3 (needs training plan data)
  - User Story 5 (P2) depends on User Story 4 (needs session detail view)
  - User Story 6 (P2) is independent - can proceed in parallel
  - User Story 7 (P3) is independent - can proceed in parallel
  - User Story 8 (P3) is independent - can proceed in parallel
- **Polish (Phase 11)**: Depends on all desired user stories being complete

### User Story Dependencies

```
Foundation (Phase 2)
      ↓
   ┌──┴──┬──────┬──────┬──────┬──────┬──────┐
   ↓     ↓      ↓      ↓      ↓      ↓      ↓
 US1   US2    US6    US7    US8    US3    (independent)
  ↓     ↓                            ↓
  └─────┘                            ↓
                                   US4
                                     ↓
                                   US5
```

- **User Story 1 (P1)**: Independent - can start immediately after Foundation
- **User Story 2 (P1)**: Independent - can start immediately after Foundation
- **User Story 3 (P1)**: Independent - can start immediately after Foundation (requires US1 profile data but not implementation)
- **User Story 4 (P2)**: Depends on US3 (needs training plan and sessions)
- **User Story 5 (P2)**: Depends on US4 (needs session detail screen)
- **User Story 6 (P2)**: Independent - can start immediately after Foundation
- **User Story 7 (P3)**: Independent - can start immediately after Foundation
- **User Story 8 (P3)**: Independent - can start immediately after Foundation

### Within Each User Story

- Domain models (entities, DTOs) can be created in parallel [P]
- DAOs and Database updates are sequential (must update database class after creating DAOs)
- Repositories depend on DAOs being created
- Use cases depend on repositories
- ViewModels depend on use cases
- Screens depend on ViewModels
- Navigation updates are sequential within a story

### Parallel Opportunities

#### Phase 1 - Setup (All Parallel)
- T003, T004, T007, T008, T009, T010 can run simultaneously

#### Phase 2 - Foundational
- Network DTOs (T017), ApiResult (T018), SafeApiCall (T019) can run in parallel
- All domain enums (T024-T029) can run in parallel
- Common UI components (T033-T036) can run in parallel

#### Each User Story - Domain Layer
- All domain models and DTOs for a story can be created in parallel
- Example US1: T037, T038, T039, T040, T041, T042 in parallel
- Example US2: T071, T072, T073, T074, T075 in parallel

#### Each User Story - Use Cases
- All use cases within a story can be created in parallel after repository is done
- Example US1: T051, T052, T053, T054, T055 in parallel (after T048, T049)
- Example US2: T081, T082, T083, T084, T085 in parallel (after T079)

#### Polish Phase
- All accessibility tasks (T213-T217) in parallel
- All performance tasks (T218-T221) in parallel
- All error handling tasks (T222-T225) in parallel
- All security tasks (T226-T229) in parallel
- All documentation tasks (T230-T234) in parallel

---

## Parallel Example: User Story 1

```bash
# After Foundation complete, launch all domain models together:
Task T037: "Create User domain model"
Task T038: "Create RunnerProfile domain model"
Task T039: "Create UserEntity (Room)"
Task T040: "Create RunnerProfileEntity (Room)"
Task T041: "Create RunnerProfileResponse DTO"
Task T042: "Create RunnerProfileRequest DTO"

# After repositories complete, launch all use cases together:
Task T051: "Create SignupUseCase"
Task T052: "Create LoginUseCase"
Task T053: "Create SaveProfileUseCase"
Task T054: "Create GetProfileUseCase"
Task T055: "Create LogoutUseCase"
```

---

## Implementation Strategy

### MVP First (User Stories 1, 2, 3 Only - P1 Priority)

1. Complete Phase 1: Setup (T001-T010)
2. Complete Phase 2: Foundational (T011-T036) **CRITICAL** - blocks all stories
3. Complete Phase 3: User Story 1 - Auth & Onboarding (T037-T070)
4. **STOP and VALIDATE**: Test auth flow, create account, complete profile, verify persistence
5. Complete Phase 4: User Story 2 - Race Management (T071-T097)
6. **STOP and VALIDATE**: Create, view, edit, delete races
7. Complete Phase 5: User Story 3 - Training Plan Generation (T098-T125)
8. **STOP and VALIDATE**: Generate plan for a race, view weekly schedule with cycle phases
9. **Deploy MVP** - Core value delivered: Users can create accounts, add races, get hormone-aware training plans

### Incremental Delivery (Add P2 Features)

10. Complete Phase 6: User Story 4 - Daily Session View (T126-T139)
11. **STOP and VALIDATE**: View today's workout, navigate calendar, see session details
12. Complete Phase 7: User Story 5 - Workout Tracking (T140-T156)
13. **STOP and VALIDATE**: Mark workouts complete, log performance data
14. Complete Phase 8: User Story 6 - Notifications (T157-T183)
15. **STOP and VALIDATE**: Enable notifications, receive reminders, tap to navigate to session
16. **Deploy V2** - Enhanced engagement features added

### Full Feature Set (Add P3 Features)

17. Complete Phase 9: User Story 7 - Cycle Management (T184-T196)
18. **STOP and VALIDATE**: Update cycle data, verify phase predictions update
19. Complete Phase 10: User Story 8 - Cloud Sync (T197-T212)
20. **STOP and VALIDATE**: Create data, logout, login on different device, verify data restored
21. Complete Phase 11: Polish (T213-T240)
22. **Final QA**: Complete testing across all user stories
23. **Deploy V3** - Full feature parity with web app

### Parallel Team Strategy

With 3 developers after Foundation complete:

**Sprint 1 (MVP - P1 Stories)**:
- Developer A: User Story 1 (Auth & Onboarding)
- Developer B: User Story 2 (Race Management)
- Developer C: User Story 3 (Training Plan Generation)
- **Integration Point**: Test all 3 stories together as MVP

**Sprint 2 (Engagement - P2 Stories)**:
- Developer A: User Story 4 (Daily Sessions) + User Story 5 (Workout Tracking) - sequential dependency
- Developer B: User Story 6 (Notifications) - independent
- Developer C: Start Phase 11 (Accessibility, Performance)
- **Integration Point**: Test enhanced app with P1 + P2 features

**Sprint 3 (Polish - P3 Stories)**:
- Developer A: User Story 7 (Cycle Management)
- Developer B: User Story 8 (Cloud Sync)
- Developer C: Complete Phase 11 (Security, Documentation, QA)
- **Integration Point**: Final QA and production deployment

---

## Notes

- **[P] tasks**: Can run in parallel (different files, no dependencies within phase)
- **[Story] label**: Maps task to specific user story for traceability (US1-US8)
- **Sequential dependencies**: DAOs → Repository → Use Cases → ViewModels → Screens
- **Validation checkpoints**: Stop after each user story to test independently
- **MVP = User Stories 1, 2, 3**: Core value - auth, races, AI training plans
- **Avoid**: Starting user stories before Foundation complete (will cause rework)
- **Commit frequently**: After each task or logical group (e.g., all domain models for a story)
- **Deep links**: Implement early in US6 for notification navigation
- **Accessibility**: Test with TalkBack after each screen is complete, not just at end

---

## Task Summary

- **Total Tasks**: 240
- **Phase 1 (Setup)**: 10 tasks
- **Phase 2 (Foundational)**: 26 tasks (CRITICAL - blocks all stories)
- **Phase 3 (US1 - Auth/Onboarding - P1)**: 34 tasks
- **Phase 4 (US2 - Races - P1)**: 27 tasks
- **Phase 5 (US3 - Training Plans - P1)**: 28 tasks
- **Phase 6 (US4 - Daily Sessions - P2)**: 14 tasks
- **Phase 7 (US5 - Workout Tracking - P2)**: 17 tasks
- **Phase 8 (US6 - Notifications - P2)**: 27 tasks
- **Phase 9 (US7 - Cycle Management - P3)**: 13 tasks
- **Phase 10 (US8 - Cloud Sync - P3)**: 16 tasks
- **Phase 11 (Polish)**: 28 tasks

**Parallel Opportunities**:
- Setup: 6 parallel tasks
- Foundation: 15 parallel tasks across domain enums and UI components
- Per User Story: 4-6 parallel tasks (domain models, DTOs, use cases)
- Polish: 25 parallel tasks

**MVP Scope (P1 Stories)**:
- Setup + Foundation + US1 + US2 + US3 = 125 tasks
- Estimated timeline: 4-6 weeks for single developer, 2-3 weeks with parallel team

**Independent Testing**:
- Each user story has clear acceptance criteria from spec.md
- Checkpoints after each phase enable independent validation
- MVP can be deployed after Phase 5 with core value delivered
