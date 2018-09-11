package tk.andivinu.profileapp.ui.profilelist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import dagger.android.AndroidInjection
import tk.andivinu.profileapp.R
import tk.andivinu.profileapp.databinding.ActivityProfileListBinding
import tk.andivinu.profileapp.model.Profile
import javax.inject.Inject

class ProfileListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileListBinding
    private lateinit var viewModel: ProfileListViewModel
    private var errorSnackbar: Snackbar? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_list)
        binding.profileList.layoutManager = GridLayoutManager(this, 2)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(ProfileListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer {
            errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        viewModel.profile.observe(this, Observer {
            profile ->
            if (profile != null) profileItemClicked(profile)
        })
        binding.viewModel = viewModel
      }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    private fun profileItemClicked(profile: Profile) {
        // Launch Profile Detail activity, pass profile ID as int parameter
        val showDetailActivityIntent = Intent(this, ProfileDetailActivity::class.java)
        showDetailActivityIntent.putExtra("id", profile.id)
        showDetailActivityIntent.putExtra("isFavorite", profile.isFavorite)
        startActivity(showDetailActivityIntent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProfileList()
    }
}