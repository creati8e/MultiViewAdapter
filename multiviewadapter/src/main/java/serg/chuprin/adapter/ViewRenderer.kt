package serg.chuprin.adapter

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@Suppress("UNCHECKED_CAST")
abstract class ViewRenderer<in M : Any, VH : ViewHolder> {

    abstract val type: Int

    open fun bindView(holder: VH, model: M) = Unit

    open fun createViewHolder(parent: ViewGroup,
                              clickListener: Click?,
                              longClickListener: LongClick?): VH {
        return (ViewHolder(inflate(type, parent)) as VH)
                .also { onVhCreated(it, clickListener, longClickListener) }
    }

    protected open fun onVhCreated(holder: VH, clickListener: Click?,
                                   longClickListener: LongClick?) = Unit


    private fun inflate(@LayoutRes layout: Int, parent: ViewGroup): View =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)

    @Deprecated("Unused since v1.1.0", ReplaceWith("true"))
    open fun isViewForType(model: Any): Boolean = true

}
