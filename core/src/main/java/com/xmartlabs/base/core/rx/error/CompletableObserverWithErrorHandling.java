package com.xmartlabs.base.core.rx.error;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * Implementation of {@link CompletableObserver} that calls a {@link Consumer} function {@code onError}.
 * To be used as a CompletableSubscribe hook with RxJavaPlugins.
 */
@RequiredArgsConstructor
public final class CompletableObserverWithErrorHandling implements CompletableObserver {
  private final CompletableObserver completableObserver;
  private final Consumer<? super Throwable> onErrorCallback;

  @Override
  public void onSubscribe(Disposable d) {
    completableObserver.onSubscribe(d);
  }

  @Override
  public void onComplete() {
    completableObserver.onComplete();
  }

  @Override
  public void onError(Throwable e) {
    try {
      onErrorCallback.accept(e);
    } catch (Exception exception) {
      Timber.e(exception);
    }
    completableObserver.onError(e);
  }
}
