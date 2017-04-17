package com.xmartlabs.base.core.observers;

import com.xmartlabs.base.core.rx.error.SingleObserverWithErrorHandling;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class SingleMainObserversWithErrorHandlingTest extends MainObserversWithErrorHandlingTest {
  @Override
  @SuppressWarnings("unchecked")
  protected void setObserverSubscriber() {
    RxJavaPlugins.setOnSingleSubscribe((single, singleObserver) ->
        new SingleObserverWithErrorHandling<>(singleObserver, getErrorHandlingAction()));
  }

  @Test
  public void callsSingleSubscribe() {
    Single.just(new Object())
        .doOnSubscribe(disposable -> Timber.tag(OBSERVABLE_SUBSCRIBE).i(OBSERVABLE_SUBSCRIBE))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(OBSERVABLE_SUBSCRIBE).getTag(), equalTo(OBSERVABLE_SUBSCRIBE));
  }

  @Test
  public void callsSingleOnSuccess() {
    Single.just(new Object())
        .doOnSuccess(o -> Timber.tag(OBSERVABLE_SUCCESS).i(OBSERVABLE_SUCCESS))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(OBSERVABLE_SUCCESS).getTag(), equalTo(OBSERVABLE_SUCCESS));
  }

  @Test
  public void callsSingleOnError() {
    Single.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(OBSERVABLE_DO_ON_ERROR).i(OBSERVABLE_DO_ON_ERROR))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(OBSERVABLE_DO_ON_ERROR).getTag(), equalTo(OBSERVABLE_DO_ON_ERROR));
  }

  @Test
  public void doesNotCallOnSuccessWhenError() {
    Single.error(Throwable::new)
        .doOnSuccess(o -> Timber.e(OBSERVABLE_SUCCESS))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(OBSERVABLE_SUCCESS).getTag(), equalTo(DEFAULT_NOT_FOUND_TREE_NODE));
  }

  @Test
  public void doesNotCallOnSuccessWhenErrorAndErrorInHookErrorHandle() {
    //noinspection unchecked
    RxJavaPlugins.setOnSingleSubscribe((Single, SingleObserver) ->
        new SingleObserverWithErrorHandling(SingleObserver, getErrorHandlingActionWithErrorInside()));

    Single.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(OBSERVABLE_DO_ON_ERROR).i(OBSERVABLE_DO_ON_ERROR))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(OBSERVABLE_DO_ON_ERROR).getTag(), equalTo(OBSERVABLE_DO_ON_ERROR));
  }

  @Test
  public void callsHookErrorHandleWhenNoDoOnError() {
    Single.error(Throwable::new)
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(ENTERED_HOOK_ERROR_HANDLE).getTag(), equalTo(ENTERED_HOOK_ERROR_HANDLE));
  }

  @Test
  public void catchesHookOnErrorCallbackExceptionAndLogsIt() {
    //noinspection unchecked
    RxJavaPlugins.setOnSingleSubscribe((Single, SingleObserver) ->
        new SingleObserverWithErrorHandling(SingleObserver, getErrorHandlingActionWithErrorInside()));

    Single.error(Throwable::new)
        .subscribe(o -> {});

    assertThat(getLogTreeExceptionDetailMessage(EXCEPTION_WHILE_HANDLING_ERROR_WITH_HOOK),
        equalTo(EXCEPTION_WHILE_HANDLING_ERROR_WITH_HOOK));
  }

  @Test
  public void callsHookOnErrorAndSingleOnError() {
    Single.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(OBSERVABLE_DO_ON_ERROR).i(OBSERVABLE_DO_ON_ERROR))
        .doOnSuccess(o -> assertThat("Executed OnSuccess", equalTo("Didn't execute OnSuccess")))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(ENTERED_HOOK_ERROR_HANDLE).getTag(), equalTo(ENTERED_HOOK_ERROR_HANDLE));
    assertThat(getLogTreeNodeWithTag(OBSERVABLE_DO_ON_ERROR).getTag(), equalTo(OBSERVABLE_DO_ON_ERROR));
  }

  @Test
  public void catchesHookOnErrorCallbackExceptionAndLogsItAndCallsSingleOnError() {
    //noinspection unchecked
    RxJavaPlugins.setOnSingleSubscribe((Single, SingleObserver) ->
        new SingleObserverWithErrorHandling(SingleObserver, getErrorHandlingActionWithErrorInside()));

    Single.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(OBSERVABLE_DO_ON_ERROR).i(OBSERVABLE_DO_ON_ERROR))
        .doOnSuccess(o -> assertThat("Executed OnSuccess", equalTo("Didn't execute OnSuccess")))
        .subscribe(o -> {});

    assertThat(getLogTreeNodeWithTag(OBSERVABLE_DO_ON_ERROR).getTag(), equalTo(OBSERVABLE_DO_ON_ERROR));
    assertThat(getLogTreeExceptionDetailMessage(EXCEPTION_WHILE_HANDLING_ERROR_WITH_HOOK),
        equalTo(EXCEPTION_WHILE_HANDLING_ERROR_WITH_HOOK));
  }
}
