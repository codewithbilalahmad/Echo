package com.muhammad.echo.app

import android.app.Application
import com.muhammad.echo.app.di.appModule
import com.muhammad.echo.core.database.di.databaseModule
import com.muhammad.echo.echos.di.echoModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EchoApplication : Application(){
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EchoApplication)
            modules(databaseModule, appModule, echoModule)
        }
    }
}