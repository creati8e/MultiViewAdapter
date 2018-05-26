package serg.chuprin.multiviewadapter.model.repository

import io.reactivex.Observable
import serg.chuprin.multiviewadapter.model.UserEntity
import java.util.*
import java.util.concurrent.TimeUnit

class UsersRepository : PaginationRepository<UserEntity> {

    override fun getPage(offset: Int): Observable<List<UserEntity>> {
        val rand = Random()
        if (rand.nextInt() % 5 == 0) {
            return Observable.error { Throwable() }
        }
        return Observable
                .fromCallable {
                    (0 until 10)
                            .asSequence()
                            .map {
                                UserEntity(
                                    rand.nextInt(),
                                    UUID.randomUUID().toString().substring(0, 5)
                                )
                            }
                            .toList()
                }
                .delay(1, TimeUnit.SECONDS)
    }
}