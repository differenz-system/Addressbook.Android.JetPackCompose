package com.jetpackcompose.addressbook.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jetpackcompose.addressbook.R

import com.jetpackcompose.addressbook.ui.theme.*

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class LauncherActivity : ComponentActivity() {

    private val isLottiePlaying by mutableStateOf(value = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            rememberSystemUiController().isStatusBarVisible = false
            AddressBookJetpackComposeTheme {
                InitUI()
            }
        }
    }

    @Composable
    fun InitUI() {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLottiePlaying) {
                    ShowLottieLogo()
                    Handler(Looper.myLooper()!!).postDelayed({
                        navigationToLogin()
                    }, 4000)
                }
            }
        }
    }

    /**
     * Show launcher animation.
     * No need to destroy or stop animation
     * [LottieAnimationState] will automatically do this for us.
     */
    @Composable
    fun ShowLottieLogo() {

        val res = if (isDarkMode) R.raw.launcher_dark else R.raw.launcher_light
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
                .fillMaxSize(0.3f)
                .aspectRatio(1f)
        )
    }

    private fun navigationToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AddressBookJetpackComposeTheme(darkTheme = true) {
            InitUI()
        }
    }
}
