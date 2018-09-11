package tk.andivinu.profileapp.ui.profilelist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import dagger.android.AndroidInjection
import tk.andivinu.profileapp.R
import tk.andivinu.profileapp.databinding.ActivityProfileDetailBinding
import tk.andivinu.profileapp.model.Profile
import javax.inject.Inject

class ProfileDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileListViewModel
    private var profileId: Int = 0
    private var isFavorite: Boolean =false
    private lateinit var binding: ActivityProfileDetailBinding
    private var errorSnackbar: Snackbar? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_detail)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileListViewModel::class.java)
        binding.profileDetailItemViewModel = viewModel

        var intentThatStartedThisActivity = getIntent()
        if (intentThatStartedThisActivity!=null) {
            profileId = intentThatStartedThisActivity.getIntExtra("id", 0)
            isFavorite = intentThatStartedThisActivity.getBooleanExtra("isFavorite", false)

             binding.btnFavorite.isChecked = isFavorite
            if( binding.btnFavorite.isChecked){
                binding.btnFavorite.textOn=getString(R.string.remove_from_favorites)

            }
            viewModel.loadSelectedProfile(profileId)
           // viewModel.loadSelectedProfileDB(profileId)

            viewModel.profile.observe(this, Observer { profile ->
                if (profile != null) {
                    showProfileDetails(profile)
                }
            })

            viewModel.errorMessage.observe(this, Observer {
                errorMessage ->
                if (errorMessage != null) showError(errorMessage) else hideError()
            })

        }


    }

    private fun showProfileDetails(profile: Profile) {
        Glide.with(this).load(profile.profile_picture).into(binding.ImageView1)
        setTitle(profile.first_name + " " + profile.last_name)
        binding.btnFavorite.visibility = View.VISIBLE
    }


    public override fun onResume() {
        super.onResume()
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

}


