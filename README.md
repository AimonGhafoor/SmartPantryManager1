# Smart Pantry Manager

An Android application written in Java that helps users reduce food waste by tracking the ingredients they already have at home and suggesting recipes they can cook right now. 

A recipe is **only** suggested if the user has *every single ingredient* required, in at least the required quantity. If an ingredient is missing or the quantity is too low, the recipe is excluded from the suggestions. This strict-matching rule is the core logic of the application.

## Features
* **Pantry Management (Full CRUD):** Add, view, edit, and delete ingredients (Name, Quantity, Unit). Tap an item to edit it, or tap the red trash can to delete it.
* **Suggested Recipes:** Dynamically calculates which of the 16 pre-loaded recipes you can cook based on your current pantry.
* **Recipe Detail:** Click on a suggested recipe to view its detailed preparation steps, including specific quantities, temperatures, and instructions.
* **Settings:** A clean settings page to toggle preferences.
* **Persistence:** All data is saved locally using SQLite, so your pantry remains intact even after you close the app.
* **Modern UI:** Features a vibrant, colorful interface with custom cards, gradient headers, and emoji icons.

## Database Choice
This app uses **SQLite** via `SQLiteOpenHelper`. 
**Why SQLite?** 
1. It is perfect for an app that needs to function completely offline without requiring a network connection.
2. It stores data locally on the device, which is fast and secure.
3. It is the standard database solution taught in the Android module and does not require setting up an external REST API or cloud server.

## Setup and Run Instructions
1. Download or clone this repository to your computer.
2. Open **Android Studio** (Hedgehog or newer is recommended).
3. Select **File > Open** and navigate to the `SmartPantryManager` project folder.
4. Allow Android Studio to complete the Gradle Sync (download any necessary dependencies).
5. Select an Android Emulator (e.g., Medium Phone API 34) or connect a physical Android device.
6. Click the green **Run** button at the top of Android Studio to build and launch the app.

*Note: On the first run, the app will automatically create the SQLite database and pre-load 16 detailed recipes.*

## Project Structure
* `MainActivity.java`: Hosts the bottom navigation and loads the fragments.
* `DatabaseHelper.java`: Handles all SQLite table creation, seeding (16 recipes), and CRUD operations.
* `PantryFragment.java` / `PantryAdapter.java`: UI logic for displaying, adding, editing, and deleting ingredients.
* `SuggestedFragment.java`: Runs the strict-matching query and displays matching recipes.
* `RecipeDetailActivity.java`: Displays the specific preparation steps for a chosen recipe.
* `SettingsFragment.java`: Displays app settings and preferences.

