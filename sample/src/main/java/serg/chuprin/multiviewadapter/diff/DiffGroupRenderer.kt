package serg.chuprin.multiviewadapter.diff

import kotlinx.android.synthetic.main.list_item_diff_group.*
import serg.chuprin.adapter.ContainerHolder
import serg.chuprin.adapter.ContainerRenderer
import serg.chuprin.multiviewadapter.R

/**
 * @author Sergey Chuprin
 */
class DiffGroupRenderer : ContainerRenderer<DiffItem.Group>() {
    override val type = R.layout.list_item_diff_group

    override fun bindView(holder: ContainerHolder, model: DiffItem.Group) {
        super.bindView(holder, model)
        holder.textView.text = model.name
    }
}