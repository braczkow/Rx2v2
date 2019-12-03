package com.meelogic.raczkowbar.rx2v2.operators

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class MergingShowcase {

    @Test
    fun `combineLatest always emits latest from all sources`() {
        val intPub = PublishSubject.create<Int>()
        val stringPub = PublishSubject.create<String>()

        //this is from rxkotlin extenssions
        Observables
            .combineLatest(
                intPub,
                stringPub
            )
            .subscribe {
                log("Got pair: $it")
            }

        intPub.onNext(1)
        stringPub.onNext("aaa")
        stringPub.onNext("bbb")
        intPub.onNext(2)

    }
}