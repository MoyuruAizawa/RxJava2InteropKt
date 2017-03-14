package com.lvla.rxjava.interopkt

import com.taroid.knit.should
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableOperator
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class RxJavaInteropKtTest {

  @Test
  fun o1f2() {
    val a = arrayOf(0, 1, 2)
    rx.Observable.from(a).toV2Flowable().test().assertResult(*a)
  }

  @Test
  fun o1o2() {
    val a = arrayOf(0, 1, 2)
    rx.Observable.from(a).toV2Observable().test().assertResult(*a)
  }

  @Test
  fun s1s2() {
    rx.Single.just(1).toV2Single().test().assertResult(1)
  }

  @Test
  fun c1c2() {
    var i = 0
    rx.Completable.create {
      i++
      it.onCompleted()
    }.toV2Completable().test().assertComplete()
    i.should be 1
  }

  @Test
  fun s1m2() {
    rx.Single.just(1).toV2Maybe().test().assertResult(1)
  }

  @Test
  fun c1m2() {
    var i = 0
    rx.Completable.create {
      i++
      it.onCompleted()
    }.toV2Maybe<Int>().test().assertComplete()
    i.should be 1
  }

  @Test
  fun p2o1() {
    val a = arrayOf(0, 1, 2)
    Publisher<Int> {
      a.forEach(it::onNext)
      it.onComplete()
    }.toV1Observable().test().assertResult(*a)
  }

  @Test
  fun o2o1() {
    val a = arrayOf(0, 1, 2)
    Observable.fromArray(a).toV1Observable(BackpressureStrategy.LATEST).test().assertResult(a)
  }

  @Test
  fun s2s1() {
    Single.just(1).toV1Single().test().assertResult(1)
  }

  @Test
  fun c2c1() {
    var i = 0
    Completable.create {
      i++
      it.onComplete()
    }.toV1Completable().test().assertCompleted()
    i.should be 1
  }

  @Test
  fun m2s1() {
    Maybe.just(1).toV1Single().test().assertResult(1)
  }

  @Test
  fun m2c1() {
    Maybe.just(1).toCompletable().test().assertCompleted()
  }

  @Test
  fun sj1sj2() {
    val s = rx.subjects.PublishSubject.create<Int>()
    val t = s.toV2Subject().test()

    s.hasObservers().should be true

    val a = arrayOf(0, 1, 2)
    a.forEach(s::onNext)
    s.onCompleted()
    t.assertResult(*a)
  }

  @Test
  fun sj1p2() {
    val s = rx.subjects.PublishSubject.create<Int>()
    val t = s.toV2Processor().test()

    s.hasObservers().should be true

    val a = arrayOf(0, 1, 2)
    a.forEach(s::onNext)
    s.onCompleted()
    t.assertResult(*a)
  }

  @Test
  fun sj2sj1() {
    val s = PublishSubject.create<Int>()
    val t = s.toV1Subject().test()

    s.hasObservers().should be true

    val a = arrayOf(0, 1, 2)
    a.forEach(s::onNext)
    s.onComplete()
    t.assertResult(*a)
  }

  @Test
  fun fp2sj1() {
    val fp: FlowableProcessor<Int> = PublishProcessor.create<Int>()
    val t = fp.toV1Subject().test()

    fp.hasSubscribers().should be true

    val a = arrayOf(0, 1, 2)
    a.forEach(fp::onNext)
    fp.onComplete()
    t.assertResult(*a)
  }

  @Test
  fun ot1ot2() {
    val t = rx.Observable.Transformer<Int, Int> { o -> o.map { it + 1 } }

    Flowable.just(1).compose(t.toV2Transformer()).test().assertResult(2)
  }

  @Test
  fun st1st2() {
    val t = rx.Single.Transformer<Int, Int> { o -> o.map { it + 1 } }

    Single.just(1).compose(t.toV2Transformer()).test().assertResult(2)
  }

  @Test
  fun ct1ct2() {
    var i = 0
    val t = rx.Completable.Transformer { c -> c.doOnCompleted { i++ } }

    Completable.complete().compose(t.toV2Transformer()).test().assertResult()
    i.should be 1
  }

  @Test
  fun ft2ot1() {
    val ft = FlowableTransformer<Int, Int> { f -> f.map { it + 1 } }

    rx.Observable.just(1).compose(ft.toV1Transformer()).test().assertResult(2)
  }

  @Test
  fun st2st1() {
    val st = SingleTransformer<Int, Int> { s -> s.map { it + 1 } }

    rx.Single.just(1).compose(st.toV1Transformer()).test().assertResult(2)
  }

  @Test
  fun ct2ct1() {
    var i = 0
    val ct = CompletableTransformer { it.doOnComplete { i++ } }

    rx.Completable.complete().compose(ct.toV1Transformer()).test().assertResult()
    i.should be 1
  }

  @Test
  fun op1op2() {
    val op = rx.Observable.Operator<Int, Int> { o ->
      object : rx.Subscriber<Int>(o) {
        override fun onNext(t: Int) {
          o.onNext(t + 1)
        }

        override fun onError(e: Throwable?) {
          o.onError(e)
        }

        override fun onCompleted() {
          o.onCompleted()
        }
      }
    }

    Flowable.just(1).lift(op.toV2Operator()).test().assertResult(2)
  }

  @Test
  fun fo2o1() {
    val op = FlowableOperator<Int, Int> { o ->
      object : Subscriber<Int> {
        override fun onError(t: Throwable?) {
          o.onError(t)
        }

        override fun onSubscribe(s: Subscription?) {
          o.onSubscribe(s)
        }

        override fun onNext(t: Int) {
          o.onNext(t + 1)
        }

        override fun onComplete() {
          o.onComplete()
        }
      }
    }

    rx.Observable.just(1).lift(op.toV1Operator()).test().assertResult(2)
  }

}