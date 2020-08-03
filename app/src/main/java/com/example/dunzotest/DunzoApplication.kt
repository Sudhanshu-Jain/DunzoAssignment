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
        /**
         *  FIXME: move to latest DI later , right nor most of them are commented
         */
        //DaggerSVAppComponent.create()
        //  .inject(this);
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).retrofitModule(RetrofitModule()).build()



        //By default logs are disabled


    }

    override fun onCreate() {
        super.onCreate()
        initApplication()
    }

}