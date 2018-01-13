package serg.chuprin.multiviewadapter.view.adapter

import chuprin.serg.extensions.ContainerHolder
import chuprin.serg.extensions.ContainerRenderer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.list_item_user.*
import serg.chuprin.adapter.Click
import serg.chuprin.adapter.LongClick
import serg.chuprin.multiviewadapter.R
import serg.chuprin.multiviewadapter.model.UserEntity


class UserRenderer : ContainerRenderer<UserEntity>() {

    override val type: Int = R.layout.list_item_user

    override fun bindView(holder: ContainerHolder, model: UserEntity) = with(holder) {
        textView.text = model.login

        Glide.with(imageView.context)
                .load(model.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.ic_user_placeholder)
                .into(imageView)
        Unit
    }

    override fun onVhCreated(

            holder: ContainerHolder,
            clickListener: Click?,
            longClickListener: LongClick?

    ) = holder.itemView.setOnClickListener { clickListener?.onClick(it, holder.layoutPosition) }
}

