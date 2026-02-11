# Feature Specification: HerPace Android App

**Feature Branch**: `001-android-app`
**Created**: 2026-02-10
**Status**: Draft
**Input**: User description: "I want to implement HerPace functionality in a brand new Android app using Kotlin.  It should implement the core functionality but needs to be re-designed for mobile/android.   It can reuse the backend.  We will want to implement a notification system (ideally cross platform if doable)."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Account Creation and Onboarding (Priority: P1)

A new user downloads the HerPace Android app, creates an account, and completes their runner profile including menstrual cycle information to receive personalized training recommendations.

**Why this priority**: This is the entry point for all users. Without authentication and profile setup, no other features can be used. This establishes the foundation for personalized training plans.

**Independent Test**: Can be fully tested by creating a new account, entering profile details (age, fitness level, cycle length, last period date), and verifying the profile is saved and retrievable on subsequent app launches. Delivers immediate value by capturing user data needed for personalization.

**Acceptance Scenarios**:

1. **Given** I am a new user who has just installed the app, **When** I open the app for the first time, **Then** I see a welcome screen with options to sign up or log in
2. **Given** I tap "Sign Up", **When** I enter my email, password, and confirm password, **Then** my account is created and I proceed to the onboarding flow
3. **Given** I have created an account, **When** I complete the onboarding form (name, age, fitness level, weekly mileage, cycle length, last period date), **Then** my profile is saved and I'm taken to the dashboard
4. **Given** I am on the onboarding screen, **When** I provide invalid data (e.g., cycle length outside 21-40 days), **Then** I see clear validation messages guiding me to correct the input
5. **Given** I have completed onboarding, **When** I close and reopen the app, **Then** I am automatically logged in and see my dashboard

---

### User Story 2 - Create and Manage Race Goals (Priority: P1)

A user can add upcoming races with details like distance, date, and goal time to generate tailored training plans.

**Why this priority**: Race goals are the core purpose of the app - users need a target to train for. Without this, the training plan generation (P1 feature) cannot function.

**Independent Test**: Can be fully tested by creating a race (e.g., "Spring Marathon - April 15, 2026 - 42.2km - Goal: 4:00:00"), viewing the race in a list, editing race details, and deleting races. Delivers value by allowing users to organize their training around specific goals.

**Acceptance Scenarios**:

1. **Given** I am logged in, **When** I navigate to "My Races" and tap "Add Race", **Then** I see a form to enter race details (name, date, distance, goal time)
2. **Given** I fill out the race form with valid data, **When** I tap "Save", **Then** the race appears in my races list and is available for training plan generation
3. **Given** I have created a race, **When** I view my races list, **Then** I see each race with its key details (name, date, distance) sorted by date
4. **Given** I tap on a race in my list, **When** the race details screen opens, **Then** I can edit the race information or delete the race
5. **Given** I attempt to create a race with a date in the past, **When** I tap "Save", **Then** I see a validation error preventing creation

---

### User Story 3 - Generate Hormone-Aware Training Plan (Priority: P1)

A user selects a race and generates a personalized training plan that adapts workouts based on their menstrual cycle phases.

**Why this priority**: This is the unique value proposition of HerPace - AI-generated plans that account for hormonal fluctuations. This is the core feature that differentiates the app.

**Independent Test**: Can be fully tested by selecting an existing race, tapping "Generate Plan", waiting for AI generation, and viewing the resulting weekly training schedule with sessions marked by cycle phase. Delivers immediate value by providing a complete, personalized training roadmap.

**Acceptance Scenarios**:

1. **Given** I have created a race, **When** I tap "Generate Training Plan" on the race details screen, **Then** the app sends my profile and race data to the backend and shows a loading indicator
2. **Given** the AI is generating my plan, **When** generation completes successfully, **Then** I see a confirmation message and can view my training plan
3. **Given** I view my training plan, **When** I scroll through the weeks, **Then** I see each week's sessions with details (workout type, distance, intensity) and visual indicators of my predicted cycle phase
4. **Given** the plan generation fails (network error, AI service unavailable), **When** the error occurs, **Then** I see a user-friendly error message with an option to retry
5. **Given** I have an active training plan for a race, **When** I tap "Generate Training Plan" again, **Then** I see a warning that this will replace the existing plan and must confirm before proceeding

