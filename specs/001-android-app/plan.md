# Implementation Plan: HerPace Android App

**Branch**: `001-android-app` | **Date**: 2026-02-10 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-android-app/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

Build a native Android application in Kotlin that implements HerPace's hormone-aware training plan functionality. The app will provide authentication, profile management, race tracking, AI-generated training plans, workout logging, push notifications, and fitness platform integrations (Strava, Garmin, Google Fit). Core features include offline viewing of cached data and automatic workout import from connected platforms. The app reuses the existing HerPace backend API and follows Material Design guidelines for a native Android experience.

## Technical Context

**Language/Version**: Kotlin 1.9+ with Android Gradle Plugin 8.x
**Primary Dependencies**: Jetpack Compose (UI), Retrofit + OkHttp (networking), Hilt (DI), Room + SQLCipher (database), WorkManager (background tasks), Firebase (FCM, Crashlytics, Analytics), Health Connect API (Google Fit), Strava API v3, Garmin Connect APIs
**Storage**: Room database with SQLCipher encryption for local caching, existing PostgreSQL backend via REST API
**Testing**: JUnit 5 for unit tests, Compose Testing for UI tests, MockK for mocking, Turbine for Flow testing
**Target Platform**: Android API 26+ (Android 8.0 Oreo and above), targeting API 34
**Project Type**: Mobile (Android native application)
**Performance Goals**: <2s app launch, 60 FPS UI rendering, <30s training plan generation, <10s API calls
**Constraints**: <200MB app size, offline viewing support, battery-efficient background notifications, WCAG 2.1 AA accessibility
**Scale/Scope**: Single Android app, ~25-30 screens/fragments, 8 core user flows, 3 fitness platform integrations (Strava, Garmin, Health Connect)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### I. User-Centric Development ✅

**Status**: PASS

- ✅ User stories with Given-When-Then scenarios are defined (8 prioritized stories in spec)
- ✅ Features are independently testable (each story has Independent Test section)
- ✅ Success criteria are measurable and user-observable (10 success criteria with metrics)
- ✅ Edge cases are documented (8 edge cases identified in spec)

**Alignment**: The feature spec provides comprehensive user scenarios for all core flows (authentication, race management, plan generation, workout tracking, notifications, sync). Each scenario includes testable acceptance criteria.

### II. Accessibility-First Design ✅

**Status**: PASS

- ✅ Android accessibility requirements documented (TalkBack support in spec NFR section)
- ✅ Material Design components ensure semantic structure and keyboard navigation
- ✅ Accessibility review included in acceptance criteria (NFR section requires TalkBack testing)
- ⚠️ **Action Required**: Implement content descriptions, focus management, and accessibility services

**Alignment**: The spec includes non-functional requirements for accessibility (TalkBack screen reader support, clear visual hierarchy, responsive layouts). Implementation plan must ensure WCAG 2.1 AA compliance through Android accessibility APIs.

**Android-Specific Considerations**:
- Use `contentDescription` for all ImageViews and IconButtons
- Ensure minimum touch target size of 48dp × 48dp
- Provide text alternatives for visual cycle phase indicators
- Test with TalkBack and Switch Access
- Support dynamic text sizing (user font preferences)
- Ensure color contrast ratios meet 4.5:1 (normal text) and 3:1 (large text)

### III. Iterative Excellence ✅

**Status**: PASS

- ✅ User stories prioritized (P1, P2, P3) for incremental delivery
- ✅ MVP defined through P1 stories: auth/onboarding, race management, plan generation
- ✅ Each iteration delivers independently valuable functionality
- ✅ Tests optional in early iterations (constitution allows manual testing for MVP)

**Alignment**: The spec's prioritized user stories enable rapid iteration:
- **Iteration 1 (MVP)**: P1 stories - auth, profile, races, AI plan generation (core value)
- **Iteration 2**: P2 stories - daily sessions, workout logging, notifications (engagement)
- **Iteration 3**: P3 stories - cycle updates, cloud sync (polish)

