package serg.chuprin.adapter

import android.view.ViewGroup

abstract class ContainerRenderer<in M : Any> : ViewRenderer<M, ContainerHolder>() {

    override fun createViewHolder(
        parent: ViewGroup, clickListener: Click?,
        longClickListener: LongClick?
    ): ContainerHolder {
        return ContainerHolder(inflate(type, parent))
                .also { holder -> onVhCreated(holder, clickListener, longClickListener) }
    }
}