---

### User Story 4 - View Daily Training Sessions (Priority: P2)

A user can view their training schedule for each day, including workout details, intensity level, and how the workout aligns with their current cycle phase.

**Why this priority**: Once a plan is generated, users need to see what to do each day. This is essential for plan execution but builds on P1 stories.

**Independent Test**: Can be fully tested by navigating to the calendar/dashboard view, selecting different dates, and viewing session details (workout type, distance, pace, notes). Delivers value by making the daily training actionable and understandable.

**Acceptance Scenarios**:

1. **Given** I have an active training plan, **When** I open the dashboard, **Then** I see today's training session prominently displayed with key details
2. **Given** I am viewing a training session, **When** I read the details, **Then** I see workout type (e.g., "Easy Run", "Tempo Run"), distance, target pace/intensity, and any AI-generated notes
3. **Given** I want to see future or past sessions, **When** I navigate to the calendar view, **Then** I can select any date and view that day's session
4. **Given** today is during my menstrual phase, **When** I view today's workout, **Then** I see a visual indicator (icon/color) showing the current cycle phase and any phase-specific guidance
5. **Given** it's a rest day, **When** I view the session, **Then** I see "Rest Day" with recovery guidance

---

### User Story 5 - Complete and Track Workouts (Priority: P2)

A user can mark workouts as completed, optionally logging actual distance, time, and perceived effort for progress tracking.

**Why this priority**: Tracking completion provides accountability and data for future plan adjustments. Important for engagement but not essential for initial plan generation and viewing.

**Independent Test**: Can be fully tested by marking a session as complete, optionally entering actual performance data, and verifying the session shows as completed with logged data. Delivers value through progress tracking and motivation.

**Acceptance Scenarios**:

1. **Given** I am viewing today's workout, **When** I tap "Mark as Complete", **Then** the workout is marked as done and shows a completion checkmark
2. **Given** I mark a workout complete, **When** prompted to log details, **Then** I can optionally enter actual distance, time, and rate my perceived effort (1-10 scale)
3. **Given** I have completed multiple workouts, **When** I view my training plan, **Then** completed sessions are visually distinguished from upcoming sessions
4. **Given** I completed a workout yesterday but forgot to log it, **When** I navigate to yesterday's session, **Then** I can still mark it complete and add details
5. **Given** I accidentally marked a workout complete, **When** I tap on the completed session, **Then** I can undo the completion

---

### User Story 6 - Receive Training Reminders (Priority: P2)

A user receives push notifications reminding them of upcoming workouts and motivational messages aligned with their cycle phase.

**Why this priority**: Notifications improve adherence and engagement but are not essential for core plan generation and viewing functionality.

**Independent Test**: Can be fully tested by enabling notifications, scheduling them for specific times, and verifying the device receives notifications with correct workout details. Delivers value through improved training consistency.

**Acceptance Scenarios**:

1. **Given** I have enabled notifications in app settings, **When** it's 6 PM the evening before a workout, **Then** I receive a push notification previewing tomorrow's session
2. **Given** I receive a workout reminder notification, **When** I tap on it, **Then** the app opens directly to that day's training session details
3. **Given** it's the morning of a scheduled workout, **When** the notification time arrives (e.g., 7 AM), **Then** I receive a notification with workout details and motivational message
4. **Given** I want to customize notification timing, **When** I go to notification settings, **Then** I can choose preferred times for reminders or disable them entirely
5. **Given** it's a rest day, **When** the notification time arrives, **Then** I receive a notification encouraging recovery and rest

---

### User Story 7 - Manage Cycle Data (Priority: P3)

A user can update their cycle information (last period start date, cycle length) to keep predictions accurate and training adjustments relevant.

**Why this priority**: Maintaining accurate cycle data improves plan personalization over time, but users can function with initial onboarding data for their first plan.

**Independent Test**: Can be fully tested by navigating to profile settings, updating cycle information, and verifying that future training session recommendations reflect the updated cycle predictions. Delivers value through improved long-term accuracy.

