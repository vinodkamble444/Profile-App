package tk.andivinu.profileapp.injection.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import tk.andivinu.profileapp.BuildConfig
import tk.andivinu.profileapp.model.Profile
import tk.andivinu.profileapp.network.ProfileApi
import tk.andivinu.profileapp.utils.DefaultOnDataMismatchAdapter
import tk.andivinu.profileapp.utils.FilterNullValuesFromListAdapter
import java.util.*
import javax.inject.Singleton

/**
 * Module which provides all required dependencies about network
 */

@Module
class NetworkModule(private val baseUrl: String) {

    /**
     * Provides the ProfileApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the ProfileApi service implementation.
     */

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi =
            retrofit.create<ProfileApi>(ProfileApi::class.java)
    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .add(DefaultOnDataMismatchAdapter.newFactory(Profile::class.java, null))
                        .add(FilterNullValuesFromListAdapter.newFactory(Profile::class.java))
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    @Provides
    @Singleton
    fun provideHttpCache(app: android.app.Application): Cache {
        val cacheSize = 10 * 1024 * 1024L
        return Cache(app.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
        client.cache(cache)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }
    }
}