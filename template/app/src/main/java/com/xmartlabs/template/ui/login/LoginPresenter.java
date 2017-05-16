package com.xmartlabs.template.ui.login;

import android.support.annotation.StringRes;

import com.annimon.stream.Optional;
import com.xmartlabs.bigbang.core.Injector;
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController;
import com.xmartlabs.bigbang.core.helper.ObjectHelper;
import com.xmartlabs.bigbang.ui.mvp.BaseMvpPresenter;
import com.xmartlabs.template.controller.AuthController;

import javax.inject.Inject;

import lombok.Builder;

@Builder
public class LoginPresenter extends BaseMvpPresenter<LoginView> {
  @Inject
  AuthController authController;
  @Inject
  ObjectHelper objectHelper;
  @Inject
  SharedPreferencesController sharedPreferencesController;

  @Override
  public void attachView(LoginView view) {
    super.attachView(view);
    Injector.inject(this);
  }

  public void loginButtonClicked() {
    //TODO: Do something on login button clicked
    Optional.ofNullable(getView())
        .ifPresent(LoginView::gotoRecyclerExampleActivity);
  }

  private void showErrorIfPresent(@StringRes int stringRes) {
    Optional.ofNullable(getView())
        .ifPresent(view -> view.showError(stringRes));
  }

  private void setIsLoading(boolean loading) {
    Optional.ofNullable(getView())
        .ifPresent(view -> view.setIsLoading(loading));
  }

  private String getString(@StringRes int stringRes) {
    return Optional.ofNullable(getView())
        .map(view -> view.getString(stringRes))
        .orElse("");
  }
}
