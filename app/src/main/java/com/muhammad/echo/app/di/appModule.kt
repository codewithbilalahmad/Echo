package com.muhammad.echo.app.di

import com.muhammad.echo.app.EchoApplication
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as EchoApplication).applicationScope
    }
}