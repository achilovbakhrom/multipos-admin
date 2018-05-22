package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.DeleteDbFailedException
import com.basicsteps.multipos.core.model.exceptions.FieldConflictsException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.UserAttrs
import com.basicsteps.multipos.model.common.MPUser
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.KeycloakConfig
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.*



class SignUpDao(dbManager: DbManager, mongoDataStore: MongoDataStore?) :
        BaseDao<SignUpMapper>(dbManager, mongoDataStore, SignUpMapper::class.java) {

    private fun createUser(signUpMapper: SignUpMapper): Observable<String> {
        return Observable.create({ event ->
            val credential = CredentialRepresentation()
            credential.type = CredentialRepresentation.PASSWORD
            credential.value = signUpMapper.password
            val user = UserRepresentation()
            user.username = signUpMapper.mail
            user.email = signUpMapper.mail
            user.credentials = Arrays.asList(credential)
            val attrs = HashMap<String, List<String>>()
            attrs[UserAttrs.LANGUAGE.value()] = listOf("en")
            attrs[UserAttrs.CONTACT.value()] = listOf()
            attrs[UserAttrs.COMPANY_IDS.value()] = listOf()
            user.attributes = attrs
            user.isEnabled = true
            //TODO Adding role

            dbManager?.userDao?.save(user)?.subscribe({ createdUser ->
                val user = MPUser()
                user.keycloakId = createdUser.id
                dbManager
                        .mpUserDao
                        ?.save(user)
                        ?.subscribe()
                event.onNext(createdUser.email)
            }, {error ->
                event.onError(error)
            })
        })
    }


    fun accessCode(mail: String, accessCode: String): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val findUserQuery = dataStore?.createQuery(SignUpMapper::class.java)
                findUserQuery?.field("mail")?.`is`(mail)
                findUserQuery?.execute({ handler ->
                    if (handler.succeeded()) {
                        if (handler.result().isEmpty) {
                            event.onError(NotExistsException("mail", mail))
                            return@execute
                        }
                        if (handler.result().size() > 1) {
                            event.onError(FieldConflictsException("mail", mail))
                            return@execute
                        }
                        handler.result().iterator().next({item ->
                            val result = item.result()
                            val equal = result.accessCode.toString() == accessCode
                            if (equal) {
                                createUser(result).flatMap({ removeSignUpMapper(mail) }).subscribe()
                            }
                            event.onNext(equal)
                        })
                    } else {
                        event.onError(ReadDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
            }
        })
    }

    fun emailUnique(email: String): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val signUpQuery = dataStore?.createQuery(SignUpMapper::class.java)
                signUpQuery?.field("mail")?.`is`(email)
                signUpQuery?.execute({ handler ->
                    if (handler.succeeded()) {
                        var result = handler.result().isEmpty
                        if (result) {
                            val client = dbManager?.keycloak!!
                            val users = client.realm(KeycloakConfig.model.realm).users()
                            result = users.search(email).isEmpty()
                        }
                        event.onNext(result)
                    } else {
                        event.onError(ReadDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
            }
        })
    }


    fun removeSignUpMapper(mail: String): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val signUpQuery = dataStore?.createQuery(SignUpMapper::class.java)
                signUpQuery?.field("mail")?.`is`(mail)
                val signUpMapperDelete = dataStore?.createDelete(SignUpMapper::class.java)
                signUpMapperDelete?.setQuery(signUpQuery)
                signUpMapperDelete?.delete({ deleteResult ->
                    if (deleteResult.failed()) {
                        event.onError(DeleteDbFailedException())
                    } else {
                        event.onNext(true)
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
            }

        })
    }

    fun getSignUpMapperByMail(email: String): Observable<SignUpMapper> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val signUpQuery = dataStore?.createQuery(SignUpMapper::class.java)
                signUpQuery?.field("mail")?.`is`(email)
                signUpQuery?.execute({ handler ->
                    if (handler.succeeded()) {
                        if (handler.result().size() == 1) {
                            handler.result().iterator().next({ item -> event.onNext(item.result()) })
                        } else if (handler.result().isEmpty) {
                            event.onError(NotExistsException("mail", email))
                        } else {
                            event.onError(FieldConflictsException("mail", email))
                        }
                    } else {
                        event.onError(ReadDbFailedException())
                    }
                })
            } else
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
        })
    }

}