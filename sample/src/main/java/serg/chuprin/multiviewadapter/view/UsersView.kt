package serg.chuprin.multiviewadapter.view

import serg.chuprin.multiviewadapter.model.UserEntity

interface UsersView {

    fun addData(list: List<UserEntity>)

    fun paginationProgress(visible: Boolean)

    fun showNetworkError(visible: Boolean)
}