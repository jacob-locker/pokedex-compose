package com.locker.catapp.di

import android.content.Context
import com.locker.catapp.model.IPokeRepository
import com.locker.catapp.model.PokeRepository
import com.locker.catapp.model.PokeWebService
import com.locker.catapp.model.hasNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object BackendModule {
    @Provides
    fun providePokeWebService(@ApplicationContext context: Context): PokeWebService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (context.hasNetwork()!!) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 60).build()
                } else {
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                }

                chain.proceed(request)
            }
            .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
            .build())
        .build()
        .create(PokeWebService::class.java)

    @Provides
    fun providePokeRepository(pokeWebService: PokeWebService): IPokeRepository = PokeRepository(pokeWebService)
}