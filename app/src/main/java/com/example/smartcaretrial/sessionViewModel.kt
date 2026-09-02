package com.example.smartcaretrial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Holds who is currently logged in, for as long as the app process is alive.
 *
 * One instance is created in MainActivityNavigation (so it's scoped to the
 * Activity, not to any single screen) and passed down as a parameter to
 * whichever composables need to know "who am I right now":
 *  - Login calls login(user) once the DB lookup succeeds.
 *  - Profile / Directory / anything "personalized" reads currentUser.
 *  - The Logout drawer item calls logout().
 *
 * This does NOT persist across app restarts — if you need "stay logged in
 * after closing the app," that's a separate step (e.g. saving the user id
 * in DataStore/SharedPreferences and re-loading it on launch).
 */
class SessionViewModel : ViewModel() {

    var currentUser by mutableStateOf<UserInfo?>(null)
        private set

    val isLoggedIn: Boolean
        get() = currentUser != null

    fun login(user: UserInfo) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }
}