package serg.chuprin.multiviewadapter.model.domain

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

abstract class Paginator<MODEL>(
    private val limit: Int = 20,
    private val retryCount: Int = 3
) {

    fun paginate(
        observable: Observable<ScrollEvent>,
        onScrollAction: (() -> Unit)? = null
    ): Observable<List<MODEL>> {
        return internalPaginate(
            observable,
            onScrollAction = onScrollAction
        ).subscribeOn(AndroidSchedulers.mainThread())
    }

    protected abstract fun nextPage(offset: Int): Observable<List<MODEL>>

    private fun internalPaginate(
        observable: Observable<ScrollEvent>,
        attemptsCount: Int = 0,
        onScrollAction: (() -> Unit)? = null
    ): Observable<List<MODEL>> {
        return observable
                .filter { (lastVisible, itemsCount) -> lastVisible >= itemsCount - 1 - (limit / 2) }
                .map(ScrollEvent::itemsCount)
                .distinctUntilChanged()
                .doOnNext { onScrollAction?.invoke() }
                .observeOn(Schedulers.newThread())
                .switchMap(::nextPage)
                .onErrorResumeNext(
                    Function {
                        if (attemptsCount < retryCount) {
                            internalPaginate(observable, attemptsCount + 1)
                        } else {
                            Observable.error(it)
                        }
                    })
    }
}