package com.xmartlabs.template.module;

import com.xmartlabs.template.controller.AuthController;
import com.xmartlabs.template.controller.SessionController;
import com.xmartlabs.template.controller.SharedPreferencesController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ControllerModule {
  @Provides
  @Singleton
  AuthController provideAuthController() {
    return new AuthController();
  }

  @Provides
  @Singleton
  SessionController provideSessionController() {
    return new SessionController();
  }

  @Provides
  @Singleton
  SharedPreferencesController provideSharedPreferencesController() {
    return new SharedPreferencesController();
  }
}
