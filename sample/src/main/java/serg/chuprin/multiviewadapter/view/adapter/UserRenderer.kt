package serg.chuprin.multiviewadapter.view.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.list_item_user.*
import serg.chuprin.adapter.Click
import serg.chuprin.adapter.ContainerHolder
import serg.chuprin.adapter.ContainerRenderer
import serg.chuprin.adapter.LongClick
import serg.chuprin.multiviewadapter.R
import serg.chuprin.multiviewadapter.model.UserEntity


class UserRenderer : ContainerRenderer<UserEntity>() {

    override val type: Int = R.layout.list_item_user

    override fun bindView(holder: ContainerHolder, model: UserEntity) {
        super.bindView(holder, model)

        holder.textView.text = model.login

        Glide
            .with(holder.imageView.context)
            .load(model.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .error(R.drawable.ic_user_placeholder)
    }

    override fun onVhCreated(
        holder: ContainerHolder,
        clickListener: Click?,
        longClickListener: LongClick?
    ) = holder.itemView.setOnClickListener { clickListener?.onClick(it, holder.layoutPosition) }
}

