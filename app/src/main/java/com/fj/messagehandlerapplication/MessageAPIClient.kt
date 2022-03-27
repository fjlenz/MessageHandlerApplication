package com.fj.messagehandlerapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MessageAPIClient {

    // URL for API Call
    private val BASE_URL = "https://spring-boot-playground-datastore-jolguo2gba-lm.a.run.app"
    private var retrofitClient : Retrofit? = null

    fun getClient(): Retrofit {

        if (retrofitClient == null) {
            retrofitClient = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return this.retrofitClient!!
    }

}