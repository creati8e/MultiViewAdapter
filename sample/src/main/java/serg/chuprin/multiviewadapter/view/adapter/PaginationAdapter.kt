package serg.chuprin.multiviewadapter.view.adapter

import serg.chuprin.adapter.MultiViewAdapter

class PaginationAdapter : MultiViewAdapter() {

    fun setLoadingMoreVisibility(visible: Boolean) = when (visible) {
        true -> addItemIfNotPresent(ProgressModel())
        false -> removeLast(ProgressModel::class.java)
    }

    fun setNetworkErrorVisibility(visible: Boolean) = when (visible) {
        true -> addItemIfNotPresent(NetworkErrorModel())
        false -> removeLast(NetworkErrorModel::class.java)
    }

}