package org.sniffsnirr.simplephotogalery

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
       MapKitFactory.setApiKey("")
    }
}
