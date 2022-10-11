package com.example.casebycase.park

import android.media.session.MediaSession
import android.media.session.MediaSession.Token
import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("pressure") val pressure:String?,
    @SerializedName("lat") val lat:String?,
    @SerializedName("lng") val lng:String?,
    @SerializedName("tem") val tem:String?,
    @SerializedName("humi") val humi:String?,

)

data class ParkResponse(
    val MESSAGE: List<Message>
)

