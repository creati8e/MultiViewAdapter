package serg.chuprin.multiviewadapter.model

data class UserEntity(var id: Int = -1,
                      var login: String = "",
                      var avatarUrl: String = "",
                      var repos: Int = 0,
                      var followers: Int = 0,
                      var following: Int = 0)

