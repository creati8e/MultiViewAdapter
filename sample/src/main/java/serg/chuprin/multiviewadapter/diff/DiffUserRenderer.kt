package serg.chuprin.multiviewadapter.diff

import kotlinx.android.synthetic.main.list_item_diff_user.*
import serg.chuprin.adapter.ContainerHolder
import serg.chuprin.adapter.ContainerRenderer
import serg.chuprin.multiviewadapter.R

/**
 * @author Sergey Chuprin
 */
class DiffUserRenderer : ContainerRenderer<DiffItem.User>() {
    override val type = R.layout.list_item_diff_user

    override fun bindView(holder: ContainerHolder, model: DiffItem.User) {
        super.bindView(holder, model)
        holder.urlTextView.text = model.url
    }
}