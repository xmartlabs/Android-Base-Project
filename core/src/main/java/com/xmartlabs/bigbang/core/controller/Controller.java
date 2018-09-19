package com.xmartlabs.bigbang.core.controller;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.xmartlabs.bigbang.core.Injector;
import com.xmartlabs.bigbang.core.helper.IoSchedulersTransformationHelper;
import com.xmartlabs.bigbang.core.helper.RxTransformerHelper;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.schedulers.Schedulers;

/**
 * Contains general controller methods.
 * It automatically injects inherited classes.
 */
public abstract class Controller {
  protected Controller() {
    Injector.inject(this);
  }

  /**
   * @deprecated The transformation shouldn't observe on the {Android main thread. You should be the one
   * to decide when to observe on the main thread. Instead of this, use
   * {@link IoSchedulersTransformationHelper#applyCompletableIoSchedulersTransformation()}
   * (it subscribes and observes on Io bound {@link Schedulers})
   *
   * Provides the Io schedule {@link Completable} transformation.
   * Subscribes the stream to Io bound {@link Schedulers} and observes it in the {Android main thread.
   *
   * @return The stream with the schedule transformation
   */
  @CheckResult
  @Deprecated
  @NonNull
  protected CompletableTransformer applyCompletableIoSchedulers() {
    return RxTransformerHelper.completableIoAndMainThreadTransformer;
  }

  /**
   * @deprecated The transformation shouldn't observe on the {Android main thread. You should be the one
   * to decide when to observe on the main thread. Instead of this, use
   * {@link IoSchedulersTransformationHelper#applyFlowableIoSchedulersTransformation()}
   * (it subscribes and observes on Io bound {@link Schedulers})
   *
   * Provides the Io schedule {@link Observable} transformation.
   * Subscribes the stream to Io bound {@link Flowable} and observes it in the {Android main thread.
   *
   * @return The stream with the schedule transformation
   */
  @CheckResult
  @Deprecated
  @NonNull
  protected <T> FlowableTransformer<T, T> applyFlowableIoSchedulers() {
    //noinspection unchecked
    return (FlowableTransformer<T, T>) RxTransformerHelper.flowableIoAndMainThreadTransformer;
  }

  /**
   * @deprecated The transformation shouldn't observe on the {Android main thread. You should be the one
   * to decide when to observe on the main thread. Instead of this, use
   * {@link IoSchedulersTransformationHelper#applySingleIoSchedulersTransformation()}
   * (it subscribes and observes on Io bound {@link Schedulers})
   *
   * Provides the Io schedule {@link Maybe} transformation.
   * Subscribes the stream to Io bound {@link Schedulers} and observes it in the {Android main thread.
   *
   * @return The stream with the schedule transformation
   */
  @CheckResult
  @Deprecated
  @NonNull
  protected <T> MaybeTransformer<T, T> applyMaybeIoSchedulers() {
    //noinspection unchecked
    return (MaybeTransformer<T, T>) RxTransformerHelper.maybeIoAndMainThreadTransformer;
  }

  /**
   * @deprecated The transformation shouldn't observe on the {Android main thread. You should be the one
   * to decide when to observe on the main thread. Instead of this, use
   * {@link IoSchedulersTransformationHelper#applySingleIoSchedulersTransformation()}
   * (it subscribes and observes on Io bound {@link Schedulers})
   *
   * Provides the Io schedule {@link Observable} transformation.
   * Subscribes the stream to Io bound {@link Schedulers} and observes it in the {Android main thread.
   *
   * @return The stream with the schedule transformation
   */
  @CheckResult
  @Deprecated
  @NonNull
  protected <T> ObservableTransformer<T, T> applyObservableIoSchedulers() {
    //noinspection unchecked
    return (ObservableTransformer<T, T>) RxTransformerHelper.observableIoAndMainThreadTransformer;
  }

  /**
   * @deprecated The transformation shouldn't observe on the {Android main thread. You should be the one
   * to decide when to observe on the main thread. Instead of this, use
   * {@link IoSchedulersTransformationHelper#applySingleIoSchedulersTransformation()}
   * (it subscribes and observes on Io bound {@link Schedulers})
   *
   * Provides the Io schedule {@link Single} transformation.
   * Subscribes the stream to Io bound {@link Schedulers} and observes it in the {Android main thread.
   *
   * @return The stream with the schedule transformation
   */
  @CheckResult
  @Deprecated
  @NonNull
  protected <T> SingleTransformer<T, T> applySingleIoSchedulers() {
    //noinspection unchecked
    return (SingleTransformer<T, T>) RxTransformerHelper.singleIoAndMainThreadTransformer;
  }
}
