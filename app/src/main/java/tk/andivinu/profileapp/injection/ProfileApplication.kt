package tk.andivinu.profileapp.injection

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import tk.andivinu.profileapp.BuildConfig
import tk.andivinu.profileapp.injection.component.DaggerAppComponent
import tk.andivinu.profileapp.injection.module.AppModule
import tk.andivinu.profileapp.injection.module.NetworkModule
import javax.inject.Inject


class ProfileApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(BuildConfig.URL))
                .build()
                .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}