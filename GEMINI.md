# HerPace - Gemini CLI Context

This file provides essential context for Gemini CLI when working on the HerPace project. HerPace is a hormone-aware training plan application for women runners, leveraging AI to adapt training intensity to menstrual cycle phases.

## Project Overview

- **Purpose**: Personalized, cycle-aware running training plans.
- **Architecture**: Decoupled Full-stack (ASP.NET Core Web API + React Frontend).
- **Core Feature**: AI-powered plan generation that adjusts workout intensity (Easy, Long, Tempo, Interval, Rest) based on predicted menstrual cycle phases (Menstrual, Follicular, Ovulatory, Luteal).

## Tech Stack

### Backend
- **Framework**: .NET 8.0 / C# 12
- **API**: ASP.NET Core Web API
- **Database**: PostgreSQL with Entity Framework Core
- **Auth**: ASP.NET Core Identity + JWT
- **Background Jobs**: Hangfire
- **AI Integration**: Google Gemini API (Vertex AI/Gemini Flash)

### Frontend
- **Framework**: React 19 + TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS + shadcn/ui
- **State Management**: React Context (Auth, Toast)
- **Routing**: React Router 6
- **Testing/Development**: Vitest, Storybook

## Getting Started

### Backend Development
1. **Directory**: `backend/`
2. **Configuration**: 
   - Update `backend/src/HerPace.API/appsettings.json` with your `HerPaceDb` connection string and `Gemini:ApiKey`.
   - Use `UseCloudSql: true` for production/proxy connections.
3. **Database**: 
   ```powershell
   dotnet ef database update --project src/HerPace.Infrastructure --startup-project src/HerPace.API
   ```
4. **Run**:
   ```powershell
   dotnet run --project src/HerPace.API
   ```
   - API: `https://localhost:7001`
   - Hangfire Dashboard: `/hangfire` (Dev only)

### Frontend Development
1. **Directory**: `frontend/`
2. **Setup**:
   ```bash
   npm install
   ```
3. **Configuration**: Create `.env.development` with `VITE_API_BASE_URL=https://localhost:7001`.
4. **Run**:
   ```bash
   npm run dev
   ```
   - URL: `http://localhost:5163`
5. **Storybook**:
   ```bash
   npm run storybook
   ```

## Key Domain Logic

### Training Stages (`TrainingStageLibrary.cs`)
The app calculates stages based on plan timeline:
- **Base**: Foundation building (first ~35% of plans >10 weeks).
- **Build**: Intensity increase (between Base and Peak).
- **Peak**: Highest volume/intensity (2 weeks before Taper).
- **Taper**: Volume reduction (final 2 weeks).

### Cycle Phase Calculation
Intensity scaling is mapped to phases:
- **Menstrual Phase**: Low intensity, recovery focus.
- **Follicular Phase**: High intensity, strength building.
- **Ovulatory Phase**: Peak performance.
- **Luteal Phase**: Maintenance, listening to body signals.

## Development Conventions

### Naming Conventions
- **C#**: PascalCase for types/methods, camelCase for local variables. Interfaces use `I*` prefix (e.g., `IPlanGenerationService`).
- **TypeScript/React**: PascalCase for components (e.g., `Signup.tsx`), camelCase for hooks and utility functions (e.g., `useOnboardingCheck`).

### Backend Architecture
- **Clean Architecture**: 
  - `HerPace.Core`: Entities, Enums, Interfaces, DTOs.
  - `HerPace.Infrastructure`: DB, AI Services, Third-party integrations.
  - `HerPace.API`: Controllers, Middleware, Configuration.
- **Migrations**: Always run migrations specifying the infrastructure and startup projects as shown in setup.
- **Dependency Injection**: Services must be registered in `Program.cs`.

### Frontend Patterns
- **Components**: Follow shadcn/ui patterns. Reusable components in `@/components/ui`.
- **Pages**: Located in `src/pages/`.
- **Hooks**: Logic separation using custom hooks.
- **Styling**: Strict adherence to Tailwind utility classes and `DESIGN_TOKENS.md`.

## Commit & Pull Request Guidelines
- **Commit Messages**: Short, imperative summaries (e.g., "Fix date issues", "Add onboarding flow").
- **PR Content**: Include description, linked specs, and verification steps.

## Security & Configuration
- **Secrets**: NEVER commit secrets or API keys. 
- **Backend**: Use `appsettings.json` for local dev and Environment Variables/Secret Manager for production.
- **Frontend**: Use `VITE_` prefixed environment variables.

## Testing
- **Backend**: `dotnet test` (xUnit).
- **Frontend**: `npm run test` (Vitest).

## Deployment
- Deployed to **Google Cloud Run**.
- Use `deploy-update.ps1` for rapid updates.
- Infrastructure managed via Dockerfiles in `backend/` and `frontend/`.
