# GitHub Workflow When Making Changes

Follow these steps **every time you want to make changes to the project**:

1. **Create your own branch from `main`**

   * A branch is basically a separate copy/version of the `main` code.
   * This allows you to make changes without directly breaking `main`.
   * Example: `feature-login` or `ryan-ui`

2. **Pull the latest code from `main`**

   * Before you start coding, make sure your branch has the latest changes from `main`.
   * This helps prevent conflicts with code your teammates have already added.
   * **Do this before starting your work.**

3. **Make your code changes**

   * Open the project in your IDE.
   * Code normally and test your changes.

4. **Commit your changes**

   * A commit is basically a **save point** for your changes on your local branch.
   * Example: `Added login validation`

5. **Push your branch**

   * Push uploads your branch and its commits to GitHub.
   * Your teammates can now see your changes.

6. **Create a Pull Request (PR) on GitHub**

   * A Pull Request is a request to **merge your branch into `main`**.
   * Your teammates can review your code before merging it.
   * Once approved, your changes can be merged into `main`.

## Simple Flow

`main` → **create branch** → **pull latest changes** → **code** → **commit** → **push** → **Pull Request** → **merge into main**

### ⚠️ Important

**Do NOT directly code on `main`.**

Always work on your own branch first. This keeps `main` safe if your code has problems.
