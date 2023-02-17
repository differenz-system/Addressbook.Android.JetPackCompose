package com.jetpackcompose.addressbook.utils

import android.content.Context
import android.util.Patterns

/**
 * 02/02/23
 *
 * @since 1.0.
 */
class Validation(
    val context: Context,
    private val name: String? = null,
    private val email: String? = null,
    private val password: String? = null,
    private val phoneNumber: String? = null
) {

    fun isNameValid(name: String? = this.name): Boolean {
        if (name == null || name.trim().isEmpty()) {
            toast(context, "Please enter name")
            return false
        }
        return true
    }

    /**
     * @return true on email is valid else false.
     */
    fun isEmailValid(email: String? = this.email): Boolean {
        if (email == null || email.trim().isEmpty()) {
            toast(context = context, message = "Please enter email")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            toast(context = context, message = "Please enter valid email")
            return false
        }
        return true
    }

    /**
     * @return true on password is valid else false.
     */
    fun isPasswordValid(password: String? = this.password): Boolean {
        if (password == null || password.trim().isEmpty()) {
            toast(context = context, message = "Please enter password")
            return false
        } else if (password.trim().length < 6) {
            toast(context = context, message = "Password must be greater than 6 digits")
            return false
        } else if (password.trim().contains(" ")) {
            toast(context = context, message = "Password must not contain space")
            return false
        }
        return true
    }

    /**
     * @return true on phone number is valid else false.
     */
    fun isPhoneNumberValid(phoneNumber: String? = this.phoneNumber): Boolean {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            toast(context = context, message = "Please enter phone number")
            return false
        } else if (!Patterns.PHONE.matcher(phoneNumber.trim()).matches()) {
            toast(context = context, message = "Please enter valid phone number")
            return false
        }
        return true
    }
}