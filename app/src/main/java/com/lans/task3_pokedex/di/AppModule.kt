package com.lans.task3_pokedex.di

import android.content.Context
import androidx.room.Room
import com.lans.task3_pokedex.common.Constants.BASE_URL
import com.lans.task3_pokedex.data.repository.AuthRepository
import com.lans.task3_pokedex.data.repository.FavoriteRepository
import com.lans.task3_pokedex.data.repository.ItemRepository
import com.lans.task3_pokedex.data.repository.PokemonRepository
import com.lans.task3_pokedex.data.repository.UserRepository
import com.lans.task3_pokedex.data.source.local.AppDatabase
import com.lans.task3_pokedex.data.source.local.DataStoreManager
import com.lans.task3_pokedex.data.source.local.dao.FavoriteDao
import com.lans.task3_pokedex.data.source.local.dao.UserDao
import com.lans.task3_pokedex.data.source.remote.api.PokeApi
import com.lans.task3_pokedex.domain.repository.IAuthRepository
import com.lans.task3_pokedex.domain.repository.IFavoriteRepository
import com.lans.task3_pokedex.domain.repository.IItemRepository
import com.lans.task3_pokedex.domain.repository.IPokemonRepository
import com.lans.task3_pokedex.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofitClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providePokeApi(okHttpClient: OkHttpClient): PokeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pokedexDB"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao
    }

    @Provides
    @Singleton
    fun provideAuthRepository(userDao: UserDao): IAuthRepository {
        return AuthRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dataStoreManager: DataStoreManager): IUserRepository {
        return UserRepository(dataStoreManager)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(pokeApi: PokeApi): IPokemonRepository {
        return PokemonRepository(pokeApi)
    }

    @Provides
    @Singleton
    fun provideItemRepository(pokeApi: PokeApi): IItemRepository {
        return ItemRepository(pokeApi)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao, api: PokeApi): IFavoriteRepository {
        return FavoriteRepository(favoriteDao, api)
    }
}