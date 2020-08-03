package com.example.dunzotest.di

import com.example.dunzotest.MainActivity
import com.example.dunzotest.viewmodel.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,RetrofitModule::class])
interface AppComponent {
    fun inject(o: SearchViewModel)
}