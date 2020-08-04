package com.example.dunzotest.ui

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dunzotest.R
import com.example.dunzotest.data.FlickrDataModel
import com.example.dunzotest.databinding.ActivityMainBinding
import com.example.dunzotest.viewmodel.SearchViewModel
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    lateinit var binder: ActivityMainBinding
    val layoutManager: GridLayoutManager by lazy { getGridLayoutManager() }
    lateinit var adapter: SearchAdapter
    var scrollListener: EndlessScrollingListener? = null
    var scrolledPosition = 0

    companion object{
        const val TAG = "MainActivity"
    }
    var searchedString : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(this)
            .get(SearchViewModel::class.java)
        inflateView(savedInstanceState)


        RxTextView.afterTextChangeEvents(binder.frSearchEdit)
            .map {
                val query: String = it.view().text.toString()
                if (query.trim().isNotEmpty()) {
                    binder.frIvSearchCross.visibility = View.VISIBLE
                }
                else{
                    binder.frIvSearchCross.visibility = View.GONE
                }


                if (query.trim().length < 3) {
                    searchedString = query.trim()
                    searchViewModel.setZeroResult()
                    scrollListener?.resetState()

                    return@map
                } else if (query.trim().length >= 3) {
                    binder.progress.visibility = View.VISIBLE
                    searchedString = query
                    Log.d(TAG, searchedString)

                    scrollListener?.resetState()

                    if(isInternetAvailable(this))
                        searchViewModel.getSearchResult(searchedString)
                    else{
                        Toast.makeText(this, getString(R.string.internet_required), Toast.LENGTH_SHORT).show()
                        binder.progress.visibility = View.GONE

                    }

                    return@map
                }
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{it.printStackTrace()}
            .subscribe()


        scrollListener = object : EndlessScrollingListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                searchViewModel.onLoadMore(searchedString)
            }

            override fun onScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScroll(recyclerView, dx, dy)
                scrolledPosition = dy

            }
        }
        setRecyclerAdapter()
        observeLiveData()

        binder.searchGrid.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(p0, this@SearchActivity)
                return false
            }

        })

    }

    private fun observeLiveData() {

        searchViewModel.getFlickrResponseData().observe(this, object : Observer<FlickrDataModel>{
            override fun onChanged(data: FlickrDataModel?) {
               data?.let {
                   if(it.photo.size > 0) {
                       binder.emptyTxt.visibility = View.GONE
                       binder.searchGrid.visibility = View.VISIBLE
                       if (it.page == 1) {
                           adapter.items = it.photo
                           adapter.notifyDataSetChanged()
                       } else {
                           adapter.items += it.photo
                           adapter.notifyDataSetChanged()
                       }
                   }else{
                       binder.emptyTxt.text = getString(R.string.no_result)
                       binder.searchGrid.visibility = View.GONE
                       binder.emptyTxt.visibility = View.VISIBLE
                   }


               }
                   ?: kotlin.run {
                       binder.searchGrid.visibility = View.GONE
                       binder.emptyTxt.text = getString(R.string.enter_3)
                       binder.emptyTxt.visibility = View.VISIBLE
                       binder.frIvSearchCross.visibility = View.GONE
                       binder.frSearchEdit.setText("")
                   }
                binder.progress.visibility = View.GONE
            }


        })
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putInt("position", scrolledPosition) // get current recycle view position here.
        state.putString("query", searchedString)
    }

    // Restore list state from bundle
    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)
        scrolledPosition = state.getInt("position")
        searchedString = state.getString("query") ?: ""
        binder.frSearchEdit.setText(searchedString)
        binder.searchGrid.smoothScrollBy(0,scrolledPosition)
    }
    private fun setRecyclerAdapter() {
        binder.searchGrid.layoutManager = layoutManager
        adapter = SearchAdapter()
        binder.searchGrid.adapter = adapter
        binder.searchGrid.addOnScrollListener(scrollListener as EndlessScrollingListener)
    }

    private fun inflateView(savedInstanceState: Bundle?) {
        binder = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        ) as ActivityMainBinding
        binder.lifecycleOwner = this
        binder.viewModel = searchViewModel
    }


    fun getGridLayoutManager(): GridLayoutManager {

        val gridLayoutManager = GridLayoutManager(this, if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        return gridLayoutManager
    }


    fun hideKeyboard(v: View?, context: Context?) {
        if (context != null) {
            try {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun isInternetAvailable(context: Context): Boolean
    {
        try {
            val conn = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = conn.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.isConnected
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return false
    }
}