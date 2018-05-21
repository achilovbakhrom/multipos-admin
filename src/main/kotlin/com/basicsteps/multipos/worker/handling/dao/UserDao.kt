package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.utils.KeycloakConfig
import io.reactivex.Observable
import org.keycloak.representations.idm.UserRepresentation

class UserDao(val dbManager: DbManager) {

    fun save(user: UserRepresentation) : Observable<UserRepresentation> {
        return Observable.create({ event ->
            val client = dbManager.keycloak!!
            client
                    .realm(KeycloakConfig.model.realm)
                    .users()
                    .create(user)
            event.onNext(user)
        })
    }

    fun remove(user: UserRepresentation) : Observable<Boolean> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

    fun update(user: UserRepresentation) : Observable<UserRepresentation> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

    fun findByEMail(email: String) : Observable<UserRepresentation> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

    fun findAll() : Observable<List<UserRepresentation>> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

}