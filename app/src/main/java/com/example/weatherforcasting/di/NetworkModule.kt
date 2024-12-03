package com.example.weatherforcasting.di

import com.banquemisr.challenge05.data.source.remote.LocalEndPoint
import com.example.weatherforcasting.BuildConfig
import com.example.weatherforcasting.data.Constants.API_KEY_K
import com.example.weatherforcasting.data.Constants.API_VALUE
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


@InstallIn(ViewModelComponent::class, FragmentComponent::class, ServiceComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideRetrofitBuilder(): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getAuthInterceptor())
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @ViewModelScoped
    fun getAuthEndPoints(): LocalEndPoint {
            return provideEndPoints(provideRetrofitBuilder()).create(
            LocalEndPoint::class.java
        )
    }
    @Provides
    fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val url: HttpUrl =
                original.url.newBuilder()
                    .addQueryParameter(API_KEY_K,
                        API_VALUE).build()// add query in request
            chain.proceed(
                original.newBuilder().url(url)
                    .header("Accept","application/json")
                    .method(original.method, original.body)
                    .build()
            )
        }
    }

    @Provides
    fun provideEndPoints(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.baseUrlWeather)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
          //  .create(EndPoints::class.java)
    }

}