**Acceptance Scenarios**:

1. **Given** I am in my profile settings, **When** I navigate to "Cycle Tracking", **Then** I see my current cycle length and last period start date
2. **Given** my period has started, **When** I tap "Log Period Start" and select today's date, **Then** the app updates my cycle predictions for future training sessions
3. **Given** my cycle length has changed, **When** I update the cycle length field (21-40 days), **Then** future cycle phase predictions are recalculated
4. **Given** I update my cycle data, **When** I return to my active training plan, **Then** I see updated cycle phase indicators on future sessions
5. **Given** I haven't logged my period in over 60 days, **When** I open the app, **Then** I see a gentle reminder to update my cycle information

---

### User Story 8 - Sync Data Across Sessions (Priority: P3)

A user's data (profile, races, training plans, completed workouts) is automatically synced with the backend, allowing them to access their information if they switch devices or reinstall the app.

**Why this priority**: Cloud sync is important for data persistence and multi-device scenarios, but most users will use a single device. This is a quality-of-life feature rather than core functionality.

**Independent Test**: Can be fully tested by creating data on one installation, logging out or clearing app data, logging back in, and verifying all data is restored from the server. Delivers value through data safety and device flexibility.

**Acceptance Scenarios**:

1. **Given** I am logged in with internet connectivity, **When** I make any changes (create race, complete workout, update profile), **Then** the data is automatically synced to the backend
2. **Given** I am offline, **When** I make changes, **Then** the changes are queued and automatically synced when connectivity is restored
3. **Given** I uninstall and reinstall the app, **When** I log back in, **Then** all my data (profile, races, training plans, workout history) is restored
4. **Given** sync is in progress, **When** I navigate through the app, **Then** I see a subtle sync indicator but can still use all features
5. **Given** a sync conflict occurs (rare edge case), **When** detected, **Then** the server data takes precedence and the app notifies me of any local changes that were overwritten

---

### Edge Cases

- What happens when a user creates a race that's too soon to generate a reasonable training plan (e.g., only 2 weeks away for a marathon)?
- How does the app handle extremely long cycle lengths (e.g., irregular cycles of 60+ days or medical conditions like PCOS)?
- What happens if the user's device time zone changes during active training plan?
- How does the system handle plan generation failures or timeouts (backend AI service unavailable)?
- What happens when a user has multiple active races with overlapping training schedules?
- How does the app behave when notifications are disabled at the OS level but enabled in-app?
- What happens if the user's authentication token expires while offline?
- How does the app handle race date changes after a plan has been generated?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: App MUST support user registration with email and password
- **FR-002**: App MUST support user login with email and password, persisting session across app restarts
- **FR-003**: App MUST validate email format, password strength (minimum 8 characters), and cycle data (cycle length 21-40 days) during registration and profile updates
- **FR-004**: App MUST collect and store runner profile information including: name, age, fitness level, current weekly mileage, menstrual cycle length, and last period start date
- **FR-005**: App MUST allow users to create, view, edit, and delete race entries with fields: race name, date, distance, and goal time
- **FR-006**: App MUST display races in chronological order with upcoming races shown first
- **FR-007**: App MUST send user profile and race data to backend API to request AI-generated training plans
- **FR-008**: App MUST display training plans as a weekly calendar/schedule showing workout details for each day
- **FR-009**: App MUST visually indicate the predicted menstrual cycle phase (Menstrual, Follicular, Ovulatory, Luteal) for each training session
- **FR-010**: App MUST allow users to mark training sessions as complete with optional actual performance data (distance, time, perceived effort)
- **FR-011**: App MUST show a visual distinction between completed and upcoming training sessions
- **FR-012**: App MUST send push notifications for workout reminders at user-configurable times (default: evening before + morning of workout)
- **FR-013**: App MUST allow users to enable/disable notifications and customize notification timing in settings
- **FR-014**: Tapping a notification MUST open the app to the relevant training session details
- **FR-015**: App MUST allow users to update their cycle information (last period start date, cycle length) in profile settings
- **FR-016**: App MUST automatically sync all user data (profile, races, training plans, workout completions) with the backend when online
- **FR-017**: App MUST queue data changes made while offline and sync them when connectivity is restored
- **FR-018**: App MUST authenticate all API requests using JWT tokens obtained during login
- **FR-019**: App MUST handle API errors gracefully with user-friendly error messages and retry options
- **FR-020**: App MUST display a loading indicator during plan generation, which can take 10-30 seconds
- **FR-021**: App MUST warn users before regenerating a training plan that would replace an existing plan
- **FR-022**: App MUST provide a logout option that clears local session data
- **FR-023**: App MUST maintain login session until user explicitly logs out or token expires
- **FR-024**: App MUST support Android API level 26 (Android 8.0 Oreo) and above
- **FR-025**: App MUST use the existing HerPace backend API at the configured production or development endpoint
- **FR-026**: App MUST support offline viewing of cached training plans and workout details (previously loaded while online), allowing users to access their schedule without internet connectivity
- **FR-027**: App MUST integrate with multiple fitness tracking platforms (Strava, Garmin Connect, Google Fit) to allow users to automatically import completed workout data (distance, time, route)
- **FR-028**: App MUST cache training plan data locally when user is online, making it available for viewing when offline
- **FR-029**: App MUST clearly indicate when displaying cached/stale data in offline mode with visual indicators showing last sync time
- **FR-030**: For fitness platform integrations, app MUST provide OAuth authentication flows for Strava and Garmin Connect, and use Android Health Connect API for Google Fit
- **FR-031**: Users MUST be able to choose which fitness platforms to connect in app settings, and disconnect platforms at any time
- **FR-032**: When a workout is imported from a connected platform, app MUST match it to the corresponding training session by date and automatically mark it as complete with imported data

