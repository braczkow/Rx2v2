package com.meelogic.raczkowbar.rx2v2.subscribers

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Test

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

    }

    @Test
    fun `basic subscription with DisposableObserver`() {
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
    }

    @Test
    fun `basic subscription with Consumer`() {
        val publisher = PublishSubject.create<String>()

        publisher
            .subscribe {
                log("onNext: $it")
            }


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onComplete()
    }


}