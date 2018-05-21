package com.basicsteps.multipos.utils

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.StartTLSOptions
import io.vertx.kotlin.ext.mail.MailConfig
import io.vertx.kotlin.ext.mail.MailMessage



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