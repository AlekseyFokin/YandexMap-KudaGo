package org.sniffsnirr.simplephotogalery

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        //MapKitFactory.setApiKey("cb6deb10-8e91-46c1-ad1c-f5e89fd819e0")
        MapKitFactory.setApiKey("6648ffb7-eeea-46b5-aba4-5542f503de1f")
    }
}