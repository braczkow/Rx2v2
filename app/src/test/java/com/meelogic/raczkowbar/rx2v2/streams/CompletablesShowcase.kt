package com.meelogic.raczkowbar.rx2v2.streams

import com.meelogic.raczkowbar.rx2v2.log
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class CompletablesShowcase {

    @Test
    fun `basic Completable operations`() {

        Completable.fromAction {
            log("Yey, doing stuff!")
        }
            .subscribe {
                log("done working")
            }

        Completable.create { emitter ->
            log("Yey, doing stuff!")
            emitter.onComplete()

            //this will crash
            //emitter.onError(Throwable("blast"))
        }
            .subscribe({
                log("done")
            }, {
                log("error: $it")
            })

        Completable.complete()
            .subscribe {
                log("not that useful?")
            }

        Completable.error(Throwable("blast"))
            .subscribe({}, {
                log("not that useful part2?")
            })
    }

    fun completableOne() = Completable.fromAction { log("completableOne") }
    fun completableTwo() = Completable.fromAction { log("completableTwo") }

    @Test
    fun `andThen executes Completables sequentially`() {
        completableOne()
            .andThen(completableTwo())
            .subscribe {
                log("done")
            }

        log("starting 2nd call")

        //this does not work!
        completableOne()
            .andThen {
                completableTwo()
            }
            .subscribe {
                log("done 2")
            }

        log("starting 3rd call")

        //this would, but makes no sense
        completableOne()
            .andThen {
                completableTwo()
                it.onComplete()
            }
            .subscribe {
                log("done 3")
            }

        //how it's done
        completableOne()
            .andThen(Completable.defer {
                log("must log!")
                completableTwo()
            })
            .subscribe {
                log("done 4")
            }
    }

    @Test
    fun `basic Single operations`() {
        Single.just("One, no more")
            .subscribe { item, error ->
                log("item is: $item error is: $error")
            }

        Single.error<String>(Throwable("blast"))
            .subscribe { item, error ->
                log("item is: $item error is: $error")
            }

        //more traditional
        Single.just("One, no more")
            .subscribe({
                log("success: $it")
            }, {
                log("error: $it")
            })
    }

}