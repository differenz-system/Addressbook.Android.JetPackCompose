package com.jetpackcompose.addressbook.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val transparent = Color(0x00000000)

val black = Color(0xFF000000)
val black05 = Color(0x0D000000)
val black10 = Color(0x1A000000)
val black20 = Color(0x33000000)
val black50 = Color(0x80000000)

val white = Color(0xFFFFFFFF)
val white05 = Color(0x0DFFFFFF)
val white10 = Color(0x1AFFFFFF)
val white20 = Color(0x33FFFFFF)
val white50 = Color(0x80FFFFFF)

fun wb05() = if (isDarkMode) white05 else black05