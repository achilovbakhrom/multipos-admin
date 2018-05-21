package com.basicsteps.multipos.utils

/**
 * Created at 20.01.2018
 *
 * @author Achilov Bakhrom
 *
 * Singleton Helper for validation inputs
 */

object ValidationUtils {

    /**
     * Validation of phone number
     */
    fun phoneNumber(phoneNumber: String) : Boolean {
        val regex = "[\\+0-9\\s\\-\\(\\)]+".toRegex()
        return regex.matches(phoneNumber)
    }

    /**
     * Validation for e-mail address
     */
    fun email(email: String) : Boolean{
        val regex = "^[a-z0-9._%\\+-]+@[a-z0-9.-]+\\.[a-z]{2,6}\$".toRegex()
        return regex.matches(email.toLowerCase())
    }
}