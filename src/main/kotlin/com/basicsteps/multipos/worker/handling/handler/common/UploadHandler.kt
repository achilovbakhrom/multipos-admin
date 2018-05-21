package com.basicsteps.multipos.worker.handling.handler.common

import com.basicsteps.multipos.config.imageURL
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import org.apache.commons.io.FilenameUtils
import java.net.URLDecoder


class UploadHandler {
    companion object {
        fun uploadAvatar(vertx: Vertx, routingContext: RoutingContext) {
            val fileUploadSet = routingContext.fileUploads()
            val fileUploadIterator = fileUploadSet.iterator()
            while (fileUploadIterator.hasNext()) {
                val fileUpload = fileUploadIterator.next()
                val uploadedFile = vertx.fileSystem().readFileBlocking(fileUpload.uploadedFileName())
                try {
                    val fileUploadsDir = "file-uploads/"
                    val fn = fileUpload.fileName()
                    val extension = FilenameUtils.getExtension(fn)
                    var uploadedFileName = fileUpload.uploadedFileName()
                    uploadedFileName = uploadedFileName.substring(fileUploadsDir.length, uploadedFileName.length)
                    val filePath = "avatars/$uploadedFileName.$extension"
                    val fileNameURL = URLDecoder.decode(filePath, "UTF-8")
                    val fileSystem = vertx.fileSystem()
                    fileSystem.exists("avatars", { event ->
                        if (event.succeeded()) {
                            if (!event.result()) {
                                fileSystem.mkdir("avatars", {result ->
                                    if (result.succeeded()) {
                                        fileSystem.writeFile(fileNameURL, uploadedFile, {event ->
                                            if (event.succeeded()) {
                                                val tempUploadFilePath = "file-uploads/$uploadedFileName"
                                                fileSystem.delete(tempUploadFilePath, { result -> })
                                                routingContext.response().end(MultiPosResponse("$uploadedFileName.$extension", null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                                            } else {
                                                routingContext.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                            }
                                        })
                                    } else {
                                        routingContext.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                    }
                                })
                            } else {
                                fileSystem.writeFile(fileNameURL, uploadedFile, { event ->
                                    if (event.succeeded()) {
                                        val tempUploadFilePath = "file-uploads/$uploadedFileName"
                                        fileSystem.delete(tempUploadFilePath, null)
                                        routingContext.response().end(MultiPosResponse(imageURL("$uploadedFileName.$extension"), null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                                    } else {
                                        routingContext.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                    }
                                })
                            }
                        } else {
                            routingContext.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun uploadProductImage(routingContext: RoutingContext) {

        }
    }
}