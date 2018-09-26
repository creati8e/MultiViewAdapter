package serg.chuprin.adapter

import android.view.ViewGroup

abstract class ContainerRenderer<in M : Any> : ViewRenderer<M, ContainerHolder>() {

    override fun createViewHolder(parent: ViewGroup): ContainerHolder {
        return ContainerHolder(inflate(type, parent))
    }
}