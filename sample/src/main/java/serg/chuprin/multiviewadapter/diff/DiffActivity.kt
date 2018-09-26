package serg.chuprin.multiviewadapter.diff

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_diff.*
import serg.chuprin.adapter.DiffMultiViewAdapter
import serg.chuprin.multiviewadapter.R
import java.util.*
import java.util.concurrent.TimeUnit

class DiffActivity : AppCompatActivity() {

    private val list = (0..10).map(::createListItem)

    private val compositeStop = CompositeDisposable()
    private val diffAdapter = DiffMultiViewAdapter(Callback())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diff)

        diffAdapter.registerRenderer(DiffUserRenderer())
        diffAdapter.registerRenderer(DiffGroupRenderer())
        diffAdapter.registerRenderer(DiffProgressRenderer())

        recyclerView.adapter = diffAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addBtn.setOnClickListener {
            val items = diffAdapter.items.toMutableList()
            val randomPos = Random().nextInt(items.size)

            diffAdapter.setItems(items.apply { add(randomPos, createListItem(randomPos)) })
        }

        removeBtn.setOnClickListener {
            val items = diffAdapter.items.toMutableList()
            val randomPos = Random().nextInt(items.size)

            diffAdapter.setItems(items.apply { removeAt(randomPos) })
        }

        changeBtn.setOnClickListener {
            val items = diffAdapter.items.toMutableList()
            val randomPos = Random().nextInt(items.size)

            diffAdapter.setItems(items.apply { set(randomPos, createListItem(randomPos)) })
        }
    }

    override fun onStart() {
        super.onStart()

        compositeStop
            .add(Single
                .just(true)
                .doOnSubscribe { diffAdapter.setItems(listOf(DiffItem.Progress)) }
                .delay(700, TimeUnit.MILLISECONDS)
                .map { list }
                .subscribe(diffAdapter::setItems, Throwable::printStackTrace)
            )
    }

    override fun onStop() {
        super.onStop()
        compositeStop.clear()
    }

    private fun createListItem(pos: Int): DiffItem {
        val random = UUID.randomUUID().toString()
        return when {
            pos % 3 == 0 -> DiffItem.Group("Group: $pos (${random.take(2)})")
            else -> DiffItem.User(pos, "$pos: ${random.take(5)}")
        }
    }

    private class Callback : DiffUtil.ItemCallback<DiffItem>() {

        override fun areContentsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
            if (oldItem is DiffItem.Group && newItem is DiffItem.Group) return true
            if (oldItem is DiffItem.Progress && newItem is DiffItem.Progress) return true
            if (oldItem is DiffItem.User && newItem is DiffItem.User) return oldItem == newItem
            return false
        }

        override fun areItemsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
            if (oldItem is DiffItem.Progress && newItem is DiffItem.Progress) {
                return true
            }
            if (oldItem is DiffItem.User && newItem is DiffItem.User) {
                return oldItem.id == newItem.id
            }
            if (oldItem is DiffItem.Group && newItem is DiffItem.Group) {
                return oldItem == newItem
            }
            return false
        }
    }

}