package com.meelogic.raczkowbar.rx2v2.subscribers

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.lang.RuntimeException

class SubscribersShowcase {

    @Test
    fun `basic subscription with Observer`() {
        val publisher = PublishSubject.create<String>()

        publisher
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    log("onSubscribe")
                }

                override fun onNext(t: String) {
                    log("onNext: ${t}")
                }

                override fun onError(e: Throwable) {
                    log("onError: ${e}")
                }

                override fun onComplete() {
                    log("onComplete")
                }
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onComplete()
        publisher.onNext("Tree")

    }

    @Test
    fun `subscribe and dispose with DisposableObserver`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onNext(t: String) {
                    log("onNext: ${t}")
                }

                override fun onError(e: Throwable) {
                    log("onError: ${e}")
                }

                override fun onComplete() {
                    log("onComplete")
                }
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        disposable.dispose()
        publisher.onNext("Tree")
    }

    @Test
    fun `subscribe when error is emitted`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onNext(t: String) {
                    log("onNext: ${t}")
                }

                override fun onError(e: Throwable) {
                    log("onError: ${e}")
                }

                override fun onComplete() {
                    log("onComplete")
                }
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onError(RuntimeException("Catch me"))
        publisher.onNext("Tree")
    }

    @Test
    fun `lambda subscriber - risky way`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe {
                log("onNext: $it")
            }


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onError(RuntimeException("blast"))
    }

    @Test
    fun `lambda subscriber - safe way`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe ({
                log("onNext: $it")
            }, {
                log("onError: $it")
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onError(RuntimeException("blast"))
    }

    @Test
    fun `lambda subscriber, error after complete`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe ({
                log("onNext: $it")
            }, {
                log("onError: $it")
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onComplete()
        publisher.onError(RuntimeException("blast"))
    }


}