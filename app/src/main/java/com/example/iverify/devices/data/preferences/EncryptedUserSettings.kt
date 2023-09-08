package com.example.iverify.devices.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.iverify.devices.data.preferences.IEncryptedUserSettings.Companion.AUTH_TOKEN
import javax.inject.Inject

// Define an interface for encrypted user settings
interface IEncryptedUserSettings {

    companion object {
         const val AUTH_TOKEN = "AUTH_TOKEN" // Define a constant for the authentication token key
    }

    // Function to get the authentication token
    fun getAuthToken(): String?

    // Function to set the authentication token
    fun setAuthToken(token: String)

}

class EncryptedUserSettings @Inject constructor(
    context: Context
): IEncryptedUserSettings {

    companion object {
        private const val ENCRYPTED_PREF_NAME = "user_preferences" // Define a constant for the name of the encrypted preferences
    }

    // Declare variables for encrypted preferences and a master key
    private var encryptedPrefs: SharedPreferences? = null
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Initialize the encrypted preferences in the constructor
    init {
        encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Implementation of the getAuthToken function to retrieve the authentication token
    override fun getAuthToken(): String? {
        return encryptedPrefs?.getString(AUTH_TOKEN, null)
    }

    // Implementation of the setAuthToken function to set the authentication token
    override fun setAuthToken(token: String) {
        encryptedPrefs?.edit()?.apply {
            putString(AUTH_TOKEN, token)
            apply()
        }
    }

}