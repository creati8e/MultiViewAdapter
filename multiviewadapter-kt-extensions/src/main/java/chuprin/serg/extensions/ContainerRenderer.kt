package chuprin.serg.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import serg.chuprin.adapter.Click
import serg.chuprin.adapter.LongClick
import serg.chuprin.adapter.ViewRenderer

abstract class ContainerRenderer<in M : Any> : ViewRenderer<M, ContainerHolder>() {

    override fun createViewHolder(

            parent: ViewGroup, clickListener: Click?,
            longClickListener: LongClick?

    ): ContainerHolder = ContainerHolder(inflate(type, parent)).also {
        onVhCreated(it, clickListener, longClickListener)
    }

    private fun inflate(

            @LayoutRes layout: Int,
            parent: ViewGroup

    ): View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
}