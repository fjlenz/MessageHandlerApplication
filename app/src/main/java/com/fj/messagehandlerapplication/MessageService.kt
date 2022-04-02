package com.fj.messagehandlerapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MessageService {
    @GET("/messages")
    fun getMessageList() : Call<ArrayList<MessageModel>>

    @GET("/message/{id}")
    fun getMessage(@Path("id") id: String?) : Call<MessageModel>

    @PUT("/message/{id}")
    fun updateMessage(@Path("id") id: String?, @Body requestBody : MessageModel) : Call<MessageModel>

}