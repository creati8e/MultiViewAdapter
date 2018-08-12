package serg.chuprin.adapter

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@Suppress("UNCHECKED_CAST")
abstract class ViewRenderer<in M : Any, VH : ViewHolder> {

    /** Unique identifier that represents [LayoutRes] and ViewType at the same time **/
    abstract val type: Int

    open fun createViewHolder(
        parent: ViewGroup,
        clickListener: Click?,
        longClickListener: LongClick?
    ): VH = createViewHolder(parent).also { onVhCreated(it, clickListener, longClickListener) }

    open fun bindView(holder: VH, model: M) = Unit

    open fun bindView(holder: VH, model: M, payloads: MutableList<Any>) = Unit

    protected open fun onVhCreated(
        holder: VH,
        clickListener: Click?,
        longClickListener: LongClick?
    ) = Unit

    protected open fun inflate(@LayoutRes layout: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }

    protected open fun createViewHolder(parent: ViewGroup): VH {
        return ViewHolder(inflate(type, parent)) as VH
    }
}
