package com.fj.messagehandlerapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MessageService {
    @GET("/messages")
    fun getMessageList() : Call<ArrayList<MessageModel>>

    @GET("/message/{id}")
    fun getMessage(@Path("id") id: String?) : Call<MessageModel>

}