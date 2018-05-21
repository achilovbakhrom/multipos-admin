package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.common.MPUser
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class MPUserDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<MPUser>(dbManager, dataStore, MPUser::class.java)