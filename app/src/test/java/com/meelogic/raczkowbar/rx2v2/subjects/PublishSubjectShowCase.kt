package com.meelogic.raczkowbar.rx2v2.subjects

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.subjects.PublishSubject
import org.junit.Test


class PublishSubjectShowCase {
    @Test
    fun `what is PublishSubject`() {
        val publishSubject = PublishSubject.create<String>()

        publishSubject
            .subscribe {
                log("Got: $it")
            }

        publishSubject.onNext("Hi")
        publishSubject.onNext("guys!")
        //spoiler: it's an Observer pattern implementation
    }

    @Test
    fun `Does it store the value`() {
        val publishSubject = PublishSubject.create<String>()

        publishSubject.onNext("Hi")

        publishSubject
            .subscribe {
                log("Got: $it")
            }

        publishSubject.onNext("guys!")
        //not really
    }

    @Test
    fun `Does it handle multiple Subscribers`() {
        val publishSubject = PublishSubject.create<String>()
        publishSubject
            .subscribe {
                log("First got: $it")
            }

        publishSubject
            .subscribe {
                log("Second got: $it")
            }

        publishSubject.onNext("This could be events")
        publishSubject.onNext("for everyone")
        //yep it does!
    }

    @Test
    fun `you can publish errors`() {
        val publishSubject = PublishSubject.create<String>()

        publishSubject
            .subscribe ({
                log("Got: $it")
            }, {
                log("error: $it")
            }, {
                log("completed")
            })

        publishSubject.onNext("Hi")
        publishSubject.onError(Throwable("blast"))

    }

    @Test
    fun `you can publish completion`() {
        val publishSubject = PublishSubject.create<String>()

        publishSubject
            .subscribe ({
                log("Got: $it")
            }, {
                log("error: $it")
            }, {
                log("completed")
            })

        publishSubject.onNext("Hi")
        publishSubject.onComplete()

    }


}
