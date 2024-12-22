package com.example.twitter.di

import com.banquemisr.challenge05.data.source.remote.TwitterEndPoint
import com.example.twitter.data.cititesrepo.TwitterRepo
import com.example.twitter.data.cititesrepo.TwitterRepoImplementer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(
    ViewModelComponent::class, FragmentComponent::class,
    ActivityComponent::class
)
class RepoDi {
    @Provides
    fun getRepoWeather(
        endPoints: TwitterEndPoint
    ): TwitterRepo {
        return TwitterRepoImplementer(endPoints)
    }


}