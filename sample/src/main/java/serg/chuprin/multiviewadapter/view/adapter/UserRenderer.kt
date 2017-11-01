package serg.chuprin.multiviewadapter.view.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.list_item_user.view.*
import serg.chuprin.adapter.Click
import serg.chuprin.adapter.LongClick
import serg.chuprin.adapter.SimpleViewRenderer
import serg.chuprin.adapter.ViewHolder
import serg.chuprin.multiviewadapter.R
import serg.chuprin.multiviewadapter.model.UserEntity


class UserRenderer : SimpleViewRenderer<UserEntity>() {

    override val type: Int = R.layout.list_item_user

    override fun bindView(holder: ViewHolder, model: UserEntity) {
        holder.itemView.textView.text = model.login

        Glide.with(holder.itemView.imageView.context)
                .load(model.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.ic_user_placeholder)
                .into(holder.itemView.imageView)
    }

    override fun onVhCreated(holder: ViewHolder, clickListener: Click?,
                             longClickListener: LongClick?) {
        holder.itemView.setOnClickListener {
            clickListener?.onClick(it, holder.layoutPosition)
        }
    }
}

