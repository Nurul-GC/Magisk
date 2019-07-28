package com.topjohnwu.magisk.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.skoumal.teanity.rxbus.RxBus
import com.topjohnwu.magisk.App
import org.koin.dsl.module

val applicationModule = module {
    single { RxBus() }
    factory { get<Context>().resources }
    factory { get<Context>() as App }
    factory { get<Context>().packageManager }
    factory(Protected) { get<App>().protectedContext }
    single(SUTimeout) { get<Context>(Protected).getSharedPreferences("su_timeout", 0) }
    single { PreferenceManager.getDefaultSharedPreferences(get<Context>(Protected)) }
    single { ActivityTracker() as Application.ActivityLifecycleCallbacks }
    factory { (get<Application.ActivityLifecycleCallbacks>() as ActivityTracker).foreground }
}

private class ActivityTracker : Application.ActivityLifecycleCallbacks {

    var foreground: Activity? = null

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    @Synchronized
    override fun onActivityResumed(activity: Activity) {
        foreground = activity
    }

    @Synchronized
    override fun onActivityPaused(activity: Activity) {
        foreground = null
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
