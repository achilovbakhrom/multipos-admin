package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.SubCategory
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class SubCategoryDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<SubCategory>(dbManager, dataStore, SubCategory::class.java)