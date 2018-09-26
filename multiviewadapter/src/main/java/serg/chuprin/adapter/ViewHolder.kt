package serg.chuprin.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

typealias Click = ViewHolder.ClickCallback
typealias LongClick = ViewHolder.LongClickCallback

open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    interface ClickCallback {
        fun onClick(view: View, position: Int)
    }

    interface LongClickCallback {
        fun onLongClick(view: View, position: Int)
    }
}
