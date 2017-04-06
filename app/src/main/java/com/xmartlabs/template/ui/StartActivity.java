package com.xmartlabs.template.ui;

import android.content.Intent;
import android.os.Bundle;

import com.xmartlabs.template.controller.AuthController;
import com.xmartlabs.template.controller.SessionController;

import javax.inject.Inject;

public class StartActivity extends BaseActivity {
  @Inject
  AuthController authController;
  @Inject
  SessionController sessionController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    sessionController.getSession()
        .ifPresent(authController::setLoginInfo);
    Intent intent = Henson.with(getContext()).gotoMainActivity().build();

    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    getContext().startActivity(intent);
  }
}
