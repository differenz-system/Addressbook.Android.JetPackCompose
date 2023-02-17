package com.jetpackcompose.addressbook.activities

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
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.jetpackcompose.addressbook.R

import com.jetpackcompose.addressbook.ui.theme.*
import com.jetpackcompose.addressbook.utils.Credentials
import com.jetpackcompose.addressbook.utils.Validation
import com.jetpackcompose.addressbook.utils.toast

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class ForgotPasswordActivity : ComponentActivity() {

    private var email by mutableStateOf(TextFieldValue(""))
    private var emailError by mutableStateOf(false)

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

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Start)
                        .padding(8.dp),
                        onClick = { onBackPressedDispatcher.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "",
                            tint = white
                        )
                    }
                    ShowLottieLogo()
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center,
                        text = "Forgot Password?",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Enter registered email address to\nretrieve password",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextField(modifier = Modifier.fillMaxWidth(0.87f),
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
                    keyboardActions = KeyboardActions(onDone = { localFocusManager?.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.87f)
                        .clip(RoundedCornerShape(16))
                        .clickable(onClick = { forgotPassword(email.text) }),
                    shape = RoundedCornerShape(16),
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Forgot Password",
                        style = TextStyle(color = white),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    /**
     * Show no address animation.
     * No need to destroy or stop animation
     * [LottieAnimationState] will automatically do this for us.
     */
    @Composable
    fun ColumnScope.ShowLottieLogo() {

        val res = if (isDarkMode) R.raw.forgot_password_dark else R.raw.forgot_password_light
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(res))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            restartOnPlay = false
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize(0.4f)
                .weight(1f)
        )
    }

    private fun forgotPassword(email: String?) {
        Validation(this@ForgotPasswordActivity).apply {
            isEmailValid(email = email).also { valid ->
                this@ForgotPasswordActivity.emailError = !valid
                this@ForgotPasswordActivity.email = TextFieldValue(text = (email ?: "").trim())

                if (valid) {
                    Credentials(context = context, email = email ?: "").getPassword()?.also {
                        toast(context = context, message = it)
                    }
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
