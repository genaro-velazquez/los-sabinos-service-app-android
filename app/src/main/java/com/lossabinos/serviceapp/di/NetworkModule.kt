package com.lossabinos.serviceapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.lossabinos.data.datasource.local.TokenManager
import com.lossabinos.data.datasource.remoto.interceptor.TokenRefreshInterceptor
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.AuthenticationServices
import com.lossabinos.data.retrofit.MechanicsServices
import com.lossabinos.data.retrofit.SyncServices
import com.lossabinos.data.utilities.CurlLoggingInterceptor
import com.lossabinos.data.utilities.NetworkStateMonitor
import com.lossabinos.domain.usecases.authentication.RefreshSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Qualifier
annotation class NoInterceptorOkHttp

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // 🆕 OkHttpClient SIN interceptor (para AuthenticationServices)
    @Singleton
    @Provides
    @NoInterceptorOkHttp
    fun provideOkHttpClientNoInterceptor(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(CurlLoggingInterceptor())
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    // 🆕 Retrofit SIN interceptor (para AuthenticationServices)
    @Singleton
    @Provides
    @NoInterceptorOkHttp
    fun provideRetrofitNoInterceptor(
        @NoInterceptorOkHttp okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    // 🆕 AuthenticationServices usa Retrofit SIN interceptor
    @Singleton
    @Provides
    fun provideAuthenticationServices(
        @NoInterceptorOkHttp retrofit: Retrofit
    ): AuthenticationServices {
        return retrofit.create(AuthenticationServices::class.java)
    }

    // ✅ ORIGINAL: OkHttpClient CON interceptor
    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenRefreshInterceptor: TokenRefreshInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenRefreshInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(CurlLoggingInterceptor())
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    // ✅ ORIGINAL: Retrofit CON interceptor
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    // ✅ ORIGINAL: MechanicsServices usa Retrofit CON interceptor
    @Singleton
    @Provides
    fun provideMechanicsServices(retrofit: Retrofit): MechanicsServices {
        return retrofit.create(MechanicsServices::class.java)
    }

    // ✅ ORIGINAL: SyncServices usa Retrofit CON interceptor
    @Singleton
    @Provides
    fun provideSyncSyncServices(retrofit: Retrofit): SyncServices {
        return retrofit.create(SyncServices::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideNetworkStateMonitor(
        connectivityManager: ConnectivityManager
    ): NetworkStateMonitor {
        return NetworkStateMonitor(connectivityManager)
    }
}