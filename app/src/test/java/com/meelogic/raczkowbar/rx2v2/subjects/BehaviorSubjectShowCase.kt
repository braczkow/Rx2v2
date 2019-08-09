package com.meelogic.raczkowbar.rx2v2.subjects

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Test


class BehaviorSubjectShowCase {
    @Test
    fun `what is BehaviorSubject`() {
        val behaviorSubject = BehaviorSubject.create<String>()

        behaviorSubject
            .subscribe {
                log("Got: ${it}")
            }

        behaviorSubject.onNext("Hello")
        behaviorSubject.onNext("again")
        //spoiler: its basic behavior is similar as PublishSubject
    }

    @Test
    fun `does it store the value`() {
        val behaviorSubject = BehaviorSubject.create<String>()

        behaviorSubject.onNext("Hi")

        behaviorSubject
            .subscribe {
                log("Got: ${it}")
            }

        behaviorSubject.onNext("guys!")
        //Yes it does!
    }

    @Test
    fun `Does it handle multiple Subscribers`() {
        val behaviorSubject = BehaviorSubject.create<String>()

        behaviorSubject.onNext("This could be events")

        behaviorSubject
            .subscribe {
                log("First got: ${it}")
            }

        behaviorSubject
            .subscribe {
                log("Second got: ${it}")
            }


        behaviorSubject.onNext("for everyone")
    }
}
