package com.meelogic.raczkowbar.rx2v2.operators

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class FilteringShowcase {

    @Test
    fun `use debounce to get only latest item over a period of time`() {
        val publisher = PublishSubject.create<Int>()

        publisher
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe {
                log("Got: $it")
            }

        publisher.onNext(1)
        publisher.onNext(2)
        publisher.onNext(3)

        Thread.sleep(1000)

        publisher.onNext(4)

        Thread.sleep(1000)
    }

    @Test
    fun `use throttleFirst to get only first item over a period of time`() {
        val publisher = PublishSubject.create<Int>()

        publisher
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                log("Got: $it")
            }

        publisher.onNext(1)
        publisher.onNext(2)
        publisher.onNext(3)

        Thread.sleep(1000)

        publisher.onNext(4)

        Thread.sleep(1000)
    }
}