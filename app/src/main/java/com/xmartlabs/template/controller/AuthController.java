package com.xmartlabs.template.controller;

import android.support.annotation.NonNull;

import com.xmartlabs.template.model.AuthResponse;
import com.xmartlabs.template.model.LoginRequest;
import com.xmartlabs.template.model.Session;
import com.xmartlabs.template.service.AuthService;

import javax.inject.Inject;

import io.reactivex.Single;

public class AuthController extends ServiceController {
  @Inject
  AuthService authService;
  @Inject
  SessionController sessionController;

  public Single<Session> login(LoginRequest loginRequest) {
    // TODO
    //authService.login();
    return Single.just(new AuthResponse())
            .map(sessionController::setSession)
            .doOnSuccess(this::setLoginInfo);
  }

  public void setLoginInfo(@NonNull Session session) {
    // TODO
  }
}
