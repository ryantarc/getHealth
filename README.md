# getHealth

A health and wellbeing mobile application developed using **Kotlin** and **Jetpack Compose**.

## Project Modules

The application consists of 3 main modules:

- **Meal Planner** – AI-assisted meal planning and nutrition recommendations
- **Mood Journal** – Create, view, edit and delete mood journal entries
- **Fitness Advisor** – Fitness goals and exercise recommendations

Other parts of the application include:

- **Dashboard** – Main page connecting the modules
- **Navigation** – Handles movement between screens
- **UI Components** – Shared components used throughout the application

---

# GitHub Guide

We use GitHub to work on the project together.

> **Important: Do not directly push to `main`.**

Each member should work on their own branch and create a Pull Request when their work is ready.

## Branch Structure

```text
main
├── feature/meal-planner
├── feature/mood-journal
├── feature/fitness-advisor
└── feature/dashboard
```

---

# 1. Before Starting Work

First, get the latest version of the project:

```bash
git checkout main
git pull origin main
```

Create your own feature branch:

```bash
git checkout -b feature/your-feature
```

Example:

```bash
git checkout -b feature/meal-planner
```

If you already created the branch before, switch to it instead:

```bash
git checkout feature/meal-planner
```

---

# 2. Work on Your Feature

Work normally in Android Studio.

Try to work mainly inside your assigned module/package:

```text
mealplanner/
mood/
fitness/
dashboard/
```

Avoid changing the same files as another team member unless necessary.

---

# 3. Save Your Changes

After completing a small part of your work, check your changes:

```bash
git status
```

Add your changes:

```bash
git add .
```

Commit your changes:

```bash
git commit -m "Add meal planner screen"
```

Use clear commit messages.

### Good Examples

```text
Add meal planner screen
Add mood journal CRUD
Add fitness recommendation UI
Fix navigation bug
Add meal preference input
```

### Avoid

```text
update
stuff
changes
test
final
```

---

# 4. Push Your Branch

Upload your branch to GitHub:

```bash
git push -u origin feature/your-feature
```

Example:

```bash
git push -u origin feature/meal-planner
```

After the first push, you can normally use:

```bash
git push
```

---

# 5. Create a Pull Request

After pushing your branch:

1. Go to the GitHub repository.
2. Open your recently pushed branch.
3. Click **Compare & pull request**.
4. Make sure the destination is `main`.
5. Create the Pull Request.
6. Ask another team member to review your changes.
7. If everything is okay, merge the Pull Request into `main`.

```text
Your branch
     ↓
  Push
     ↓
Pull Request
     ↓
Team Review
     ↓
  Merge
     ↓
   main
```

---

# 6. After Someone Merges Their Work

Before starting new work, update your local `main`:

```bash
git checkout main
git pull origin main
```

Then switch back to your feature branch:

```bash
git checkout feature/your-feature
```

If your branch needs the latest changes from `main`:

```bash
git merge main
```

If Git reports a merge conflict, ask the team for help before making changes.

---

# Important Rules

## Do Not Push Directly to `main`

Do **not** do:

```bash
git checkout main
git add .
git commit -m "My changes"
git push
```

Instead, always use your own feature branch.

## Pull Before Starting Work

Always get the latest version before starting:

```bash
git checkout main
git pull origin main
```

## Test Before Creating a Pull Request

Make sure the application:

- Builds successfully
- Runs on the emulator
- Does not break another module
- Does not contain obvious errors

## Make Small Commits

Do not wait until the entire module is finished before committing.

For example:

```text
Add meal planner screen
Add diet goal input
Add meal preference selection
Fix meal planner layout
```

## Be Careful With Shared Files

Coordinate with the team before changing files that multiple people may use:

```text
MainActivity.kt
AppNavigation.kt
build.gradle.kts
libs.versions.toml
strings.xml
Theme.kt
```

## Keep `main` Working

The `main` branch should always contain a version of the application that the team can **build and run**.

---

# Branch Naming

Use:

```text
feature/feature-name
```

Examples:

```text
feature/meal-planner
feature/mood-journal
feature/fitness-advisor
feature/dashboard
feature/navigation
```

For bug fixes:

```text
fix/navigation-bug
fix/mood-journal-crash
```

---

# Common Git Commands

| Command | Purpose |
|---|---|
| `git checkout main` | Switch to the main branch |
| `git pull origin main` | Download the latest main branch |
| `git checkout -b feature/name` | Create a new feature branch |
| `git checkout feature/name` | Switch to an existing branch |
| `git status` | Check your changes |
| `git add .` | Prepare changes for commit |
| `git commit -m "message"` | Save changes locally |
| `git push` | Upload your changes |
| `git merge main` | Bring main's changes into your branch |

---

# Simple Workflow

```text
Get latest main
      ↓
Create/switch to your branch
      ↓
Write code
      ↓
Test the application
      ↓
git add .
      ↓
git commit
      ↓
git push
      ↓
Create Pull Request
      ↓
Team member reviews
      ↓
Merge into main
```

> **Golden Rule: Work on your own branch → Commit → Push → Pull Request → Review → Merge into `main`.**
