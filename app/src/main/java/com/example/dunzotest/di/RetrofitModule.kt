package com.example.dunzotest.di

import com.example.dunzotest.api.ApiInterface
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
public class RetrofitModule {

    @Provides
    fun getApiInterface(retroFit: Retrofit): ApiInterface {
        return retroFit.create<ApiInterface>(ApiInterface::class.java)
    }

    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun getOkHttpCleint(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

}