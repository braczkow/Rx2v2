package com.meelogic.raczkowbar.rx2v2.subscribers

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.lang.RuntimeException

class DisposablesShowcase {

    @Test
    fun `lambda subscriber, simple dispose`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe({
                log("onNext: $it")
            }, {
                log("onError: $it")
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        disposable.dispose()
        publisher.onNext("Three")
    }

    //recall from SubscribersShowcase
    @Test
    fun `lambda subscriber, error after complete`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe({
                log("onNext: $it")
            }, {
                log("onError: $it")
            }, {
                log("completed")
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        publisher.onComplete()
        publisher.onError(RuntimeException("blast"))
    }

    @Test
    fun `lambda subscriber, error after dispose`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe({
                log("onNext: $it")
            }, {
                log("onError: $it")
            })


        publisher.onNext("One")
        publisher.onNext("Two")
        disposable.dispose()
        publisher.onError(RuntimeException("blast"))
    }

    @Test
    fun `disposing `() {

    }

}