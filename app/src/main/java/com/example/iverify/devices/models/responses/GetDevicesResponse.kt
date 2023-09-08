package com.example.iverify.devices.models.responses

import com.example.iverify.devices.models.entities.network.DeviceNetworkEntity
import com.google.gson.annotations.SerializedName

data class GetDevicesResponse(
    @SerializedName("devices") val devices: List<DeviceNetworkEntity>,
    @SerializedName("totalPages") val pages: Int
)
