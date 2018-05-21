package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Category
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class CategoryDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Category>(dbManager, dataStore, Category::class.java)