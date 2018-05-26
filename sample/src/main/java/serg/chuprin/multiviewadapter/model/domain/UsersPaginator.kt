package serg.chuprin.multiviewadapter.model.domain

import io.reactivex.Observable
import serg.chuprin.multiviewadapter.model.UserEntity
import serg.chuprin.multiviewadapter.model.repository.PaginationRepository

class UsersPaginator(
    private val repository: PaginationRepository<UserEntity>
) : Paginator<UserEntity>() {

    private var lastUserId: Int = -1

    override fun nextPage(offset: Int): Observable<List<UserEntity>> {
        val actualOffset = if (lastUserId == -1) offset else lastUserId
        return repository
                .getPage(actualOffset)
                .doOnNext { if (it.isNotEmpty()) lastUserId = it.last().id }
    }

}