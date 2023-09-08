package com.example.iverify.devices.models.usecases

sealed class UseCaseFailReason {
    object NoInternet: UseCaseFailReason()
    object ServerError: UseCaseFailReason()
    data class Unknown(val message: String? = null): UseCaseFailReason()
}