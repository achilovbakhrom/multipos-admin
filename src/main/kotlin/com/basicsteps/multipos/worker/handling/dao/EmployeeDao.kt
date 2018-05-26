package com.basicsteps.multipos.worker.handling.dao

/**
 * Created by Ikrom Mirzayev on 26-May-18.
 */
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Employee
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class EmployeeDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Employee>(dbManager, dataStore, Employee::class.java)