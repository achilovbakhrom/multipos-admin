package com.basicsteps.multipos.utils

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.StartTLSOptions
import io.vertx.kotlin.ext.mail.MailConfig
import io.vertx.kotlin.ext.mail.MailMessage
import org.apache.commons.io.IOUtils
import java.io.BufferedInputStream


val logger = LoggerFactory.getLogger("com.basicsteps.multipos")

//UserRepresentation utils
fun sendEmail(to: String, text: String, html: String, successHandler: () -> Unit, errorHandler: () -> Unit) {
    val config = MailConfig()
    config.hostname = "smtp.gmail.com"
    config.port = 587
    config.starttls = StartTLSOptions.REQUIRED
    config.username = "bahrom.achilov@finnetlimited.com"
    config.password = "thinkthank001"
    val mailClient = MailClient.createNonShared(Vertx.vertx(), config)
    val sendingMessage = MailMessage()
    sendingMessage.from = "bahrom.achilov@finnetlimited.com"
    sendingMessage.setTo(to)
    sendingMessage.setCc(to)
    sendingMessage.text = text
    sendingMessage.html = html
    mailClient.sendMail(sendingMessage, {result ->
        if (result.succeeded()) {
            successHandler()
        } else {
            errorHandler()
        }
    })
}

fun getFromUrl(resourceName: String) : String {
    val stream = SystemConfig::class.java.classLoader.getResourceAsStream(resourceName)
    val reader = BufferedInputStream(stream)
    var result = ""
    val list = IOUtils.readLines(stream)
    for (text in list) {
        val temp = text as String
        result += temp + "\n"
    }
    return result
}
