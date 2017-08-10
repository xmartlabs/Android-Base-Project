package com.xmartlabs.bigbang.core.providers

import com.xmartlabs.bigbang.core.Injector
import com.xmartlabs.bigbang.core.common.EntityProvider
import com.xmartlabs.bigbang.core.controller.SessionController
import com.xmartlabs.bigbang.core.module.SessionInterceptor

import javax.inject.Inject

/**
 * Provides the access token in order to be added in the service requests.
 * It's used by the [SessionInterceptor].
 */
open class AccessTokenProvider : EntityProvider<String> {
  companion object {
    protected open val AUTH_TOKEN_HEADER_KEY = "session"
  }
  
  @Inject
  internal lateinit var sessionController: SessionController

  init {
    Injector.inject(this)
  }

  override fun provideEntity() = sessionController.abstractSession?.accessToken

  /**
   * Provides the access token header key.
   * This should be overridden to change the header key.
   *
   * @return the access token header key
   */
  open fun provideAccessTokenHeaderKey() = AUTH_TOKEN_HEADER_KEY

  override fun updateEntity(entity: String) {
    sessionController.abstractSession
        ?.apply { accessToken = entity }
        ?.let(sessionController::saveSession)
  }

  override fun deleteEntity() {
    sessionController.abstractSession
        ?.apply { accessToken = null }
        ?.let(sessionController::saveSession)
  }
}
