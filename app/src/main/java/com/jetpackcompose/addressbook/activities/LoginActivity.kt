package com.jetpackcompose.addressbook.activities

import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

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
class LoginActivity : ComponentActivity() {

    private var email by mutableStateOf(TextFieldValue(""))
    private var password by mutableStateOf(TextFieldValue(""))
    private var passwordVisibility by mutableStateOf(false)
    private var emailError by mutableStateOf(false)
    private var passwordError by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateTo {
            setContent {
                AddressBookJetpackComposeTheme {
                    InitUI()
                }
            }
        }
    }

    /**
     * Navigate user to valid screen on app open.
     */
    private fun navigateTo(function: () -> Unit) {
        val loggedIn = Credentials(this).isAccountExist()
        if (loggedIn) {
            navigateToMain()
        } else function()
    }

    @Composable
    fun InitUI() {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
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
                    text = "Please login to continue",
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

                TextField(
                    modifier = Modifier.fillMaxWidth(0.87f),
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
                    },
                )

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
                        modifier = Modifier.padding(16.dp), text = "Login", style = TextStyle(
                            color = white
                        ), textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .clickable(onClick = {
                            navigateToForgotPassword()
                        }), text = "Forgot Password?", style = TextStyle(
                        color = MaterialTheme.colors.primary
                    )
                )

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
                                navigateToSignup()
                            }),
                        text = "Don't have an Account? Register!",
                        style = TextStyle(color = MaterialTheme.colors.primary)
                    )
                }
            }
        }
    }

    /**
     * Check enter credentials is valid or not.
     * Proceed for login if credentials valid.
     */
    private fun checkCredentials() {

        if (!isEmailPasswordValid(email.text, password.text)) return

        Credentials(this, email.text, password.text).apply {
            if (isAccountExist()) {

                if (isPasswordMatched()) {

                    setLoginCredentials()
                    navigateToMain()

                } else toast(this@LoginActivity, "Invalid password")

            } else toast(this@LoginActivity, "Invalid credentials. Register account first.")
        }
    }

    /**
     * Navigate to [SignupActivity] for new registration.
     */
    private fun navigateToSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        launcher.launch(intent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                finish()
            }
        }

    /**
     * Navigate to [ForgotPasswordActivity].
     */
    private fun navigateToForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    /**
     * After successfully login navigate to [MainActivity].
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @return true if email and password is valid else false.
     */
    private fun isEmailPasswordValid(email: String?, password: String?): Boolean {
        return Validation(this).run {
            isEmailValid(email = email).also {
                this@LoginActivity.emailError = !it
                this@LoginActivity.email = TextFieldValue(text = (email ?: "").trim())
            } && isPasswordValid(password = password).also {
                this@LoginActivity.passwordError = !it
                this@LoginActivity.password = TextFieldValue(text = (password ?: "").trim())
            }
        }
    }

    /**
     * Preview the UI.
     */
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AddressBookJetpackComposeTheme(darkTheme = true) {
            InitUI()
        }
    }
}
