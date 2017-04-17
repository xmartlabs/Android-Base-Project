package com.xmartlabs.base.core.observers;

import com.xmartlabs.base.core.helpers.TestingTree;
import com.xmartlabs.base.core.rx.error.ObserverWithErrorHandling;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;

import static com.xmartlabs.base.core.helpers.TestingTree.DEFAULT_NOT_FOUND_TREE_NODE;
import static com.xmartlabs.base.core.observers.ObservableResult.DO_ON_ERROR;
import static com.xmartlabs.base.core.observers.ObservableResult.ENTERED_HOOK_ERROR_HANDLE;
import static com.xmartlabs.base.core.observers.ObservableResult.ERROR_HANDLING_ACTION;
import static com.xmartlabs.base.core.observers.ObservableResult.ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE;
import static com.xmartlabs.base.core.observers.ObservableResult.EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE;
import static com.xmartlabs.base.core.observers.ObservableResult.ON_NEXT;
import static com.xmartlabs.base.core.observers.ObservableResult.SUBSCRIBE;
import static com.xmartlabs.base.core.observers.ObservableResult.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ObserversWithErrorHandlingTest {
  private TestingTree testingTree = new TestingTree();

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    Timber.plant(testingTree);
    RxJavaPlugins.setOnObservableSubscribe((observable, observer) ->
        new ObserverWithErrorHandling<>(observer, ERROR_HANDLING_ACTION));
  }

  @Test
  public void callsObservableSubscribe() {
    Observable.empty()
        .doOnSubscribe(disposable -> Timber.tag(SUBSCRIBE.toString()).i(SUBSCRIBE.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(SUBSCRIBE.toString()).getTag(), equalTo(SUBSCRIBE.toString()));
  }

  @Test
  public void callsObservableOnComplete() {
    Observable.empty()
        .doOnComplete(() -> Timber.tag(SUCCESS.toString()).i(SUCCESS.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(SUCCESS.toString()).getTag(), equalTo(SUCCESS.toString()));
  }

  @Test
  public void callsObservableOnNextCorrectAmountOfTimes() {
    Observable.range(0, 5)
        .doOnNext(objects -> Timber.tag(ON_NEXT.toString()).i(ON_NEXT.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodesCountWithTag(ON_NEXT.toString()), equalTo(5L));
  }

  @Test
  public void callsObservableOnError() {
    Observable.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(DO_ON_ERROR.toString()).i(DO_ON_ERROR.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(DO_ON_ERROR.toString()).getTag(), equalTo(DO_ON_ERROR.toString()));
  }

  @Test
  public void doesNotCallOnCompleteWhenError() {
    Observable.error(Throwable::new)
        .doOnComplete(() -> Timber.e(SUCCESS.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(SUCCESS.toString()).getTag(), equalTo(DEFAULT_NOT_FOUND_TREE_NODE));
  }

  @Test
  public void doesNotCallOnCompleteWhenErrorAndErrorInHookErrorHandle() {
    //noinspection unchecked
    RxJavaPlugins.setOnObservableSubscribe((observable, observer) ->
        new ObserverWithErrorHandling<>(observer, ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE));

    Observable.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(DO_ON_ERROR.toString()).i(DO_ON_ERROR.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(DO_ON_ERROR.toString()).getTag(), equalTo(DO_ON_ERROR.toString()));
  }

  @Test
  public void callsHookErrorHandleWhenNoDoOnError() {
    Observable.error(Throwable::new)
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ENTERED_HOOK_ERROR_HANDLE.toString()).getTag(), equalTo(ENTERED_HOOK_ERROR_HANDLE.toString()));
  }

  @Test
  public void catchesHookOnErrorCallbackExceptionAndLogsIt() {
    //noinspection unchecked
    RxJavaPlugins.setOnObservableSubscribe((observable, observer) ->
        new ObserverWithErrorHandling<>(observer, ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE));

    Observable.error(Throwable::new)
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeExceptionDetailMessage(EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE.toString()),
        equalTo(EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE.toString()));
  }

  @Test
  public void callsHookOnErrorAndObservableOnError() {
    Observable.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(DO_ON_ERROR.toString()).i(DO_ON_ERROR.toString()))
        .doOnComplete(() -> assertThat("Executed OnComplete", equalTo("Didn't execute onComplete")))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ENTERED_HOOK_ERROR_HANDLE.toString()).getTag(),
        equalTo(ENTERED_HOOK_ERROR_HANDLE.toString()));
    assertThat(testingTree.getLogTreeNodeWithTag(DO_ON_ERROR.toString()).getTag(), equalTo(DO_ON_ERROR.toString()));
  }

  @Test
  public void catchesHookOnErrorCallbackExceptionAndLogsItAndCallsObservableOnError() {
    //noinspection unchecked
    RxJavaPlugins.setOnObservableSubscribe((observable, observer) ->
        new ObserverWithErrorHandling<>(observer, ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE));

    Observable.error(Throwable::new)
        .doOnError(throwable -> Timber.tag(DO_ON_ERROR.toString()).i(DO_ON_ERROR.toString()))
        .doOnComplete(() -> assertThat("Executed OnComplete", equalTo("Didn't execute onComplete")))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(DO_ON_ERROR.toString()).getTag(), equalTo(DO_ON_ERROR.toString()));
    assertThat(testingTree.getLogTreeExceptionDetailMessage(EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE.toString()), equalTo(EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE.toString()));
  }
}
