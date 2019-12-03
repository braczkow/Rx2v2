package com.meelogic.raczkowbar.rx2v2.schedulers

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.util.concurrent.Executors

class SchedulersShowcase {
    @Test
    fun `you can control the execution of the root observable by subscribeOn`() {
        log("Starting")
        Completable.fromAction {
            log("Doing stuff")
        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                log("done working")
            }

        Thread.sleep(100)
    }

    @Test
    fun `you can control the observer thread by observeOn`() {
        log("starting")
        Observable.create<Unit> { emitter ->
            log("Create Unit!")
            emitter.onNext(Unit)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe {
                log("Got Unit!")
            }

        Thread.sleep(100)
    }

    @Test
    fun `observeOn affects all operations below`() {
        Observable.create<Int> { emitter ->
            log("Create Unit!")
            emitter.onNext(1)
        }
            .subscribeOn(Schedulers.io())
            .map {
                log("transforming A $it")
                it + 1
            }
            .observeOn(Schedulers.computation())
            .map {
                log("transforming B $it")
                it + 2
            }
            .subscribe {
                log("Got $it!")
            }

        Thread.sleep(100)
    }

    @Test
    fun `subscribeOn has no effect on Subjects`() {
        log("Starting thread")
        val publishSubject = PublishSubject.create<Int>()

        publishSubject
            .subscribeOn(Schedulers.io())
            .subscribe {
                log("Got $it")
            }

        //subscribeOn adds a little delay for the subscription
        Thread.sleep(100)

        publishSubject.onNext(13)

        Thread.sleep(100)
    }
}


