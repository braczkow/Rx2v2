package com.meelogic.raczkowbar.rx2v2.creators

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.lang.RuntimeException

class CreatorsShowcase {

    @Test
    fun `simple creators`() {

        //simplest
        Observable.just("World!")
            .subscribe {
                log("Hello, $it")
            }

        //iterable
        Observable.fromIterable(listOf("One", "Two", "Three"))
            .subscribe {
                log("Elements: $it")
            }

        //action a.k.a. callable

        Observable.fromCallable {
            "One"
        }
            .subscribe {
                log("Callable: $it")
            }

        //empty?
        Observable.empty<String>()
            .subscribe({
                log("Empty: $it")
            }, {

            }, {
                log("Empty, completed")
            })

    }

    @Test
    fun `create from scratch`() {
        Observable.create<String> { emitter ->
            //do whatever you want
            emitter.onNext("One")
            emitter.onNext("Two")
            emitter.onComplete()
            emitter.onNext("Tree")
        }
            .subscribe {
                log("next: $it")
            }
    }

    @Test
    fun `when create from scratch, you can subscribe to some other observable`() {
        Observable.create<String> { emitter ->
            //do whatever you want
            emitter.onNext("One")

            Completable.fromAction {
                log("some other stuff")
            }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    emitter.onNext("Three")
                }

            emitter.onNext("Two")
        }
            .subscribe({
                log("next: $it")
            }, {
                log("Error: $it")
            })

        Thread.sleep(100)
    }

    @Test
    fun `when create from scratch, you can subscribe to some other observable, propagate errors`() {
        Observable.create<String> { emitter ->
            //do whatever you want
            emitter.onNext("One")

            Completable.fromAction {
                log("some other stuff")
                throw Throwable("blast")
            }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    emitter.onNext("Three")
                }, {
                    emitter.onError(it)
                })

            emitter.onNext("Two")
        }
            .subscribe({
                log("next: $it")
            }, {
                log("Error: $it")
            })

        Thread.sleep(100)
    }

    @Test
    fun `when create from scratch, you can subscribe to some other observable, propagate errors, but be careful`() {
        Observable.create<String> { emitter ->
            //do whatever you want
            emitter.onNext("One")

            Completable.fromAction {
                log("some other stuff")
                throw Throwable("blast 2")
            }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    emitter.onNext("Three")
                }, {
                    emitter.onError(it)
                })

            emitter.onNext("Two")
            throw Throwable("blast 1")
        }
            .subscribe({
                log("next: $it")
            }, {
                log("Error: $it")
            })

        Thread.sleep(100)
    }

    @Test
    fun `when create from scratch, inline throws are catched`() {
        Observable.create<String> { emitter ->
            //do whatever you want
            emitter.onNext("One")

            emitter.onNext("Two")
            throw Throwable("blast 1")
        }
            .subscribe({
                log("next: $it")
            }, {
                log("Error: $it")
            })

        Thread.sleep(100)
    }
}