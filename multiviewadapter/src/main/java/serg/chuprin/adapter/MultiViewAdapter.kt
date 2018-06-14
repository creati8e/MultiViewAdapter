package serg.chuprin.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass


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

    var items: MutableList<Any> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var clickListener: ((Any, View, Int) -> Unit)? = null
    var longClickListener: ((Any, View, Int) -> Unit)? = null

    val renderers = SparseArray<ViewRenderer<Any, ViewHolder>>()
    val rendererTypes = mutableMapOf<KClass<*>, ViewRenderer<Any, ViewHolder>>()

    private val handler = Handler()

    inline fun <reified R : Any> registerRenderer(renderer: ViewRenderer<R, *>) {
        if (renderers.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        renderers.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        rendererTypes[R::class] = renderer
    }

    fun <R : Any> registerRenderer(renderer: ViewRenderer<R, *>, typeKlass: KClass<R>) {
        if (renderers.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        renderers.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        rendererTypes[typeKlass] = renderer
    }

    fun removeRenderer(typeKlass: KClass<*>): Boolean {
        val removedRenderer = rendererTypes.remove(typeKlass) ?: return false
        val index = renderers.indexOfValue(removedRenderer).takeIf { it != -1 } ?: return false

        renderers.removeAt(index)
        return true
    }

    fun removeRenderer(renderer: ViewRenderer<R, *>): Boolean {
        var type: KClass<*>? = null

        for ((registeredType, registeredRenderer) in rendererTypes) {
            if (registeredRenderer.type == renderer.type) {
                type = registeredType
                break
            }
        }
        if (type != null) rendererTypes.remove(type)
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

    open fun removeLast(itemClass: Class<*>) {
        if (items.isNotEmpty() && items.last().javaClass == itemClass) {
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
            "No renderer registered for type: ${kClass.simpleName}"
        )
        return viewRenderer to item
    }

    private fun notifyInserted(count: Int) {
        handler.post { notifyItemRangeInserted(itemCount, itemCount + count) }
    }
}
