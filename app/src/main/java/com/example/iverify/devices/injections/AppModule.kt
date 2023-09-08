package com.example.iverify.devices.injections

import android.app.Application
import android.util.Log
import com.example.iverify.devices.BuildConfig
import com.example.iverify.devices.data.network.APIAuthenticator
import com.example.iverify.devices.data.network.IInternetAPI
import com.example.iverify.devices.data.network.deserializer.InstantDeserializer
import com.example.iverify.devices.data.preferences.EncryptedUserSettings
import com.example.iverify.devices.data.preferences.IEncryptedUserSettings
import com.example.iverify.devices.data.repository.DevicesRepository
import com.example.iverify.devices.data.repository.IDevicesRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesEncryptedUserSettings(
        context: Application
    ): IEncryptedUserSettings {
        return EncryptedUserSettings(
            context = context
        )
    }

    @Provides
    @Singleton
    fun providesAPIAuthenticator(
        encryptedUserSettings: IEncryptedUserSettings
    ): APIAuthenticator {
        return APIAuthenticator(
            encryptedUserSettings
        )
    }

    @Provides
    @Singleton
    fun providesAPI(
        apiAuthenticator: APIAuthenticator
    ): IInternetAPI {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .serializeNulls()
                        .registerTypeAdapter(Instant::class.java, InstantDeserializer())
                        .create()
                )
            )
            .client(
                OkHttpClient.Builder()
                    .certificatePinner(
                        // Setup a certificate pins to secure network connections on weak devices
                        CertificatePinner.Builder()
                        .add(BuildConfig.API_DOMAIN, BuildConfig.API_CERTIFICATE_HASH)
                        .build()
                    )
                    .authenticator(apiAuthenticator)
                    .addInterceptor(HttpLoggingInterceptor { message ->
                        // Add logging if debug mode is enabled
                        if(BuildConfig.DEBUG) {
                            if (!message.contains("�")) Log.i("IInternetAPI", message)
                        }
                    }.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()
            .create(IInternetAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesDevicesRepository(
        api: IInternetAPI
    ): IDevicesRepository {
        return DevicesRepository(
            api = api
        )
    }
}