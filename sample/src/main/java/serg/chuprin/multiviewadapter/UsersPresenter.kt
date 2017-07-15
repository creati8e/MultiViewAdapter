package serg.chuprin.multiviewadapter

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import serg.chuprin.multiviewadapter.model.domain.ScrollEvent
import serg.chuprin.multiviewadapter.model.domain.UsersPaginator
import serg.chuprin.multiviewadapter.view.UsersView

class UsersPresenter(val paginator: UsersPaginator) {

    var view: UsersView? = null

    private var paginationDisposable: Disposable? = null

    fun paginate(scrollEvents: Observable<ScrollEvent>) {
        paginationDisposable?.dispose()
        paginationDisposable = paginator.paginate(scrollEvents, { view?.paginationProgress(true) })
                .doOnSubscribe { view?.showNetworkError(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { view?.paginationProgress(false) }
                .subscribe({ view?.addData(it) }, {
                    view?.paginationProgress(false)
                    view?.showNetworkError(true)
                })
    }

    fun attachView(view: UsersView) {
        this.view = view
    }

    fun detachView() {
        view = null
    }
}