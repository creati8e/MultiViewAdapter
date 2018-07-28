package serg.chuprin.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup


@Suppress(
    "unused",
    "MemberVisibilityCanPrivate",
    "UNCHECKED_CAST",
    "PropertyName",
    "MemberVisibilityCanBePrivate", "RemoveRedundantBackticks"
)
open class MultiViewAdapter : RecyclerView.Adapter<ViewHolder>(),
    ViewHolder.ClickCallback,
    ViewHolder.LongClickCallback {

    val items = mutableListOf<Any>()

    var clickListener: ((Any, View, Int) -> Unit)? = null
    var longClickListener: ((Any, View, Int) -> Unit)? = null

    val renderers = SparseArray<ViewRenderer<Any, ViewHolder>>()
    val rendererTypes = mutableMapOf<Class<*>, ViewRenderer<Any, ViewHolder>>()

    private val handler = Handler()

    inline fun <reified R : Any> registerRenderer(renderer: ViewRenderer<R, *>) {
        if (renderers.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        renderers.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        rendererTypes[R::class.java] = renderer
    }

    fun <R : Any> registerRenderer(renderer: ViewRenderer<R, *>, clazz: Class<R>) {
        if (renderers.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        renderers.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        rendererTypes[clazz] = renderer
    }

    fun removeRenderer(clazz: Class<*>): Boolean {
        val removedRenderer = rendererTypes.remove(clazz) ?: return false
        val index = renderers.indexOfValue(removedRenderer).takeIf { it != -1 } ?: return false

        renderers.removeAt(index)
        return true
    }

    fun removeRenderer(renderer: ViewRenderer<R, *>): Boolean {
        var classType: Class<*>? = null

        for ((registeredType, registeredRenderer) in rendererTypes) {
            if (registeredRenderer.type == renderer.type) {
                classType = registeredType
                break
            }
        }
        if (classType != null) rendererTypes.remove(classType)
        val index = renderers
                .indexOfValue(renderer as ViewRenderer<Any, ViewHolder>)
                .takeIf { it != -1 }
                ?: return false

        renderers.removeAt(index)
        return true
    }

    //region RecyclerView impl

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return renderers
                .get(viewType)
                .createViewHolder(
                    parent = parent,
                    clickListener = this,
                    longClickListener = this
                )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (viewRenderer, model) = getRendererForPosition(position)
        viewRenderer.bindView(holder, model)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val (viewRenderer, model) = getRendererForPosition(position)
        viewRenderer.bindView(holder, model, payloads)
    }

    override fun getItemViewType(position: Int): Int = getRendererForPosition(position).first.type

    override fun getItemCount(): Int = items.size

    //endregion

    //region data management

    open fun getItem(position: Int): Any = items[position]

    open fun addItems(newItems: List<Any>) {
        items.addAll(newItems)
        notifyInserted(newItems.size)
    }

    open fun addItem(model: Any) {
        items.add(itemCount, model)
        notifyInserted(1)
    }

    open fun addItemIfNotPresent(model: Any) {
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

    open fun setItems(items: List<Any>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun removeLast(itemClass: Class<*>) {
        if (items.isNotEmpty() && items.last().javaClass == itemClass) {
            removeItemAt(items.lastIndex)
        }
    }

    //endregion

    override fun onClick(view: View, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        clickListener?.invoke(getItem(position), view, position)
    }

    override fun onLongClick(view: View, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        longClickListener?.invoke(getItem(position), view, position)
    }

    protected open fun getRendererForPosition(
        position: Int
    ): Pair<ViewRenderer<Any, ViewHolder>, Any> {

        val item = getItem(position)
        val clazz = item::class.java
        val viewRenderer = rendererTypes[clazz] ?: throw IllegalStateException(
            "No renderer registered for type: ${clazz.simpleName}"
        )
        return viewRenderer to item
    }

    private fun notifyInserted(count: Int) {
        handler.post { notifyItemRangeInserted(itemCount, itemCount + count) }
    }
}
