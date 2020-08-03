package com.example.dunzotest.viewmodel

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dunzotest.DunzoApplication
import com.example.dunzotest.DunzoConstant
import com.example.dunzotest.api.ApiInterface
import com.example.dunzotest.data.FlickrDataModel
import com.example.dunzotest.data.FlickrResponseModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val TAG = "SearchViewModel"
    private val flickrData = MutableLiveData<FlickrDataModel>()
    var pageNo = 1

    init {
        DunzoApplication.appComponent?.inject(this)
    }

    @Inject
    lateinit var api: ApiInterface

    fun getSearchResult(query: String, fromLoadMore: Boolean = false){
        if(fromLoadMore){
            pageNo++
        }
        else{
            pageNo = 1
        }
        api.getPhotos(method = DunzoConstant.METHOD_NAME, format = DunzoConstant.FORMAT_NAME, key = DunzoConstant.API_KEY, callaback = DunzoConstant.NOJSON_CALLBACK, perPage = 16, page = pageNo, query = query)
            .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<FlickrResponseModel>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onNext(response: FlickrResponseModel?) {
                response?.let {
                    flickrData.value = it.photos
                }

            }

            override fun onError(e: Throwable?) {
                Log.e(TAG, "error = " + e?.message)
            }

        })
    }

    fun getFlickrResponseData(): MutableLiveData<FlickrDataModel>{
        return flickrData
    }

    fun setZeroResult() {
        pageNo = 1;
    }

    fun onCrossClick(){
        flickrData.value = null
    }


    fun onLoadMore(query: String) {
        getSearchResult(query, fromLoadMore = true)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}