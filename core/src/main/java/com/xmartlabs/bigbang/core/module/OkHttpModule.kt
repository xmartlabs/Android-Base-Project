package com.xmartlabs.bigbang.core.module

import android.content.Context
import android.os.Build
import android.os.StatFs
import com.moczul.ok2curl.CurlInterceptor
import com.xmartlabs.bigbang.core.model.BuildInfo
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
open class OkHttpModule {
  companion object {
    const private val MIN_DISK_CACHE_SIZE = 10 * 1024 * 1024 // 10MB
    const private val MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024 // 100MB
    const val CLIENT_PICASSO = "Picasso"
    const val CLIENT_SERVICE = "Service"
  }

  @Named(CLIENT_SERVICE)
  @Provides
  @Singleton
  open fun provideServiceOkHttpClient(clientBuilder: OkHttpClient.Builder, buildInfo: BuildInfo): OkHttpClient {
    addLoggingInterceptor(clientBuilder, buildInfo)
    return clientBuilder.build()
  }

  @Named(CLIENT_PICASSO)
  @Provides
  @Singleton
  open fun providePicassoOkHttpClient(clientBuilder: OkHttpClient.Builder, cache: Cache,
                                      buildInfo: BuildInfo): OkHttpClient {
    clientBuilder.cache(cache)
    addLoggingInterceptor(clientBuilder, buildInfo)
    return clientBuilder.build()
  }

  @Provides
  open fun provideOkHttpClientBuilder() = OkHttpClient.Builder()

  @Provides
  @Singleton
  open fun provideCache(context: Context): Cache {
    val httpCacheDir = File(context.externalCacheDir, "cache")
    if (!httpCacheDir.exists()) {
      httpCacheDir.mkdirs()
    }
    val httpCacheSize = calculateDiskCacheSize(httpCacheDir)
    return Cache(httpCacheDir, httpCacheSize)
  }

  private fun addLoggingInterceptor(clientBuilder: OkHttpClient.Builder, buildInfo: BuildInfo) {
    if (buildInfo.isDebug) {
      val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
      loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
      clientBuilder.addInterceptor(loggingInterceptor)

      val curlInterceptor = CurlInterceptor { message -> Timber.tag("Ok2Curl").d(message) }
      clientBuilder.addInterceptor(curlInterceptor)
    }
  }

  // Taken from: https://github.com/square/picasso/blob/eb2e9730fb7dbe25b4ab9d4ba5d2050c70c27024/picasso/src/main/java/com/squareup/picasso/Utils.java#L255
  private fun calculateDiskCacheSize(dir: File): Long {
    var size = MIN_DISK_CACHE_SIZE.toLong()

    try {
      val statFs = StatFs(dir.absolutePath)
      val blockCount: Long
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockCount = statFs.blockCountLong
      } else {
        @Suppress("DEPRECATION")
        blockCount = statFs.blockCount.toLong()
      }
      val available = blockCount * blockCount
      // Target 2% of the total space.
      size = available / 50
    } catch (e: IllegalArgumentException) {
      Timber.e(e, "Error while trying to calculate disk cache size")
    }

    // Bound inside min/max size for disk cache.
    return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE.toLong()), MIN_DISK_CACHE_SIZE.toLong())
  }
}
