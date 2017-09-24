package serg.chuprin.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup


@Suppress("unused", "WeakerAccess")
open class MultiViewAdapter(val items: MutableList<Any> = mutableListOf())
    : RecyclerView.Adapter<ViewHolder>(),
        ViewHolder.ClickCallback,
        ViewHolder.LongClickCallback {

    protected val renderers = SparseArray<ViewRenderer<Any, ViewHolder>>()
    private val handler = Handler()
    var clickListener: ((Any, View, Int) -> Unit)? = null
    var longClickListener: ((Any, View, Int) -> Unit)? = null

    //region RecyclerView impl

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            renderers.get(viewType).createViewHolder(parent, this, this)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (viewRenderer, model) = getRendererForPosition(position)
        viewRenderer.bindView(holder, model)
    }

    override fun getItemViewType(position: Int): Int = getRendererForPosition(position).first.type()

    override fun getItemCount(): Int = items.size

    //endregion

    //region data management

    fun getItem(position: Int): Any = items[position]

    fun setItems(newItems: List<Any>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Any>) {
        items.addAll(newItems)
        notifyInserted(newItems.size)
    }

    fun addItem(model: Any) {
        items.add(itemCount, model)
        notifyInserted(1)
    }

    fun addItemIfNotPresent(model: Any) {
        if (items.isEmpty() || model.javaClass != items[items.lastIndex].javaClass) {
            addItem(model)
        }
    }

    fun removeItemAt(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeLast(itemClass: Class<*>) {
        if (items.isNotEmpty() && items[items.lastIndex].javaClass == itemClass) {
            removeItemAt(items.lastIndex)
        }
    }

    //endregion

    @Suppress("UNCHECKED_CAST")
    fun registerRenderer(renderer: ViewRenderer<*, *>) {
        if (renderers.get(renderer.type()) == null) {
            renderers.put(renderer.type(), renderer as ViewRenderer<Any, ViewHolder>)
        } else {
            throw RuntimeException("ViewRenderer already exist with this type: " + renderer.type())
        }
    }

    private fun notifyInserted(count: Int) = itemCount.run {
        handler.post { notifyItemRangeInserted(this, this + count) }
    }

    private fun getRendererForPosition(position: Int) = getItem(position).run {
        (0 until renderers.size())
                .asSequence()
                .map { renderers[renderers.keyAt(it)] }
                .filter { it.isViewForType(this) }
                .first() to this
    }

    override fun onClick(view: View, position: Int) {
        clickListener?.invoke(getItem(position), view, position)
    }

    override fun onLongClick(view: View, position: Int) {
        longClickListener?.invoke(getItem(position), view, position)
    }

}
