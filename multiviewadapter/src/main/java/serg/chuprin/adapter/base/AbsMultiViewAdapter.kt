package serg.chuprin.adapter.base

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import serg.chuprin.adapter.R
import serg.chuprin.adapter.RendererDelegate
import serg.chuprin.adapter.ViewHolder
import serg.chuprin.adapter.ViewRenderer

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class AbsMultiViewAdapter<T : Any> : RecyclerView.Adapter<ViewHolder>(),
    ViewHolder.ClickCallback,
    ViewHolder.LongClickCallback {

    var clickListener: ((T, View, Int) -> Unit)? = null
    var longClickListener: ((T, View, Int) -> Unit)? = null

    val rendererDelegate = RendererDelegate<T>()

    inline fun <reified R : T> registerRenderer(renderer: ViewRenderer<R, *>) {
        rendererDelegate.registerRenderer(renderer)
    }

    fun <R : T> registerRenderer(renderer: ViewRenderer<R, *>, clazz: Class<R>) {
        rendererDelegate.registerRenderer(renderer, clazz)
    }

    fun removeRenderer(clazz: Class<*>): Boolean = rendererDelegate.removeRenderer(clazz)

    fun removeRenderer(renderer: ViewRenderer<R, *>): Boolean {
        return rendererDelegate.removeRenderer(renderer)
    }

    //region RecyclerView impl

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return rendererDelegate
            .getRendererForViewType(viewType)
            .createViewHolder(
                parent = parent,
                clickListener = this,
                longClickListener = this
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val (viewRenderer, model) = getRendererForPosition(position)

        if (payloads.isNotEmpty()) {
            viewRenderer.bindView(holder, model, payloads)
        } else {
            viewRenderer.bindView(holder, model)
        }
    }

    //unused;
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = Unit

    override fun getItemViewType(position: Int): Int {
        return getRendererForPosition(position).first.type
    }

    //endregion

    override fun onClick(view: View, position: Int) {
        if (!isValidClick(position)) return
        clickListener?.invoke(getItem(position), view, position)
    }

    override fun onLongClick(view: View, position: Int) {
        if (!isValidClick(position)) return
        longClickListener?.invoke(getItem(position), view, position)
    }

    protected open fun getRendererForPosition(position: Int): Pair<ViewRenderer<T, ViewHolder>, T> {
        val item = getItem(position)
        return rendererDelegate.getRendererForClass(item::class.java) to item
    }

    abstract fun getItem(position: Int): T

    private fun isValidClick(position: Int): Boolean {
        return when {
            position == RecyclerView.NO_POSITION -> false
            position >= itemCount -> false
            else -> true
        }
    }
}