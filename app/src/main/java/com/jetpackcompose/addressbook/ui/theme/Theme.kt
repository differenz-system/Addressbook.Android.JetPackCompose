package com.jetpackcompose.addressbook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode

private val DarkColorPalette = darkColors(
    primary = Purple200, primaryVariant = Purple700, secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500, primaryVariant = Purple700, secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

/*
* To check whether @Preview is available or not.
* This will help to see preview in compiling time.
*/
var isInspectionMode = false
var isDarkMode = false
var localFocusManager: FocusManager? = null

@Composable
fun fieldColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        backgroundColor = wb05(),
        /*cursorColor = black,
        textColor = black,*/
        disabledIndicatorColor = transparent,
        errorIndicatorColor = Color.Red,
        focusedIndicatorColor = transparent,
        unfocusedIndicatorColor = transparent,
        /*unfocusedLabelColor = black50,*/
    )
}

@Composable
fun AddressBookJetpackComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {

    isDarkMode = darkTheme
    isInspectionMode = LocalInspectionMode.current
    localFocusManager = LocalFocusManager.current

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors, typography = Typography, shapes = Shapes, content = content
    )
}