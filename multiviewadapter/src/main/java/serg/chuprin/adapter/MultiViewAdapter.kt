package serg.chuprin.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass


@Suppress("unused", "MemberVisibilityCanPrivate", "UNCHECKED_CAST", "PropertyName")
open class MultiViewAdapter(

        val items: MutableList<Any> = mutableListOf()

) : RecyclerView.Adapter<ViewHolder>(),
        ViewHolder.ClickCallback,
        ViewHolder.LongClickCallback {

    var clickListener: ((Any, View, Int) -> Unit)? = null
    var longClickListener: ((Any, View, Int) -> Unit)? = null

    protected val renderers = SparseArray<ViewRenderer<Any, ViewHolder>>()
    protected val rendererTypes = mutableMapOf<KClass<*>, ViewRenderer<Any, ViewHolder>>()
    private val handler = Handler()

    @PublishedApi
    internal val `access$renderers`: SparseArray<ViewRenderer<Any, ViewHolder>>
        get() = renderers

    @PublishedApi
    internal val `access$rendererTypes`: MutableMap<KClass<*>, ViewRenderer<Any, ViewHolder>>
        get() = rendererTypes

    inline fun <reified R : Any> registerRenderer(renderer: ViewRenderer<R, *>) {
        if (`access$renderers`.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        `access$renderers`.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        `access$rendererTypes`.put(R::class, renderer)
    }

    //region RecyclerView impl

    override fun onCreateViewHolder(

            parent: ViewGroup,
            viewType: Int

    ): ViewHolder = renderers
            .get(viewType)
            .createViewHolder(
                    parent = parent,
                    clickListener = this,
                    longClickListener = this
            )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (viewRenderer, model) = getRendererForPosition(position)
        viewRenderer.bindView(holder, model)
    }

    override fun getItemViewType(position: Int): Int = getRendererForPosition(position).first.type

    override fun getItemCount(): Int = items.size

    //endregion

    //region data management

    open fun getItem(position: Int): Any = items[position]

    open fun setItems(newItems: List<Any>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    open fun addItems(newItems: List<Any>) {
        items.addAll(newItems)
        notifyInserted(newItems.size)
    }

    open fun addItem(model: Any) {
        items.add(itemCount, model)
        notifyInserted(1)
    }

    open fun addItemIfNotPresent(model: Any) {
        if (items.isEmpty() || model.javaClass != items[items.lastIndex].javaClass) {
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
        if (items.isNotEmpty() && items[items.lastIndex].javaClass == itemClass) {
            removeItemAt(items.lastIndex)
        }
    }

    //endregion

    override fun onClick(view: View, position: Int) {
        clickListener?.invoke(getItem(position), view, position)
    }

    override fun onLongClick(view: View, position: Int) {
        longClickListener?.invoke(getItem(position), view, position)
    }

    protected open fun getRendererForPosition(
            position: Int
    ): Pair<ViewRenderer<Any, ViewHolder>, Any> {

        val item = getItem(position)
        val kClass = item::class
        val viewRenderer = rendererTypes[kClass] ?: throw IllegalStateException(
                "No renderer registered for type: ${kClass.simpleName}")
        return viewRenderer to item
    }

    private fun notifyInserted(count: Int) = itemCount.run {
        handler.post { notifyItemRangeInserted(this, this + count) }
    }
}