**Re-evaluation Point**: After Phase 1 design, verify architecture supports iterative delivery without major refactoring between iterations.

## Project Structure

### Documentation (this feature)

```text
specs/001-android-app/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
│   └── api-endpoints.md # Backend API contract documentation
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
android/                           # New Android app project root
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/herpace/
│   │   │   │   ├── data/              # Data layer
│   │   │   │   │   ├── local/         # Room database, DAOs, entities
│   │   │   │   │   ├── remote/        # Retrofit API services, DTOs
│   │   │   │   │   ├── repository/    # Repository implementations
│   │   │   │   │   └── integrations/  # Fitness platform integrations
│   │   │   │   ├── domain/            # Domain layer
│   │   │   │   │   ├── model/         # Domain models
│   │   │   │   │   ├── repository/    # Repository interfaces
│   │   │   │   │   └── usecase/       # Business logic use cases
│   │   │   │   ├── presentation/      # Presentation layer (UI)
│   │   │   │   │   ├── auth/          # Login, signup, onboarding screens
│   │   │   │   │   ├── dashboard/     # Main dashboard
│   │   │   │   │   ├── races/         # Race management screens
│   │   │   │   │   ├── plan/          # Training plan screens
│   │   │   │   │   ├── session/       # Session details, workout logging
│   │   │   │   │   ├── profile/       # Profile and settings
│   │   │   │   │   └── common/        # Shared UI components
│   │   │   │   ├── di/                # Dependency injection modules (Hilt)
│   │   │   │   ├── notification/      # FCM and notification handling
│   │   │   │   └── util/              # Utilities, extensions
│   │   │   ├── res/                   # Android resources
│   │   │   │   ├── layout/            # XML layouts (if not using Compose)
│   │   │   │   ├── values/            # Strings, colors, themes
│   │   │   │   ├── drawable/          # Icons, images
│   │   │   │   └── navigation/        # Navigation graph
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                      # Unit tests
│   │   │   └── java/com/herpace/
│   │   │       ├── data/              # Repository tests
│   │   │       ├── domain/            # Use case tests
│   │   │       └── presentation/      # ViewModel tests
│   │   └── androidTest/               # Instrumented tests
│   │       └── java/com/herpace/
│   │           ├── ui/                # UI tests (Espresso)
│   │           └── data/              # Database tests
│   ├── build.gradle.kts               # App-level Gradle config
│   └── proguard-rules.pro
├── build.gradle.kts                   # Project-level Gradle config
├── settings.gradle.kts
├── gradle.properties
└── README.md                          # Android app setup guide

backend/                               # Existing backend (unchanged)
├── src/HerPace.API/
├── src/HerPace.Core/
└── src/HerPace.Infrastructure/

frontend/                              # Existing web frontend (unchanged)
└── src/
```

**Structure Decision**: Mobile + API architecture (Option 3). The Android app is a new project in the `android/` directory, separate from the existing `backend/` and `frontend/` directories. The app follows **Clean Architecture with MVVM pattern**, organizing code into three layers:

1. **Data Layer**: Handles data sources (Room for local cache, Retrofit for API calls, fitness platform SDKs)
2. **Domain Layer**: Contains business logic, domain models, and repository interfaces (platform-agnostic)
3. **Presentation Layer**: UI components, ViewModels, navigation (MVVM pattern with Android lifecycle awareness)

This structure supports:
- Clear separation of concerns
- Testability (each layer can be tested independently)
- Iterative delivery (features can be built vertically through all layers)
- Offline-first capability (Repository pattern abstracts data source)
- Easy integration of fitness platforms (dedicated integrations/ module)

## Complexity Tracking

**Status**: No constitution violations requiring justification.

The implementation aligns with all three constitutional principles:
- User-centric development through comprehensive user scenarios
- Accessibility-first design with Android accessibility APIs
- Iterative excellence through P1/P2/P3 prioritization

