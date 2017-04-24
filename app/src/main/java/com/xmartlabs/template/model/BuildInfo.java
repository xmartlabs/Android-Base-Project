package com.xmartlabs.template.model;

import com.annimon.stream.Objects;
import com.xmartlabs.template.BuildConfig;

public class BuildInfo implements com.xmartlabs.base.core.model.BuildInfo {
  @Override
  public boolean isDebug() {
    return BuildConfig.DEBUG;
  }

  @Override
  public boolean isStaging() {
    return Objects.equals(BuildConfig.FLAVOR, BuildType.STAGING);
  }

  @Override
  public boolean isProduction() {
    return Objects.equals(BuildConfig.FLAVOR, BuildType.PRODUCTION);
  }
}
