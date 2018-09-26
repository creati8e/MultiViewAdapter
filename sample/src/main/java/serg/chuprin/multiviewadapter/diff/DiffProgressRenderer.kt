package serg.chuprin.multiviewadapter.diff

import serg.chuprin.adapter.ContainerRenderer
import serg.chuprin.multiviewadapter.R

/**
 * @author Sergey Chuprin
 */
class DiffProgressRenderer : ContainerRenderer<DiffItem.Progress>() {
    override val type: Int = R.layout.list_item_diff_progress
}