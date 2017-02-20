package com.lvla.rxjava.interopkt

import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.FlowableOperator
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.subjects.Subject
import org.reactivestreams.Publisher

fun <T> rx.Observable<T>.toV2Flowable() = RxJavaInterop.toV2Flowable(this)!!
fun <T> rx.Observable<T>.toV2Observable() = RxJavaInterop.toV2Observable(this)!!
fun <T> rx.Single<T>.toV2Single() = RxJavaInterop.toV2Single(this)!!
fun rx.Completable.toV2Completable() = RxJavaInterop.toV2Completable(this)!!
fun <T> rx.Single<T>.toV2Maybe() = RxJavaInterop.toV2Maybe(this)!!
fun <T> rx.Completable.toV2Maybe() = RxJavaInterop.toV2Maybe<T>(this)!!

fun <T> Publisher<T>.toV1Observable() = RxJavaInterop.toV1Observable(this)
fun <T> Observable<T>.toV1Observable(strategy: BackpressureStrategy) = RxJavaInterop.toV1Observable(this, strategy)!!
fun <T> Single<T>.toV1Single() = RxJavaInterop.toV1Single(this)!!
fun Completable.toV1Completable() = RxJavaInterop.toV1Completable(this)!!
fun <T> Maybe<T>.toV1Single() = RxJavaInterop.toV1Single(this)!!
fun <T> Maybe<T>.toCompletable() = RxJavaInterop.toV1Completable(this)!!


fun <T> rx.subjects.Subject<T, T>.toV2Subject() = RxJavaInterop.toV2Subject(this)!!
fun <T> rx.subjects.Subject<T, T>.toV2Provessor() = RxJavaInterop.toV2Processor(this)!!

fun <T> Subject<T>.toV1Subject() = RxJavaInterop.toV1Subject(this)!!
fun <T> FlowableProcessor<T>.toV1Subject() = RxJavaInterop.toV1Subject(this)!!


fun <T, R> rx.Observable.Transformer<T, R>.toV2Transformer() = RxJavaInterop.toV2Transformer(this)!!
fun <T, R> rx.Single.Transformer<T, R>.toV2Transformer() = RxJavaInterop.toV2Transformer(this)!!
fun rx.Completable.Transformer.toV2Transformer() = RxJavaInterop.toV2Transformer(this)!!

fun <T, R> FlowableTransformer<T, R>.toV1Transformer() = RxJavaInterop.toV1Transformer(this)!!
fun <T, R> SingleTransformer<T, R>.toV1Transformer() = RxJavaInterop.toV1Transformer(this)!!
fun CompletableTransformer.toV1Transformer() = RxJavaInterop.toV1Transformer(this)!!


fun <T, R> rx.Observable.Operator<R, T>.toV2Operator() = RxJavaInterop.toV2Operator(this)!!
fun <T, R> FlowableOperator<R, T>.toV1Operator() = RxJavaInterop.toV1Operator(this)!!