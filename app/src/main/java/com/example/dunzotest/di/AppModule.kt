package com.example.dunzotest.di

import android.app.Application
import android.content.Context
import com.example.dunzotest.DunzoApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(application: DunzoApplication) {

    var application : DunzoApplication?=null

    init{
        this.application = application
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application?.applicationContext!!
    }
}