package com.example.iverify.devices.ui.activity

import androidx.lifecycle.ViewModel
import com.example.iverify.devices.BuildConfig
import com.example.iverify.devices.data.preferences.IEncryptedUserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Annotate the ViewModel with HiltViewModel for dependency injection
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val encryptedUserSettings: IEncryptedUserSettings
): ViewModel() {
    /*
    This function setups up this prototype app to act as if
    the client is already authed. In the production app, this
    will be done through a login screen where the user enters
    their license, which is then validated by server by sending
    an access token.
     */
    fun setupFirstTimeToken() {
        if(encryptedUserSettings.getAuthToken() == null) {
            encryptedUserSettings.setAuthToken(
                token = BuildConfig.API_AUTH_TOKEN
            )
        }
    }
}