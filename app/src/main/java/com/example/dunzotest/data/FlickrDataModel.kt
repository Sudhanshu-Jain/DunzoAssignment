package com.example.dunzotest.data

data class FlickrDataModel(val page: Int, val pages: Long, val perpage: Int, val total: Long, val photo: List<PhotoModel>) {
}