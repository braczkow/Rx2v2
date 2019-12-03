package com.meelogic.raczkowbar.rx2v2.operators

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import kotlin.random.Random

class MappingShowcase {

    @Test
    fun `apply map to transform each item`() {

        Observable.fromIterable(listOf(1, 2, 3, 4))
            .map {
                it * it
            }
            .subscribe {
                log("$it")
            }
    }

    private fun backendSquare(i: Int): Observable<Int> = Observable.fromCallable {
        log("backendSquare")
        i * i
    }

    private fun sayItTwice(i: Int) = Observable.fromArray(i, i)


    @Test
    fun `apply flatMap to execute other Observable for each item`() {
        Observable.fromIterable(listOf(1, 2, 3))
            .map {
                it + 2
            }
            .flatMap {
                backendSquare(it)
            }
            .subscribe {
                log("$it")
            }

    }

    private fun wordsSplitter(s: String) = Observable.fromIterable(s.split(' '))

    @Test
    fun `flatMap can result in 1toN transformation`() {
        Observable.just("Split this into words")
            .flatMap {
                wordsSplitter(it)
            }
            .subscribe {
                log("next word: $it")
            }
    }

    @Test
    fun `flatMapIterable for a map-reduce`() {

        Observable.just(listOf(1, 2, 3, 4, 5, 6))
            .flatMapIterable { list ->
                list.filter { it % 2 == 0 }
            }
            .flatMap {
                backendSquare(it).subscribeOn(Schedulers.io())
            }
            .reduce { i0, i1 ->
                i0 + i1
            }
            .observeOn(Schedulers.single())
            .subscribe {
                log("sum of squares of evens is: $it")
            }

        Thread.sleep(100)
    }

    class ThirdPartyLib {
        sealed class VeryCustomError : Throwable() {
            class OneStrangeError : VeryCustomError()
            class AnotherStrangeError : VeryCustomError()
        }

        fun doRiskyStuff() = Completable
            .create { emitter ->

                when (Random(System.currentTimeMillis()).nextInt(3)) {
                    0 -> {
                        emitter.onComplete()
                    }
                    1 -> emitter.onError(VeryCustomError.OneStrangeError())
                    2 -> emitter.onError(VeryCustomError.AnotherStrangeError())
                }

            }
    }

    class FriendlyError : Throwable()

    @Test
    fun `onErrorResumeNext to map errors`() {
        ThirdPartyLib()
            .doRiskyStuff()
            .onErrorResumeNext {
                if (it is ThirdPartyLib.VeryCustomError) {
                    return@onErrorResumeNext Completable.error(FriendlyError())
                } else {
                    return@onErrorResumeNext Completable.error(it)
                }
            }
            .subscribe({
                log("this one is boring")
            }, {
                log("error: $it")
            })
    }

}