package serg.chuprin.adapter.base

import android.os.Handler

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class BaseMultiViewAdapter<T : Any> : AbsMultiViewAdapter<T>() {

    open val items: MutableList<T> = mutableListOf()

    private val handler = Handler()

    override fun getItem(position: Int): T = items[position]

    override fun getItemCount(): Int = items.size

    open fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun addItems(newItems: List<T>) {
        items.addAll(newItems)
        notifyInserted(newItems.size)
    }

    open fun addItem(model: T) {
        items.add(itemCount, model)
        notifyInserted(1)
    }

    open fun addItemIfNotPresent(model: T) {
        if (items.isEmpty() || model.javaClass != items.last().javaClass) {
            addItem(model)
        }
    }

    open fun removeItemAt(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    open fun removeLast(itemClass: Class<*>) {
        if (items.isNotEmpty() && items.last().javaClass == itemClass) {
            removeItemAt(items.lastIndex)
        }
    }

    private fun notifyInserted(count: Int) {
        handler.post {
            val itemCount = itemCount
            notifyItemRangeInserted(itemCount, itemCount + count)
        }
    }
}