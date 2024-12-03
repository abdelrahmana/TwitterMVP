package com.example.weatherforcasting.di

import com.banquemisr.challenge05.data.source.remote.LocalEndPoint
import com.example.weatherforcasting.data.cititesrepo.LocalNotification
import com.example.weatherforcasting.data.cititesrepo.LocalRepoImplementer
import com.example.weatherforcasting.data.source.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class,
    ActivityComponent::class)
class RepoDi {
    @Provides
    fun getRepoWeather(
        localDataBase: AppDataBase, endPoints: LocalEndPoint
    ): LocalNotification {
        return  LocalRepoImplementer(endPoints)
    }


}