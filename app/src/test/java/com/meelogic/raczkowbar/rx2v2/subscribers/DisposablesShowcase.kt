package com.meelogic.raczkowbar.rx2v2.subscribers

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class DisposablesShowcase {

    @Test
    fun `what is a disposable`() {
        val publisher = PublishSubject.create<String>()

        val disposable = publisher
            .subscribe {
                log("onNext: ${it}")
            }


        publisher.onNext("One")

        disposable.dispose()

        publisher.onNext("Two")
    }

}