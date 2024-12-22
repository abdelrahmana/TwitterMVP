package com.example.twitter.di

import com.example.twitter.domain.LogicClass
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
class CommonDi {
    @Provides
    fun getLogicClass(
    ): LogicClass {
        return LogicClass()
    }

}