package com.example.twitter.di

import com.banquemisr.challenge05.data.source.remote.TwitterEndPoint
import com.example.twitter.data.Constants.ACCESS_TOKEN_KEY
import com.example.twitter.data.Constants.VALID_ACCESS_TOKEN
import com.example.twitter.data.Constants.accessToken
import com.example.twitter.data.Constants.consumerKey
import com.example.twitter.data.Constants.consumerSecret
import com.example.weatherforcasting.BuildConfig
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
    fun getAuthEndPoints(): TwitterEndPoint {
            return provideEndPoints(provideRetrofitBuilder()).create(
            TwitterEndPoint::class.java
        )
    }
    @Provides
    fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            /*val url: HttpUrl =
                original.url.newBuilder()
                    .addQueryParameter(consumerKey,
                        consumerSecret).build()// add query in request */
            chain.proceed(
                original.newBuilder()//.url(url)
                    .header("Accept","application/json")
                    .header(ACCESS_TOKEN_KEY,"Bearer " + VALID_ACCESS_TOKEN)
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
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
          //  .create(EndPoints::class.java)
    }

}