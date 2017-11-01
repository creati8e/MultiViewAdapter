package serg.chuprin.multiviewadapter.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import serg.chuprin.multiviewadapter.R
import serg.chuprin.multiviewadapter.UsersPresenter
import serg.chuprin.multiviewadapter.model.UserEntity
import serg.chuprin.multiviewadapter.model.domain.ScrollEvent
import serg.chuprin.multiviewadapter.model.domain.UsersPaginator
import serg.chuprin.multiviewadapter.model.repository.UsersRepository
import serg.chuprin.multiviewadapter.view.adapter.*

class MainActivity : AppCompatActivity(), UsersView {

    private var adapter = PaginationAdapter()
    private var presenter = UsersPresenter(UsersPaginator(UsersRepository()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.registerRenderer(ProgressRenderer())
        adapter.registerRenderer(UserRenderer())
        adapter.registerRenderer(NetworkErrorRenderer())
        adapter.clickListener = { model, _, _ -> onAdapterClick(model) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter.attachView(this)
        paginate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun paginationProgress(visible: Boolean) = adapter.setLoadingMoreVisibility(visible)

    override fun showNetworkError(visible: Boolean) = adapter.setNetworkErrorVisibility(visible)

    override fun addData(list: List<UserEntity>) = adapter.addItems(list)

    private fun onAdapterClick(model: Any) {
        if (model is NetworkErrorModel) paginate()
    }

    private fun paginate() = presenter.paginate(recyclerView.paginate())
}

fun RecyclerView.paginate(): Observable<ScrollEvent> = RxRecyclerView.scrollEvents(this)
        .startWith(RecyclerViewScrollEvent.create(this, 0, 0))
        .distinctUntilChanged({ t1, t2 -> t1.dy() == t2.dy() })
        .map {
            val pos = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            ScrollEvent(pos, adapter.itemCount)
        }
