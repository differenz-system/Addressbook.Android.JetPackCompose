package com.jetpackcompose.addressbook.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * 24th Jan 2023.
 *
 * Credentials preference class.
 * @since 1.0
 */
class Credentials(
    context: Context, private var email: String = "", private var password: String = ""
) {

    /**
     * All new signup credentials will save into [accountPreferences].
     */
    private var accountPreferences: SharedPreferences

    /**
     * Current active login preference.
     */
    private var activeLoginPreferences: SharedPreferences

    init {

        activeLoginPreferences =
            context.getSharedPreferences("activeLoginInfo", Context.MODE_PRIVATE)

        //on first initialize email and password will always empty
        //so, let's get email and password from activeLoginPreferences.
        if (email.isEmpty() && password.isEmpty()) {
            email = activeLoginPreferences.getString("email", "") ?: ""
            password = activeLoginPreferences.getString("password", "") ?: ""
        }

        accountPreferences = context.getSharedPreferences(email, Context.MODE_PRIVATE)
    }

    fun set(): Boolean {
        return setEmail(email) && setPassword(password)
    }

    fun get(): Array<String?> {
        if (isAccountExist()) {
            return arrayOf(getEmail(), getPassword())
        }
        return arrayOf()
    }

    private fun setEmail(email: String): Boolean {
        return accountPreferences.edit().putString("email", email).commit()
    }

    fun setPassword(password: String): Boolean {
        return accountPreferences.edit().putString("password", password).commit()
    }

    fun getEmail(): String? {
        return accountPreferences.getString("email", null)
    }

    fun getPassword(): String? {
        return accountPreferences.getString("password", null)
    }

    fun isAccountExist(): Boolean {
        return accountPreferences.run {
            contains("email") && contains("password")
        }
    }

    fun isPasswordMatched(): Boolean {
        return getPassword()?.equals(password) ?: false
    }

    fun clearAccount(): Boolean {
        return clearLoginCredentials() && clearAccountPreferences()
    }

    fun setLoginCredentials() {
        activeLoginPreferences.edit().apply {
            putString("email", email)
            putString("password", password)
        }.apply()
    }

    fun clearLoginCredentials(): Boolean {
        return activeLoginPreferences.edit().apply {
            remove("email")
            remove("password")
        }.commit()
    }

    private fun clearAccountPreferences(): Boolean {
        return accountPreferences.edit().apply {
            remove("email")
            remove("password")
        }.commit()
    }
}