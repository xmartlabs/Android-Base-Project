package com.xmartlabs.template;

import com.xmartlabs.base.core.module.AndroidModule;
import com.xmartlabs.base.core.module.GsonModule;
import com.xmartlabs.base.core.module.LoggerModule;
import com.xmartlabs.base.core.module.OkHttpModule;
import com.xmartlabs.base.core.module.PicassoModule;
import com.xmartlabs.base.core.module.RestServiceModule;
import com.xmartlabs.template.module.ControllerModule;
import com.xmartlabs.template.module.MockRestServiceModule;
import com.xmartlabs.template.ui.common.BaseInstrumentationTest;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
    AndroidModule.class,
    ControllerModule.class,
    LoggerModule.class,
    GsonModule.class,
    RestServiceModule.class,
    OkHttpModule.class,
    PicassoModule.class,
})
@Singleton
public interface TestComponent extends ApplicationComponent {
  void inject(BaseInstrumentationTest baseInstrumentationTest);

  void inject(TestRunner testRunner);
}
