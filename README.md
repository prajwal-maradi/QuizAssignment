# MCQ Quiz App

A modern Android quiz application built with **Jetpack Compose** and **Kotlin**, featuring animated UI, streak tracking, and a clean MVVM architecture.

## Features

- **Multiple Choice Questions** — 10 general knowledge questions loaded from a local JSON file
- **Randomized Order** — Questions are shuffled on every attempt
- **Instant Feedback** — Correct/wrong answers highlighted with color-coded animations
- **Streak Tracking** — Tracks consecutive correct answers with a fire badge
- **Streak Celebration** — Animated overlay at every 3-streak milestone (3, 6, 9...)
- **Skip Support** — Skip questions you're unsure about
- **Animated Progress Bar** — Spring-animated progress indicator
- **Result Screen** — Score circle, performance rating, and detailed summary
- **Dynamic Theming** — Supports Material You dynamic colors (Android 12+)
- **Edge-to-Edge** — Full-screen immersive UI with safe drawing padding

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **UI** | Jetpack Compose, Material 3 |
| **Architecture** | MVVM (ViewModel + StateFlow) |
| **DI** | Koin |
| **JSON Parsing** | Gson |
| **Async** | Kotlin Coroutines |
| **Build** | Gradle Kotlin DSL, Version Catalog |

## Project Structure

```
app/src/main/java/com/example/quizapp/
├── MainActivity.kt              # Entry point, hosts QuizApp composable
├── QuizApplication.kt           # Application class, initializes Koin
├── data/
│   ├── FetchAndParseQuestions.kt # Loads & caches questions from assets JSON
│   ├── model/
│   │   └── Question.kt          # Immutable data class for quiz questions
│   └── repository/
│       ├── QuizRepository.kt    # Repository interface
│       └── QuizRepositoryImpl.kt# Repository implementation
├── di/
│   └── AppModule.kt             # Koin dependency injection module
├── ui/
│   ├── components/
│   │   ├── OptionCard.kt        # Reusable option button with state colors
│   │   └── StreakBadge.kt       # Streak badge & celebration overlay
│   ├── screens/
│   │   ├── QuizScreen.kt       # Main quiz gameplay screen
│   │   ├── ResultScreen.kt     # Post-quiz results & stats screen
│   │   └── SplashScreen.kt     # Loading/splash screen
│   └── theme/
│       ├── Color.kt            # Color definitions
│       ├── Theme.kt            # Material 3 theme setup
│       └── Type.kt             # Typography definitions
└── viewmodel/
    ├── QuizUiState.kt           # Sealed interface for UI states
    └── QuizViewModel.kt         # Business logic & state management
```

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────────┐
│  Composable  │◄────│  ViewModel   │◄────│    Repository        │
│   Screens    │     │  (StateFlow) │     │  (QuizRepositoryImpl)│
└─────────────┘     └──────────────┘     └──────────┬────────────┘
                                                     │
                                          ┌──────────▼────────────┐
                                          │ FetchAndParseQuestions │
                                          │  (assets/questions.json)│
                                          └─────────────────────────┘
```

- **UI Layer** — Compose screens observe `StateFlow<QuizUiState>` via `collectAsStateWithLifecycle()`
- **ViewModel** — Manages quiz state, scoring, streaks, and navigation between questions
- **Data Layer** — Reads from bundled `questions.json`, parses with Gson, and caches in memory

## Getting Started

### Prerequisites

- **Android Studio** Ladybug or newer
- **JDK 11+**
- **Android SDK 36** (compileSdk)

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/prajwal-maradi/QuizAssignment.git
   ```

2. Open in Android Studio

3. Sync Gradle and run on an emulator or device (API 34+)

### Adding Questions

Edit `app/src/main/assets/questions.json`:

```json
{
  "id": 11,
  "question": "Your question here?",
  "options": ["Option A", "Option B", "Option C", "Option D"],
  "correctAnswerIndex": 0
}
```

## Configuration

| Property | Value | File |
|----------|-------|------|
| `minSdk` | 34 | `app/build.gradle.kts` |
| `targetSdk` | 36 | `app/build.gradle.kts` |
| `compileSdk` | 36 | `app/build.gradle.kts` |
| Compose BOM | 2024.09.00 | `libs.versions.toml` |
| Kotlin | 2.0.21 | `libs.versions.toml` |

## License

This project is for educational purposes.