### Key Entities

- **User Account**: Represents authentication credentials (email, password hash) and session tokens
- **Runner Profile**: Contains personal training information (name, age, fitness level, weekly mileage, cycle length, last period date, notification preferences)
- **Race**: Represents a target race event with name, date, distance (5K, 10K, Half Marathon, Marathon), and goal finish time
- **Training Plan**: A generated schedule of training sessions spanning multiple weeks, linked to a specific race
- **Training Session**: Individual workout within a plan, including date, workout type (Easy Run, Long Run, Tempo Run, Intervals, Rest), distance, intensity/pace guidance, cycle phase indicator, and completion status
- **Workout Log**: User-entered data for completed sessions including actual distance, time, and perceived effort rating
- **Notification Schedule**: Configuration for reminder timing and enabled/disabled status

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can complete account creation and onboarding in under 5 minutes
- **SC-002**: Users can create a race and generate a training plan in under 3 minutes (excluding AI generation time)
- **SC-003**: Training plan generation completes successfully in under 30 seconds for 95% of requests
- **SC-004**: Users can view today's workout details within 2 seconds of opening the app (when authenticated)
- **SC-005**: App successfully syncs user data with backend with 99% reliability when online
- **SC-006**: Push notifications are delivered within 2 minutes of scheduled time for 95% of enabled users
- **SC-007**: App maintains session persistence across app restarts for 90% of users without requiring re-login
- **SC-008**: Users can mark a workout as complete and log details in under 30 seconds
- **SC-009**: 80% of users who generate a training plan view at least 5 different training sessions
- **SC-010**: App handles network errors and backend unavailability with clear error messaging and recovery options, preventing user frustration in 95% of error scenarios

## Assumptions

1. **Backend API Compatibility**: The existing HerPace backend API endpoints (`/api/auth/*`, `/api/profiles/*`, `/api/races/*`, `/api/plans/*`) are fully functional and will remain stable during Android app development
2. **Push Notification Service**: We will use Firebase Cloud Messaging (FCM) as the cross-platform notification service, which requires backend integration to send notifications from the server
3. **Authentication Flow**: JWT token-based authentication matching the existing web app implementation will be sufficient for mobile
4. **Data Model Consistency**: The Android app will use the same data models (DTOs) as the existing web frontend to ensure compatibility
5. **Internet Connectivity**: Users need internet connectivity for core features like plan generation, but cached data (training plans, workout details) will be viewable offline
6. **Single Device Usage**: Primary use case is a single user with one active device, though data sync supports multi-device scenarios
7. **Notification Permissions**: Users will grant notification permissions when prompted by Android OS; app functionality does not strictly depend on notifications being enabled
8. **Cycle Tracking**: Users are responsible for manually updating cycle information; no automatic tracking or integration with period tracking apps is included
9. **Fitness Platform APIs**: Strava API, Garmin Connect API, and Android Health Connect are available and stable for workout data import
10. **Performance Targets**: Android devices from 2019 or newer (typically API 26+) will provide adequate performance for all app features
11. **OAuth Flows**: Users will complete OAuth authentication flows for Strava and Garmin within the app without significant friction

