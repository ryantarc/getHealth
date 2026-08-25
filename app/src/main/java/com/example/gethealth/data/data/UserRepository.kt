package com.example.gethealth.data

import com.example.gethealth.model.User

/**
 * Placeholder "repository" for user-related data.
 *
 * What it is: A repository is just a class whose job is to fetch/save data,
 *             hiding *where* that data actually comes from (a database, an
 *             API, memory, etc.) from the rest of the app.
 *
 * Why we need it now: We don't have a database yet, so this repository just
 *             pretends to check credentials. When we later add a real
 *             database or API (Room / Retrofit / Firebase), we only need to
 *             change the code INSIDE these functions — every screen that
 *             calls this repository keeps working without changes.
 *
 * Where it's used: LoginScreen and RegisterScreen call these functions
 *             instead of doing validation directly, so the "fake" logic is
 *             kept in one place.
 */
object UserRepository {

    /**
     * Pretends to log a user in.
     * For now, any non-empty email + password is treated as valid.
     */
    fun login(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    /**
     * Pretends to register a new user.
     * For now, this always "succeeds" as long as the caller already
     * validated the fields (see RegisterScreen).
     */
    fun register(name: String, email: String, password: String): User {
        return User(name = name, email = email)
    }
}
