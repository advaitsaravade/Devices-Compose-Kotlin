package com.example.iverify.devices.data.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant

// This is a helper function fo serializing Instant type
class InstantDeserializer : JsonDeserializer<Instant> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant {
        return Instant.parse(json!!.asString)
    }
}