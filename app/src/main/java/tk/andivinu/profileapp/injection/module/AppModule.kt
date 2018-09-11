package tk.andivinu.profileapp.injection.module


import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import tk.andivinu.profileapp.model.ProfileDao
import tk.andivinu.profileapp.model.database.Database
import javax.inject.Singleton


@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext


    @Provides
    @Singleton
    fun provideProfileDatabase(app: Application): Database =
            Room.databaseBuilder(app, Database::class.java, "profile_db").build()

    @Provides
    @Singleton
    fun provideProfileDao(db: Database): ProfileDao = db.profileDao()
}