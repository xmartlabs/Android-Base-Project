package com.xmartlabs.template.common.rx;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * Implementation of {@link MaybeObserver} that calls a {@link Consumer} function {@code onError}.
 * To be used as a MaybeSubscribe hook with RxJavaPlugins.
 */
@RequiredArgsConstructor
public final class MaybeObserverWithErrorHandling<T> implements MaybeObserver<T> {
  private final MaybeObserver<T> maybeObserver;
  private final Consumer<? super Throwable> onErrorCallback;

  @Override
  public void onSubscribe(Disposable d) {
    maybeObserver.onSubscribe(d);
  }

  @Override
  public void onSuccess(T t) {
    maybeObserver.onSuccess(t);
  }

  @Override
  public void onError(Throwable e) {
    try {
      onErrorCallback.accept(e);
    } catch (Exception exception) {
      Timber.e(exception);
    }
    maybeObserver.onError(e);
  }

  @Override
  public void onComplete() {
    maybeObserver.onComplete();
  }
}
