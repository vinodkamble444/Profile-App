package tk.andivinu.profileapp.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tk.andivinu.profileapp.injection.ViewModelFactory
import tk.andivinu.profileapp.injection.ViewModelKey
import tk.andivinu.profileapp.ui.profilelist.ProfileListViewModel

/**
 * Module which provides ViewModelModule
 */
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfileListViewModel::class)
    internal abstract fun bindProfileListViewModel(profileListViewModel: ProfileListViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}