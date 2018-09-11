package tk.andivinu.profileapp.injection.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import tk.andivinu.profileapp.ui.profilelist.ProfileDetailActivity
import tk.andivinu.profileapp.ui.profilelist.ProfileListActivity



@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeProfileDetailActivity(): ProfileDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeProfileListActivity(): ProfileListActivity
}