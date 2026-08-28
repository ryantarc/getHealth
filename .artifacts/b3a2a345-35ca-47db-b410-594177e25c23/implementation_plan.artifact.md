# Implement Supabase Integration

This plan integrates Supabase into the `getHealth` app, enabling real authentication and database capabilities.

## User Review Required

> [!IMPORTANT]
> I will be updating `local.properties` to ensure the Supabase keys are correctly named and formatted (removing extra commas). Please ensure your actual Supabase project matches the keys provided.

## Proposed Changes

### Configuration

#### [MODIFY] [local.properties](file:///C:/Users/user/AndroidStudioProjects/getHealth/local.properties)
- Rename `supabaseUrl` to `SUPABASE_URL`.
- Rename `supabaseKey` to `SUPABASE_ANON_KEY`.
- Remove trailing commas and ensure values are correctly quoted for `build.gradle.kts` consumption.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/user/AndroidStudioProjects/getHealth/app/build.gradle.kts)
- Add `io.github.jan-tennert.supabase:auth-kt` dependency.
- Add `kotlinx-serialization-json` implementation.

### Data Layer

#### [NEW] [SupabaseClient.kt](file:///C:/Users/user/AndroidStudioProjects/getHealth/app/src/main/java/com/example/gethealth/data/SupabaseClient.kt)
- Create a singleton `SupabaseClient` using `BuildConfig.SUPABASE_URL` and `BuildConfig.SUPABASE_ANON_KEY`.

#### [MODIFY] [UserRepository.kt](file:///C:/Users/user/AndroidStudioProjects/getHealth/app/src/main/java/com/example/gethealth/data/data/UserRepository.kt)
- Update `login` and `register` methods to be `suspend` and use Supabase Auth.
- Note: The `UserRepository.kt` path in the file list was `app/src/main/java/com/example/gethealth/data/data/UserRepository.kt`. I'll verify the exact path. (Checking `find_files` output: `C:/Users/user/AndroidStudioProjects/getHealth/app/src/main/java/com/example/gethealth/data/data/UserRepository.kt`)

### Models

#### [MODIFY] [User.kt](file:///C:/Users/user/AndroidStudioProjects/getHealth/app/src/main/java/com/example/gethealth/model/User.kt)
- Add `@Serializable` annotation.

#### [MODIFY] [Recipe.kt](file:///C:/Users/user/AndroidStudioProjects/getHealth/app/src/main/java/com/example/gethealth/model/Recipe.kt)
- Add `@Serializable` annotation.

## Verification Plan

### Automated Tests
- Gradle sync to verify new dependencies.
- Build the project to verify `BuildConfig` fields are generated correctly.

### Manual Verification
- The user will need to test Login/Register screens once the UI is updated to handle `suspend` calls (UI changes are not in the initial scope but I'll ensure the repository is ready).
