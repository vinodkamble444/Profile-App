package tk.andivinu.profileapp.ui.profilelist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import tk.andivinu.profileapp.model.Profile


class ProfileViewModel : ViewModel() {
    private val profile_picture = MutableLiveData<String>()
    private val id = MutableLiveData<Int>()
    private val firstName = MutableLiveData<String>()
    private val lastName = MutableLiveData<String>()
     val isFavorite=MutableLiveData<Boolean>()

    fun bind(profile: Profile) {
        id.value = profile.id
        profile_picture.value = profile.profile_picture
        firstName.value=profile.first_name
        lastName.value=profile.last_name
        isFavorite.value=profile.isFavorite
    }

    fun getprofile_picture(): MutableLiveData<String> {
        return profile_picture
    }

    fun getId(): MutableLiveData<Int> {
        return id
    }

    fun getFirstName(): MutableLiveData<String> {
        return firstName
    }

    fun getLastName(): MutableLiveData<String> {
        return lastName
    }
    fun getisFavorite(): MutableLiveData<Boolean> {
        return isFavorite
    }
    fun setisFavorite( value: Boolean)  {
         isFavorite.value=value
    }
}