## Dependencies

1. **Existing Backend API**: The Android app depends entirely on the current HerPace backend API being available and functional
2. **Firebase Project**: Requires a Firebase project set up for push notifications (FCM) and potentially analytics
3. **Backend Notification Integration**: Backend must be updated to send push notifications via FCM when workout reminders are due
4. **Google Play Store Account**: Required for app distribution
5. **API Documentation**: Complete and accurate API documentation for all endpoints the mobile app will consume
6. **Strava API Access**: Requires Strava developer account and API credentials for OAuth and workout data import
7. **Garmin Connect API Access**: Requires Garmin developer account and API credentials for OAuth and activity data import
8. **Android Health Connect**: Requires app configuration to access Google Fit data through Health Connect API (Android 14+) or Google Fit API (older versions)
9. **Local Database**: Requires Room or similar local database solution for caching training plans and workout data for offline viewing

## Constraints

1. **Platform**: Android only (no iOS in this phase, though notification service should be cross-platform compatible via FCM)
2. **Language**: Kotlin only (as specified in requirements)
3. **Backend Reuse**: Cannot modify core backend business logic; Android app must adapt to existing API contracts
4. **Design Language**: Must follow Android Material Design guidelines for native look and feel
5. **API Level**: Minimum SDK version API 26 (Android 8.0) to balance feature support and device coverage
6. **Limited Offline Mode**: App supports viewing cached data offline but requires internet for plan generation, data modifications, and sync
7. **Read-Only Offline**: Users cannot create or modify data (races, profile, mark workouts complete) while offline; changes require internet connectivity

## Out of Scope

1. **iOS Application**: Separate project; this spec covers Android only
2. **GPS Workout Tracking**: No real-time GPS tracking during runs; workout completion relies on manual entry or fitness platform import
3. **Social Features**: No sharing workouts, following other users, or community features
4. **Wearable Integration**: No Android Wear OS app or smartwatch complications
5. **Payment/Subscription**: No in-app purchases or premium features in this phase
6. **Advanced Analytics**: No detailed performance analytics, charts, or training insights beyond basic completion tracking
7. **Custom Plan Editing**: Users cannot manually edit AI-generated training sessions; must regenerate plan
8. **Multi-Language Support**: English only for MVP
9. **Backend Modifications**: No changes to backend logic, database schema, or AI plan generation algorithms (except adding FCM notification support)
10. **Automatic Workout Detection**: No automatic detection of workouts from phone sensors; relies on fitness platform imports or manual logging

## Non-Functional Requirements

### Performance
- App launch time under 2 seconds on mid-range devices (2021+)
- Smooth scrolling at 60 FPS minimum when navigating training calendar
- API response handling with timeout limits (30 seconds for plan generation, 10 seconds for other requests)

### Security
- Secure storage of JWT tokens using Android Keystore
- No plaintext storage of passwords
- HTTPS only for all API communication
- Certificate pinning recommended for production

### Usability
- Intuitive navigation following Android conventions (bottom navigation or navigation drawer)
- Accessibility support for TalkBack screen reader
- Clear visual hierarchy with Material Design components
- Responsive layouts for different screen sizes (phones and tablets)

### Reliability
- Graceful degradation when backend is unavailable (show cached data with staleness indicator)
- No data loss during network interruptions (queue and retry failed requests)
- Crash-free rate target of 99.5%

### Maintainability
- Modular architecture (MVVM or MVI pattern recommended)
- Dependency injection for testability
- Comprehensive unit and integration tests for business logic
- Clear separation between UI, business logic, and data layers
