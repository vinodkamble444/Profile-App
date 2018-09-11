package tk.andivinu.profileapp.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import tk.andivinu.profileapp.model.Profile
import tk.andivinu.profileapp.model.ProfileDao




@Database(entities = arrayOf(Profile::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

}