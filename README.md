# HerPace

**Hormone-aware training plans for women runners**

HerPaceApp is an intelligent training plan application that adapts to your menstrual cycle phases. Using AI-powered insights from Google Gemini, it generates personalized running schedules that work with your body's natural rhythms, not against them.

## Features

- 🏃‍♀️ **Personalized Training Plans** - AI-generated plans tailored to your race goals and current fitness level
- 🔄 **Cycle-Aware Scheduling** - Training intensity automatically adjusts based on menstrual cycle phase
- 📊 **Interactive Dashboard** - Track your progress with visual punch cards and session details
- 🎯 **Race Management** - Set and manage multiple race goals with different distances
- 🔐 **Secure Authentication** - JWT-based auth with ASP.NET Core Identity
- 📱 **Responsive Design** - Beautiful, mobile-friendly UI built with shadcn/ui and Tailwind CSS

## Tech Stack

The backend is built with **C# 12 / .NET 8.0** using **ASP.NET Core Web API**, **Entity Framework Core**, and **PostgreSQL**, with JWT-based authentication. The frontend uses **React 19 + TypeScript** with **Vite**, **shadcn/ui**, and **Tailwind CSS** for the UI. The app integrates **Google Gemini API** for AI-powered training plan generation and deploys to **Google Cloud Run** using Docker containers.

## Prerequisites

- [.NET 8.0 SDK](https://dotnet.microsoft.com/download/dotnet/8.0)
- [Node.js 18+](https://nodejs.org/)
- [PostgreSQL 15+](https://www.postgresql.org/download/)
- [Google Cloud SDK](https://cloud.google.com/sdk/docs/install) (for deployment)
- Google Gemini API key

## Getting Started

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/herpace.git
   cd herpace
   ```

2. **Configure database connection**

   Update `backend/src/HerPace.API/appsettings.json`:
   ```json
   {
     "ConnectionStrings": {
       "HerPaceDb": "Host=localhost;Database=herpace;Username=postgres;Password=yourpassword"
     },
     "Jwt": {
       "Secret": "your-secret-key-min-32-chars",
       "Issuer": "HerPaceApp",
       "Audience": "HerPaceApp"
     },
     "Gemini": {
       "ApiKey": "your-gemini-api-key",
       "Model": "gemini-3.0-flash-preview"
     }
   }
   ```

3. **Run database migrations**
   ```bash
   dotnet ef database update --project backend/src/HerPace.Infrastructure --startup-project backend/src/HerPace.API
   ```

4. **Start the backend**
   ```bash
   dotnet run --project backend/src/HerPace.API
   ```

   API will be available at `https://localhost:7001`

### Frontend Setup

1. **Install dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Configure API endpoint**

   Create `frontend/.env.development`:
   ```env
   VITE_API_BASE_URL=https://localhost:7001
   ```

3. **Start the development server**
   ```bash
   npm run dev
   ```

   Frontend will be available at `http://localhost:5173`

## Running Tests

```bash
# Run all tests
dotnet test HerPace.sln

# Run specific test
dotnet test --filter "FullyQualifiedName~TestName"
```

## Project Structure

```
herpace/
├── backend/
│   ├── src/
│   │   ├── HerPace.API/           # Controllers, middleware, DI configuration
│   │   ├── HerPace.Core/          # Domain entities, DTOs, interfaces
│   │   └── HerPace.Infrastructure/ # DbContext, services, AI integration
│   └── tests/HerPace.Tests/       # xUnit tests
├── frontend/
│   └── src/
│       ├── pages/                 # React page components
│       ├── components/            # Reusable UI components (shadcn/ui)
│       ├── lib/                   # API client and utilities
│       ├── contexts/              # React contexts (auth, etc.)
│       ├── hooks/                 # Custom React hooks
│       └── types/                 # TypeScript type definitions
└── deploy-*.ps1                   # Deployment scripts
```

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Create new account
- `POST /api/auth/login` - Login with credentials

### Profile
- `GET /api/profiles/me` - Get current user profile
- `POST /api/profiles/me` - Create/update profile

### Races
- `GET /api/races` - List user's races
- `POST /api/races` - Create new race
- `GET /api/races/{id}` - Get race details
- `PUT /api/races/{id}` - Update race

### Training Plans
- `POST /api/plans` - Generate new training plan
- `GET /api/plans/active` - Get active training plan

## Deployment

### Quick Update
```powershell
.\deploy-update.ps1
```

### Full Deployment
```powershell
.\deploy-complete.ps1
```

### Production URLs
- **Frontend:** https://herpace-frontend-330702404265.us-central1.run.app
- **API:** https://herpace-api-330702404265.us-central1.run.app

## Key Domain Concepts

### Cycle Phase Calculation
The app uses `CyclePhaseCalculator` to scale the standard 28-day menstrual cycle to variable lengths:
- **Menstrual Phase** - Lower intensity, focus on recovery
- **Follicular Phase** - Building intensity, strength work
- **Ovulatory Phase** - Peak performance potential
- **Luteal Phase** - Maintenance, listening to body signals

### AI Plan Generation
Training plans are generated using:
- **Primary:** Google Gemini API (`GeminiPlanGenerator`)
- **Fallback:** Rule-based generator when AI is unavailable

## Configuration

### Environment Variables (Production)

Managed via Google Cloud Secret Manager:
- `jwt-secret` → `Jwt__Secret`
- `db-connection` → `ConnectionStrings__HerPaceDb`
- `gemini-api-key` → `Gemini__ApiKey`

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is private and proprietary.

## Support

For issues or questions, please open an issue on GitHub.

---

Built with ❤️ for women runners everywhere.
