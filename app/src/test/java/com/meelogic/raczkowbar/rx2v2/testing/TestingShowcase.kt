package com.meelogic.raczkowbar.rx2v2.testing

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class TestingShowcase {

    @Test
    fun `each stream provides test method that returns TestObserver`() {
        val testObserver: TestObserver<Int> = Observable.fromIterable(
            listOf(1, 2, 3)
        ).test()

        testObserver
            .assertValueCount(3)
            .assertComplete()
            .assertNoErrors()

        testObserver.assertValueAt(1) {
            it % 2 == 0
        }
    }

    @Test
    fun `use TestScheduler to make a timemachine`() {
        val scheduler = TestScheduler()

        Observable.timer(10, TimeUnit.MINUTES, scheduler)
            .subscribe {
                log("Ten minutes later!")
            }

        log("advance time by 9 minutes")
        scheduler.advanceTimeBy(9, TimeUnit.MINUTES)

        log("and 2 more")
        scheduler.advanceTimeBy(2, TimeUnit.MINUTES)

    }

    interface SchedulersFactory {
        fun main(): Scheduler
        fun io(): Scheduler
    }

    class SchedulersFactoryImpl: SchedulersFactory {
        //override fun main(): Scheduler = AndroidSchedulers.mainThread()
        override fun main(): Scheduler = Schedulers.single()

        override fun io(): Scheduler = Schedulers.io()
    }

    class TestSchedulersFactory: SchedulersFactory {
        val scheduler = TestScheduler()

        override fun main() = Schedulers.single()

        override fun io() = scheduler
    }

    @Test
    fun `production-alike example`() {
        val factory = TestSchedulersFactory()

        Observable.timer(10, TimeUnit.MINUTES, factory.io())
            .observeOn(factory.main())
            .subscribe {
                log("Ten minutes later!")
            }

        log("advance time by 9 minutes")
        factory.scheduler.advanceTimeBy(9, TimeUnit.MINUTES)

        log("and 2 more")
        factory.scheduler.advanceTimeBy(2, TimeUnit.MINUTES)

    }


}