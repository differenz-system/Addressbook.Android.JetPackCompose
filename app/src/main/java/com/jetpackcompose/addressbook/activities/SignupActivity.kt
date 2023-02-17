package com.jetpackcompose.addressbook.activities

import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.jetpackcompose.addressbook.ui.theme.*
import com.jetpackcompose.addressbook.utils.*

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class SignupActivity : ComponentActivity() {

    private var email by mutableStateOf(TextFieldValue(""))
    private var password by mutableStateOf(TextFieldValue(""))
    private var confirmPassword by mutableStateOf(TextFieldValue(""))
    private var passwordVisibility by mutableStateOf(false)
    private var confirmPasswordVisibility by mutableStateOf(false)
    private var emailError by mutableStateOf(false)
    private var passwordError by mutableStateOf(false)
    private var confirmPasswordError by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddressBookJetpackComposeTheme {
                InitUI()
            }
        }
    }

    @Composable
    fun InitUI() {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(1f),
                    textAlign = TextAlign.Center,
                    text = "AddressBook",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Create an account",
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(0.87f),
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    label = { Text(text = "Enter Email") },
                    colors = fieldColors(),
                    shape = RoundedCornerShape(percent = 16),
                    singleLine = true,
                    isError = emailError,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(modifier = Modifier.fillMaxWidth(0.87f),
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    label = { Text(text = "Enter Password") },
                    colors = fieldColors(),
                    shape = RoundedCornerShape(percent = 16),
                    singleLine = true,
                    isError = passwordError,
                    keyboardActions = KeyboardActions(onDone = { localFocusManager?.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
                    ),

                    visualTransformation = if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),

                    trailingIcon = {
                        val image = if (passwordVisibility) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisibility) "Hide password"
                        else "Show password"

                        IconButton(modifier = Modifier.padding(end = 4.dp),
                            onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(imageVector = image, description)
                        }
                    })

                Spacer(modifier = Modifier.height(16.dp))

                TextField(modifier = Modifier.fillMaxWidth(0.87f),
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = false
                    },
                    label = { Text(text = "Enter Confirm Password") },
                    colors = fieldColors(),
                    shape = RoundedCornerShape(percent = 16),
                    singleLine = true,
                    isError = confirmPasswordError,
                    keyboardActions = KeyboardActions(onDone = { localFocusManager?.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
                    ),

                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),

                    trailingIcon = {
                        val image = if (confirmPasswordVisibility) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (confirmPasswordVisibility) "Hide confirm password"
                        else "Show confirm password"

                        IconButton(modifier = Modifier.padding(end = 4.dp),
                            onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                            Icon(imageVector = image, description)
                        }
                    })

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.87f)
                        .clip(RoundedCornerShape(16))
                        .clickable(onClick = {
                            checkCredentials()
                        }),
                    shape = RoundedCornerShape(16),
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp), text = "Signup", style = TextStyle(
                            color = white
                        ), textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(bottom = 16.dp)
                            .clickable(onClick = {
                                finish()
                            }),
                        text = "Already have an account? Login!",
                        style = TextStyle(color = MaterialTheme.colors.primary)
                    )
                }
            }
        }
    }

    /**
     * Check enter credentials is valid or not.
     */
    private fun checkCredentials() {

        if (!isEmailPasswordValid(email.text, password.text, confirmPassword.text)) return

        Credentials(this, email.text, password.text).apply {
            if (isAccountExist()) {
                toast(
                    this@SignupActivity, "Account already registered with this email."
                )
            } else {
                if (set()) {
                    setLoginCredentials()
                    finishLoginActivity()
                    navigateToMain()
                } else {
                    toast(this@SignupActivity, "Failed to signup")
                }
            }
        }
    }

    /**
     * Finish [LoginActivity] before navigate to [MainActivity].
     */
    private fun finishLoginActivity() {
        setResult(RESULT_OK)
    }

    /**
     * After successfully signup navigate to [MainActivity].
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    /**
     * @return true if email and password is valid else false.
     */
    private fun isEmailPasswordValid(
        email: String?, password: String?, confirmPassword: String?
    ): Boolean {
        return Validation(this).run {
            isEmailValid(email = email).also {
                this@SignupActivity.emailError = !it
                this@SignupActivity.email = TextFieldValue(text = (email ?: "").trim())
            } && isPasswordValid(password = password).also {
                this@SignupActivity.passwordError = !it
                this@SignupActivity.password = TextFieldValue(text = (password ?: "").trim())
            } && isPasswordValid(password = confirmPassword).also {
                this@SignupActivity.confirmPasswordError = true
                this@SignupActivity.confirmPassword =
                    TextFieldValue(text = (confirmPassword ?: "").trim())
            } && confirmPassword.equals(password).also {
                this@SignupActivity.confirmPasswordError = !it
                if (this@SignupActivity.confirmPasswordError) {
                    toast(this@SignupActivity, "Password mismatched")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AddressBookJetpackComposeTheme(darkTheme = true) {
            InitUI()
        }
    }
}
