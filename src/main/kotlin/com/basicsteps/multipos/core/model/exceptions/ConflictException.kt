package com.basicsteps.multipos.core.model.exceptions

import io.netty.handler.codec.http.HttpResponseStatus
import javax.ws.rs.ClientErrorException

class ConflictException(message: String? = "Such data already exists in db"): ClientErrorException(message, HttpResponseStatus.CONFLICT.code())