package com.example.dunzotest

import android.app.Application
import com.example.dunzotest.di.AppComponent
import com.example.dunzotest.di.AppModule
import com.example.dunzotest.di.DaggerAppComponent
import com.example.dunzotest.di.RetrofitModule

class DunzoApplication : Application() {


    companion object{
        var appComponent: AppComponent? = null
    }


    private fun initApplication() {

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).retrofitModule(RetrofitModule()).build()

    }

    override fun onCreate() {
        super.onCreate()
        initApplication()
    }

}