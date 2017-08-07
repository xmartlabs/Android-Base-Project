package com.xmartlabs.bigbang.core.observers;

import com.tspoon.traceur.Traceur;
import com.tspoon.traceur.TraceurConfig;
import com.xmartlabs.bigbang.core.helpers.TestingTree;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ObserversWithErrorHandlingTest {
  private final TestingTree testingTree = new TestingTree();

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    Timber.plant(testingTree);
    setUpLog(ObservableResult.ERROR_HANDLING_ACTION);
  }

  private void setUpLog(@NonNull Consumer<? super Throwable> action) {
    TraceurConfig config = new TraceurConfig(
        true,
        Traceur.AssemblyLogLevel.NONE,
        action::accept);
    Traceur.enableLogging(config);
  }

  @Test
  public void callsObservableSubscribe() {
    Observable.empty()
        .doOnSubscribe(disposable ->
            Timber.tag(ObservableResult.SUBSCRIBE.toString()).i(ObservableResult.SUBSCRIBE.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.SUBSCRIBE).getTag(),
        equalTo(ObservableResult.SUBSCRIBE.toString()));
  }

  @Test
  public void callsObservableOnComplete() {
    Observable.empty()
        .doOnComplete(() -> Timber.tag(ObservableResult.SUCCESS.toString()).i(ObservableResult.SUCCESS.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.SUCCESS).getTag(),
        equalTo(ObservableResult.SUCCESS.toString()));
  }

  @Test
  public void callsObservableOnNextCorrectAmountOfTimes() {
    Observable.range(0, 5)
        .doOnNext(objects -> Timber.tag(ObservableResult.ON_NEXT.toString()).i(ObservableResult.ON_NEXT.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodesCountWithTag(ObservableResult.ON_NEXT), equalTo(5L));
  }

  @Test
  public void callsObservableOnError() {
    Observable.error(Throwable::new)
        .doOnError(throwable ->
            TestingTree.log(ObservableResult.DO_ON_ERROR))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.DO_ON_ERROR).getTag(),
        equalTo(ObservableResult.DO_ON_ERROR.toString()));
  }

  @Test
  public void doesNotCallOnCompleteWhenError() {
    Observable.error(Throwable::new)
        .doOnComplete(() -> Timber.e(ObservableResult.SUCCESS.toString()))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.SUCCESS).getTag(),
        equalTo(TestingTree.DEFAULT_NOT_FOUND_TREE_NODE));
  }

  @Test
  public void doesNotCallOnCompleteWhenErrorAndErrorInHookErrorHandle() {
    setUpLog(ObservableResult.ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE);

    Observable.error(Throwable::new)
        .doOnError(throwable ->
            TestingTree.log(ObservableResult.DO_ON_ERROR))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.DO_ON_ERROR).getTag(),
        equalTo(ObservableResult.DO_ON_ERROR.toString()));
  }

  @Test
  public void callsHookErrorHandleWhenNoDoOnError() {
    Observable.error(Throwable::new)
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.ENTERED_HOOK_ERROR_HANDLE).getTag(),
        equalTo(ObservableResult.ENTERED_HOOK_ERROR_HANDLE.toString()));
  }

  @Test
  public void catchesHookOnErrorCallbackExceptionAndLogsIt() {
    setUpLog(ObservableResult.ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE);

    Observable.error(Throwable::new)
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeExceptionDetailMessage(ObservableResult.EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE),
        equalTo(ObservableResult.EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE.toString()));
  }

  @Test
  public void callsHookOnErrorAndObservableOnError() {
    Observable.error(Throwable::new)
        .doOnError(throwable -> TestingTree.log(ObservableResult.DO_ON_ERROR))
        .doOnComplete(() -> assertThat("Executed OnComplete", equalTo("Didn't execute onComplete")))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.ENTERED_HOOK_ERROR_HANDLE).getTag(),
        equalTo(ObservableResult.ENTERED_HOOK_ERROR_HANDLE.toString()));

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.DO_ON_ERROR).getTag(),
        equalTo(ObservableResult.DO_ON_ERROR.toString()));
  }

  @Test
  public void catchesHookOnErrorCallbackExceptionAndLogsItAndCallsObservableOnError() {
    setUpLog(ObservableResult.ERROR_HANDLING_ACTION_WITH_ERROR_INSIDE);

    Observable.error(Throwable::new)
        .doOnError(throwable -> TestingTree.log(ObservableResult.DO_ON_ERROR))
        .doOnComplete(() -> assertThat("Executed OnComplete", equalTo("Didn't execute onComplete")))
        .subscribe(o -> {});

    assertThat(testingTree.getLogTreeNodeWithTag(ObservableResult.DO_ON_ERROR).getTag(),
        equalTo(ObservableResult.DO_ON_ERROR.toString()));

    assertThat(testingTree.getLogTreeExceptionDetailMessage(ObservableResult.EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE),
        equalTo(ObservableResult.EXCEPTION_WHILE_ON_ERROR_HOOK_HANDLE.toString())
    );
  }
}
