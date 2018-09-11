package tk.andivinu.profileapp.model

import android.arch.persistence.room.*


@Dao
interface ProfileDao {
    @get:Query("SELECT * FROM Profile")
    val all: List<Profile>

    @Insert
    fun insertAll(vararg profile: Profile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg profile: Profile) :Int

    @Query("SELECT * FROM Profile WHERE id = :Id")
    fun loadProfile(Id: Int): Profile
}

