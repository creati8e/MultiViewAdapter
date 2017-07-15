package serg.chuprin.multiviewadapter.view.adapter

import serg.chuprin.adapter.MultiViewAdapter

class PaginationAdapter(items: MutableList<Any> = mutableListOf<Any>()) : MultiViewAdapter(items) {

    fun setLoadingMoreVisibility(visible: Boolean) = when (visible) {
        true -> addItemIfNotPresent(ProgressModel())
        false -> removeLast(ProgressModel::class.java)
    }

    fun setNetworkErrorVisibility(visible: Boolean) = when (visible) {
        true -> addItemIfNotPresent(NetworkErrorModel())
        false -> removeLast(NetworkErrorModel::class.java)
    }

}