package tk.andivinu.profileapp.ui.profilelist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tk.andivinu.profileapp.R
import tk.andivinu.profileapp.model.Profile
import tk.andivinu.profileapp.model.ProfileDao
import tk.andivinu.profileapp.network.ProfileApi
import javax.inject.Inject

class ProfileListViewModel @Inject constructor(private val profileDao: ProfileDao) : ViewModel() {
    @Inject
    lateinit var profileApi: ProfileApi
    @Inject
    lateinit var context: Context
    var profileList: MutableLiveData<List<Profile>> = MutableLiveData()
    val resultListAdapter: ProfileListAdapter = ProfileListAdapter({ profileItem: Profile -> profileItemClicked(profileItem) })
    val profileId: MutableLiveData<Int> = MutableLiveData()
    val profile: MutableLiveData<Profile> = MutableLiveData()
    val profileDB: MutableLiveData<Profile> = MutableLiveData()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadProfileList() }
    private lateinit var subscription: Disposable


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadProfileList() {
        subscription = Observable.fromCallable { profileDao.all }
                .concatMap { dbProfileList ->
                    if (dbProfileList.isEmpty()) {
                        profileApi.getProfileList()
                                .concatMap { apiProfileList ->
                                    profileDao.insertAll(*apiProfileList.toTypedArray())
                                    Observable.fromCallable { profileDao.all }
                                }
                    } else
                        Observable.just(dbProfileList)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveProfileListStart() }
                .doOnTerminate { onRetrieveProfileListFinish() }
                .subscribe(
                        { result -> onRetrieveProfileListSuccess(result) },
                        { result -> onRetrieveProfileListError(result) }
                )
    }


    fun loadSelectedProfile(id: Int) {
        subscription = profileApi.getProfileDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveProfileListStart() }
                .doOnTerminate { onRetrieveProfileListFinish() }
                .subscribe(
                        { result -> onRetrieveProfileDetailsSuccess(result) },
                        { result -> onRetrieveProfileDetailsError(result) }
                )
    }
    fun loadSelectedProfileDB(id: Int) {
        subscription = Observable.fromCallable{profileDao.loadProfile(id)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveProfileListStart() }
                .doOnTerminate { onRetrieveProfileListFinish() }
                .subscribe(
                        { result -> onRetrieveProfileDetailsDBSuccess(result) },
                        { result -> onRetrieveProfileDetailsDBError(result) }
                )
    }
    fun addToFavorites(profile: Profile) {
        subscription =Observable.fromCallable { profileDao.update(profile)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> onProfileUpdatedSuccess(result) },
                        { result -> onProfileUpdatedError(result) }
                )
    }

     fun  onProfileUpdatedError(result: Throwable?){
         Log.d("VINOD", "onProfileUpdatedError " + result.toString())
     }

    fun  onProfileUpdatedSuccess(result: Int?){
        Log.d("VINOD", "onProfileUpdatedSuccess " + result.toString())
    }

    fun onRetrieveProfileDetailsSuccess(profile: Profile) {
        Log.d("VINOD", "onRetrieveProfileDetailsSuccess " + profile.toString())
        this.profile.value = profile
    }

    private fun onRetrieveProfileDetailsError(result: Throwable) {
        Log.d("VINOD", "onRetrieveProfileDetailsError " + result.localizedMessage)
        errorMessage.value = R.string.data_error
    }

    fun onRetrieveProfileDetailsDBSuccess(profile: Profile) {
        Log.d("VINOD", "onRetrieveProfileDetailsDBSuccess " + profile.toString())
        this.profileDB.value = profile
    }

    private fun onRetrieveProfileDetailsDBError(result: Throwable) {
        Log.d("VINOD", "onRetrieveProfileDetailsDBSuccess " + result.localizedMessage)
        }

    private fun onRetrieveProfileListStart() {
        Log.d("VINOD", "onRetrieveProfileListStart")
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveProfileListFinish() {
        loadingVisibility.value = View.GONE
    }

    fun onRetrieveProfileListSuccess(poiList: List<Profile>) {
        Log.d("VINOD", "onRetrieveProfileListSuccess " + poiList.toString())
        profileList.value = poiList
        resultListAdapter.updateProfileList(poiList)
    }

    fun onRetrieveProfileListError(result: Throwable) {
        Log.d("VINOD", "onRetrieveProfileListError " + result.localizedMessage)
        errorMessage.value = R.string.data_error
    }

    private fun profileItemClicked(profile: Profile) {
        Log.d("VINOD", "profileItemClicked" + profile.toString())
        profileId.value = profile.id
        this.profile.value = profile
       // Log.d("VINOD", "profileItemClicked" + this.profile.toString())

    }

    fun onButtonClick(compoundButton:  CompoundButton?, isChecked: Boolean) {
        if(isChecked) {
            profile.value!!.isFavorite = true
        }else{
            profile.value!!.isFavorite = false
        }
        addToFavorites(profile.value!!)

    }

}