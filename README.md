# Smart Pantry Manager
## Overview
Smart Pantry Manager is a Java-based Android application that helps users manage pantry ingredients and reduce food waste by suggesting recipes based on the ingredients they already have.

The application uses strict recipe matching to ensure that recipes in the **Ready to Make** section only appear when the required ingredients are available in sufficient quantities. Recipes with missing ingredients are displayed separately in the **Almost There** section.

## Features
- Add, view, edit and delete pantry ingredients
- Record ingredient quantities, measurement units and optional expiry dates
- Store pantry data locally using SQLite
- Browse 20 preloaded recipes
- View strict recipe suggestions and almost-ready recipes
- View recipe ingredients and preparation instructions
- Save a profile name using SharedPreferences
- Validate ingredient input

## Technologies Used
- **Language:** Java
- **IDE:** Android Studio
- **Database:** SQLite
- **UI:** XML layouts and RecyclerView
- **Navigation:** Activities and Intents
- **Local preferences:** SharedPreferences
- **Version control:** Git and GitHub

## Database
SQLite was chosen because it is built into Android, supports structured data and stores information locally without requiring a separate database server.

The database contains two main tables:

- **Pantry:** Stores ingredient names, quantities, units and optional expiry dates.
- **Recipes:** Stores recipe names, required ingredients and preparation methods.

SharedPreferences is used to store the user's profile name and, if enabled, the dark-mode preference.

## How to Run
1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync and download dependencies.
4. Select an Android emulator or connected Android device.
5. Run the `app` configuration.

The project uses Java 11 compatibility and requires Android API 26 or later.

## GitHub Repository
https://github.com/YashmikaR/SmartPantryManager_Assignment