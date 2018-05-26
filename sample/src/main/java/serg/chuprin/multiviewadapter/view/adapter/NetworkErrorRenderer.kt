package serg.chuprin.multiviewadapter.view.adapter

import kotlinx.android.synthetic.main.list_item_network_error.view.*
import serg.chuprin.adapter.Click
import serg.chuprin.adapter.LongClick
import serg.chuprin.adapter.SimpleViewRenderer
import serg.chuprin.adapter.ViewHolder
import serg.chuprin.multiviewadapter.R


class NetworkErrorRenderer : SimpleViewRenderer<NetworkErrorModel>() {

    override val type: Int = R.layout.list_item_network_error

    override fun onVhCreated(
        holder: ViewHolder, clickListener: Click?,
        longClickListener: LongClick?
    ) {
        holder.itemView.tryBtn.setOnClickListener {
            clickListener?.onClick(it, holder.layoutPosition)
        }
    }
}