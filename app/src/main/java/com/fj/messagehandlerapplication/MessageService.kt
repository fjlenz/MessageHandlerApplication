package com.fj.messagehandlerapplication

import retrofit2.Call
import retrofit2.http.GET

interface MessageService {
    @GET("/messages")
    //fun getMessageList() : Call<MessagesModel>
    fun getMessageList() : Call<ArrayList<MessageModel>>

}