package com.example.iverify.devices.models.entities.network

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class DeviceNetworkEntity(
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("device") val device: String?,
    @SerializedName("latestScanDate") val latestScanDate: Instant?
)