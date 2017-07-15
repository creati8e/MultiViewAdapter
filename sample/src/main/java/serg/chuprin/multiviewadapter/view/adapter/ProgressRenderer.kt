package serg.chuprin.multiviewadapter.view.adapter

import serg.chuprin.adapter.ViewHolder
import serg.chuprin.adapter.ViewRenderer
import serg.chuprin.multiviewadapter.R

class ProgressRenderer : ViewRenderer<ProgressModel, ViewHolder>() {

    override fun type(): Int = R.layout.list_item_progress

    override fun isViewForType(model: Any): Boolean = model is ProgressModel

}