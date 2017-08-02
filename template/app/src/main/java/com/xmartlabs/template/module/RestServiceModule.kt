package com.xmartlabs.template.module

import com.xmartlabs.bigbang.retrofit.module.RestServiceModule as CoreRestServiceModule

import android.content.Context
import com.xmartlabs.template.App
import com.xmartlabs.template.R
import okhttp3.HttpUrl

class RestServiceModule : CoreRestServiceModule() {
  companion object {
    private val BASE_URL = App.context.resources.getString(R.string.base_url)
  }

  override fun provideBaseUrl(context: Context) = HttpUrl.parse(BASE_URL)
}