**Architecture Rationale**: Clean Architecture with MVVM is the Android community standard for apps of this complexity (offline support, multiple data sources, fitness integrations). The layered structure enables iterative delivery and testability without violating the simplicity principle.

---

## Phase 1 Re-evaluation: Constitution Check ✅

**Date**: 2026-02-10 (Post-Phase 1 Design Completion)

### I. User-Centric Development ✅

**Status**: PASS (Maintained)

**Design Verification**:
- ✅ **Data Model** (data-model.md) maps directly to user scenarios - all entities support defined user stories
- ✅ **API Contracts** (contracts/api-endpoints.md) provide clear request/response examples matching acceptance criteria
- ✅ **Validation Rules** enforce functional requirements from spec (age 13-120, cycle 21-40 days, etc.)
- ✅ **Error Handling** includes user-friendly error messages and recovery options

**Conclusion**: Architecture preserves user-centric focus. Repository pattern abstracts technical complexity, exposing only user-relevant operations through use cases.

### II. Accessibility-First Design ✅

**Status**: PASS (Enhanced)

**Design Enhancements**:
- ✅ **Jetpack Compose** chosen for automatic TalkBack integration via semantic modifiers
- ✅ **Material Design 3** provides accessible color contrast and touch target sizes (48dp minimum)
- ✅ **Compose Testing** includes accessibility test APIs (`onNodeWithContentDescription()`)
- ✅ **Architecture Support**: ViewModels expose semantic state (e.g., `isLoading`, `errorMessage`) for screen reader announcements

**Implementation Requirements**:
- All Compose components must use `contentDescription` for non-text elements
- Cycle phase indicators require both visual (color) and textual representations
- Form validation errors must be announced to screen readers
- Navigation must support keyboard and assistive devices

**Conclusion**: Technology choices actively support WCAG 2.1 AA compliance. Accessibility is built into framework rather than retrofitted.

### III. Iterative Excellence ✅

**Status**: PASS (Validated)

**Architecture Verification**:
- ✅ **P1 MVP**: Can ship auth + profile + race + plan generation WITHOUT P2/P3 features
  - Core entities (User, RunnerProfile, Race, TrainingPlan) are independent
  - Offline caching optional for P1 (can sync on every request initially)
  - Fitness platform integrations deferred to P2/P3 without blocking

- ✅ **P2 Iteration**: Adds workout logging + notifications WITHOUT refactoring P1
  - `WorkoutLog` entity extends, doesn't modify existing models
  - WorkManager notifications are independent feature module
  - Repository pattern isolates changes to data layer

- ✅ **P3 Iteration**: Adds sync + integrations as enhancement layers
  - Sync metadata (`syncStatus`, `lastModified`) added to existing entities non-invasively
  - Fitness platform integrations use separate `data/integrations/` module
  - No changes required to core business logic

**Testing Strategy Alignment**:
- **Iteration 1 (P1)**: Manual testing + basic unit tests for repositories
- **Iteration 2 (P2)**: Add UI tests for critical paths (Compose Testing)
- **Iteration 3 (P3)**: Comprehensive test coverage + performance benchmarks

**Conclusion**: Clean Architecture explicitly supports vertical feature slicing. Each user story can be implemented from data layer → domain → presentation without touching other stories' code.

---

### Final Constitution Compliance: ✅ PASS

**Summary**: The Android app implementation plan fully complies with all three constitutional principles:

1. **User-Centric**: Data model, contracts, and architecture directly serve user scenarios
2. **Accessibility-First**: Technology stack (Compose, Material Design 3) enables WCAG 2.1 AA compliance
3. **Iterative Excellence**: Clean Architecture + prioritized stories enable MVP shipping and incremental enhancement

**No violations or trade-offs required.** The plan is ready for Phase 2 (task generation via `/speckit.tasks`).
