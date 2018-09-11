package tk.andivinu.profileapp.network;

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import tk.andivinu.profileapp.model.Profile


/**
 * The interface which provides methods to get result of webservices
 */
interface ProfileApi {
    /**
     * Get the list of the profile from the API
     *
     */

    @GET("/profiles")
    fun getProfileList(): Observable<List<Profile>>


    @GET("/profiles/{Id}")
    fun getProfileDetails(@Path("Id") id : Int): Observable<Profile>



}
