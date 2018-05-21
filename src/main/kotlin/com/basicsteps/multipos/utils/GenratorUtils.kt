package com.basicsteps.multipos.utils

import java.util.*
import java.util.Random



object GenratorUtils {
    var sequence = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    fun generateUUID() : String = UUID.randomUUID().toString()
    fun generateRandomInt(min: Int = 0, max: Int = 100) : Int = Random().nextInt(max - min) + min
    fun generateTenancyId(count: Int = 6) : String {
        var result = ""
        for (i in 0..count) { result += sequence[generateRandomInt(0, sequence.length)] }
        return result
    }
}