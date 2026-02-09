package com.lossabinos.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.lossabinos.data.datastore.sync.SyncPreferences
import java.io.InputStream
import java.io.OutputStream


object SyncPreferencesSerializer : Serializer<SyncPreferences> {

    override val defaultValue: SyncPreferences =
        SyncPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SyncPreferences {
        try {
            return SyncPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException(
                "Cannot read SyncPreferences proto.",
                exception
            )
        }
    }

    override suspend fun writeTo(
        t: SyncPreferences,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}
