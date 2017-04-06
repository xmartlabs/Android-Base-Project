package com.xmartlabs.template.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Objects;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.xmartlabs.template.BuildConfig;
import com.xmartlabs.template.service.adapter.MillisecondsLocalDateAdapter;
import com.xmartlabs.template.service.adapter.MillisecondsLocalDateTimeAdapter;
import com.xmartlabs.template.service.gson.GsonExclude;
import com.xmartlabs.template.service.gson.RequiredFieldDeserializer;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {
  static final String SERVICE_GSON_NAME = "ServiceGson";

  @Provides
  @Singleton
  Gson provideGson(GsonBuilder gsonBuilder) {
    return gsonBuilder
        .setExclusionStrategies(getExclusionStrategy(null))
        .create();
  }

  @Named(SERVICE_GSON_NAME)
  @Provides
  @Singleton
  @SuppressWarnings("unused")
  Gson provideServiceGson(GsonBuilder gsonBuilder) {
    gsonBuilder
        .setExclusionStrategies(getExclusionStrategy(GsonExclude.Strategy.SERVICE))
        .registerTypeAdapter(LocalDateTime.class, new MillisecondsLocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate.class, new MillisecondsLocalDateAdapter());

    return gsonBuilder.create();
  }

  private ExclusionStrategy getExclusionStrategy(@Nullable GsonExclude.Strategy strategy) {
    return new ExclusionStrategy() {
      @Override
      public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getDeclaredClass().equals(ModelAdapter.class)
            || (fieldAttributes.getAnnotation(GsonExclude.class) != null
            && (Objects.equals(fieldAttributes.getAnnotation(GsonExclude.class).strategy(), GsonExclude.Strategy.ALL)
            || Objects.equals(fieldAttributes.getAnnotation(GsonExclude.class).strategy(), strategy)));
      }

      @Override
      public boolean shouldSkipClass(Class<?> clazz) {
        return false;
      }
    };
  }

  @NonNull
  @Provides
  GsonBuilder provideCommonGsonBuilder() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    if (BuildConfig.DEBUG) {
      gsonBuilder.setPrettyPrinting();
    }
    gsonBuilder.registerTypeHierarchyAdapter(Object.class, new RequiredFieldDeserializer());
    return gsonBuilder;
  }
}
