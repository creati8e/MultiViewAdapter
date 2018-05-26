package serg.chuprin.multiviewadapter.model

data class UserEntity(
    val id: Int = -1,
    val login: String = "",
    val avatarUrl: String = "",
    val repos: Int = 0,
    val followers: Int = 0,
    val following: Int = 0
)

