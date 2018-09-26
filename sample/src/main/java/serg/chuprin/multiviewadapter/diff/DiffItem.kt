package serg.chuprin.multiviewadapter.diff

/**
 * @author Sergey Chuprin
 */
sealed class DiffItem {

    data class User(val id: Int, val url: String) : DiffItem()

    data class Group(val name: String) : DiffItem()

    object Progress : DiffItem()
}