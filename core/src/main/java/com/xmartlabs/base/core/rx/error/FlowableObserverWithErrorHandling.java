package com.xmartlabs.base.core.rx.error;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * Implementation of {@link Subscriber} that calls a {@link Consumer} function {@code onError}.
 * To be used as a FlowableSubscribe hook with RxJavaPlugins.
 */
@RequiredArgsConstructor
public final class FlowableObserverWithErrorHandling<T> implements Subscriber<T> {
  private final Subscriber<T> subscriber;
  private final Consumer<? super Throwable> onErrorCallback;

  @Override
  public void onSubscribe(Subscription s) {
    subscriber.onSubscribe(s);
  }

  @Override
  public void onNext(T t) {
    subscriber.onNext(t);
  }

  @Override
  public void onError(Throwable t) {
    try {
      onErrorCallback.accept(t);
    } catch (Exception exception) {
      Timber.e(exception);
    }
    subscriber.onError(t);
  }

  @Override
  public void onComplete() {
    subscriber.onComplete();
  }
}
