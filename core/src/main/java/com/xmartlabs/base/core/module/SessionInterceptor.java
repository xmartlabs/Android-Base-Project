package com.xmartlabs.base.core.module;

import android.support.annotation.NonNull;

import com.xmartlabs.base.core.providers.AccessTokenProvider;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SessionInterceptor implements Interceptor {
  @NonNull
  private final AccessTokenProvider accessTokenProvider;

  @Inject
  public SessionInterceptor(@NonNull AccessTokenProvider accessTokenProvider) {
    this.accessTokenProvider = accessTokenProvider;
  }

  @NonNull
  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = accessTokenProvider.provideEntity()
        .map(accessToken -> chain.request()
            .newBuilder()
            .addHeader(accessTokenProvider.provideAccessTokenHeaderKey(), accessToken)
            .build()
        ).orElse(chain.request());
    return chain.proceed(request);
  }
}
