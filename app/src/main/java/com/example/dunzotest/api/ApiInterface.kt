package com.example.dunzotest.api


import com.example.dunzotest.data.FlickrResponseModel

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @GET("services/rest/")
    fun getPhotos(@Query("method") method: String, @Query("text") query: String, @Query("api_key") key:String,
    @Query("format") format: String, @Query("nojsoncallback") callaback: Int, @Query("per_page") perPage: Int,
    @Query("page") page: Int): Observable<FlickrResponseModel>

}