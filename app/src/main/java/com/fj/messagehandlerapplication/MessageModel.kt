package com.fj.messagehandlerapplication

import com.google.gson.annotations.SerializedName

class MessageModel {
    @SerializedName("id")
    val id = "";
    @SerializedName("message")
    var messageText = ""
    @SerializedName("rating")
    var rating = 0

    override fun toString(): String {
        return id + " " + messageText
    }
}
