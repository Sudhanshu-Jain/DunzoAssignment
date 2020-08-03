package com.example.dunzotest.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

object RetrofitClient {

    var BASE_URL:String=" https://api.flickr.com/"
    val getClient: ApiInterface
        get() {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val client = OkHttpClient.Builder().build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(ApiInterface::class.java)

        }

}