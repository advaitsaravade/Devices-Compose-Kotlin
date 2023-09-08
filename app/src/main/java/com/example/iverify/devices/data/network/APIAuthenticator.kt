package com.example.iverify.devices.data.network

import com.example.iverify.devices.data.preferences.IEncryptedUserSettings
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class APIAuthenticator @Inject constructor(
    private val encryptedUserSettings: IEncryptedUserSettings
): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Check if the original request already has an Authorization header
        if (response.request.header("Authorization") != null) {
            return null
        }
        // Declare a variable to hold the modified request
        var request: Request? = null
        // Retrieve the authentication token from the encrypted user settings
        encryptedUserSettings.getAuthToken()?.let { authToken ->
            // If an authentication token is available, create a new request with the
            // "Authorization" header containing the token in the format "Bearer <token>"
            request = response.request.newBuilder()
                .header("Authorization", "Bearer $authToken")
                .build()
        }
        // Return the modified request or null if no modification is needed
        return request
    }
}