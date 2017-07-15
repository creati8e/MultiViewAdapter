package serg.chuprin.multiviewadapter.model.repository

import io.reactivex.Observable

interface PaginationRepository<MODEL> {

    fun getPage(offset: Int = -1): Observable<List<MODEL>>
}
