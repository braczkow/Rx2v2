package com.meelogic.raczkowbar.rx2v2.operators

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class TimersShowcase {

    @Test
    fun `use timer for single-shots`() {
        Observable.timer(1, TimeUnit.SECONDS)
            .subscribe {
                //defaults to computation Scheduler
                log("Got it")
            }

        log("Going to sleep")
        Thread.sleep(2000)
    }

    @Test
    fun `use timer for single-shots, wait on specific scheduler`() {
        Observable.timer(1, TimeUnit.SECONDS, Schedulers.single())
            .subscribe {
                //if specified
                log("Got it")
            }

        log("Going to sleep")
        Thread.sleep(2000)
    }

    @Test
    fun `be careful with schedulers blocking`() {
        Observable.timer(1, TimeUnit.SECONDS, Schedulers.trampoline())
            .subscribe {
                log("Got it")
            }

        log("Going to sleep")
        Thread.sleep(2000)
    }

    @Test
    fun `be careful with schedulers blocking, nontrampline`() {
        Completable.fromAction {
            Observable.timer(200, TimeUnit.MILLISECONDS, Schedulers.single())
                .subscribe {
                    log("Got it: ${System.currentTimeMillis()}")
                }

            log("Going to sleep2 ${System.currentTimeMillis()}")
            Thread.sleep(1000)

        }
            .subscribeOn(Schedulers.single())
            .subscribe {
                log("done ${System.currentTimeMillis()}")
            }



        log("Going to sleep ${System.currentTimeMillis()}")
        Thread.sleep(2000)
    }

    @Test
    fun `use interval for recurring tasks`() {
        val disposable = Observable.interval(100, TimeUnit.MILLISECONDS)
            .subscribe {
                log("Again!")
            }

        log("Going to sleep")
        Thread.sleep(1000)

        log("Disposing")
        disposable.dispose()
        log("Disposed")

        log("Going to sleep 2")
        Thread.sleep(1000)

    }


}