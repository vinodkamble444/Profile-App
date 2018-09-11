package tk.andivinu.profileapp.injection.component

import dagger.Component
import dagger.android.AndroidInjectionModule
import tk.andivinu.profileapp.injection.ProfileApplication
import tk.andivinu.profileapp.injection.module.AppModule
import tk.andivinu.profileapp.injection.module.BuildersModule
import tk.andivinu.profileapp.injection.module.NetworkModule
import tk.andivinu.profileapp.injection.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, BuildersModule::class, ViewModelModule::class, AppModule::class, NetworkModule::class))
interface AppComponent {
    fun inject(app: ProfileApplication)
}