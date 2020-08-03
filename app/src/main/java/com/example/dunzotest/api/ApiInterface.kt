package com.example.dunzotest.api

import com.example.dunzotest.data.FlickrDataModel
import com.example.dunzotest.data.PhotoModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("services/rest")
    fun getPhotos(): Observable<FlickrDataModel>

}