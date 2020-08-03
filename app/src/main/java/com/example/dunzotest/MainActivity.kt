package com.example.dunzotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dunzotest.databinding.ActivityMainBinding
import com.example.dunzotest.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    lateinit var binder: ActivityMainBinding
    val layoutManager: GridLayoutManager by lazy { getGridLayoutManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateView(savedInstanceState)
        searchViewModel = ViewModelProvider(this)
            .get(SearchViewModel::class.java)




    }

    private fun inflateView(savedInstanceState: Bundle?) {
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        binder.lifecycleOwner = this
        binder.viewModel = searchViewModel
    }


    fun getGridLayoutManager(): GridLayoutManager {

        val gridLayoutManager = GridLayoutManager(this, 2)
        return gridLayoutManager
    }
}