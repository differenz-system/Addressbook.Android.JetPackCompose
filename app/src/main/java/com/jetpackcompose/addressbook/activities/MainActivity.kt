package com.jetpackcompose.addressbook.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.jetpackcompose.addressbook.R
import com.jetpackcompose.addressbook.roomdb.AddressBook
import com.jetpackcompose.addressbook.ui.theme.*
import com.jetpackcompose.addressbook.utils.*
import com.jetpackcompose.addressbook.viewmodel.AddressViewModel

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class MainActivity : ComponentActivity() {

    private val addressViewModel: AddressViewModel by lazy {
        AddressViewModel(this, this)
    }

    private var deleteAddress: AddressBook? by mutableStateOf(value = null)
    private var showLogoutAlertDialog by mutableStateOf(value = false)
    private var showDeletePermanentlyAlertDialog by mutableStateOf(value = false)

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

        LaunchedEffect(key1 = Unit) {
            //get all saved addresses from database
            addressViewModel.getAllAddresses()
        }

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.Top) {
                SetTopAppBar()
                SetAddresses()

                deleteAddress?.run {
                    ShowAlertDialog(
                        title = "Delete",
                        text = "Are you sure want to delete address?",
                        confirmButtonText = "Delete",
                        dismissButtonText = "Cancel"
                    ) { isPositive ->
                        if (isPositive) {
                            addressViewModel.deleteAddressBook(this)
                        }
                        deleteAddress = null
                    }
                }

                if (showLogoutAlertDialog) {
                    ShowAlertDialog(
                        title = "Logout",
                        text = "Are you sure want to logout?",
                        confirmButtonText = "Logout",
                        dismissButtonText = "Cancel"
                    ) { isPositive ->
                        showLogoutAlertDialog = false
                        if (isPositive) {
                            logout()
                        }
                    }
                }

                if (showDeletePermanentlyAlertDialog) {
                    ShowAlertDialog(
                        title = "Delete Permanently",
                        text = "Account will no longer exist. Are you sure want to delete account?",
                        confirmButtonText = "Delete",
                        dismissButtonText = "Cancel"
                    ) { isPositive ->
                        showDeletePermanentlyAlertDialog = false
                        if (isPositive) {
                            deletePermanently()
                        }
                    }
                }
            }
        }
    }

    /**
     * Set app toolbar.
     */
    @Composable
    private fun SetTopAppBar() {
        TopAppBar(modifier = Modifier.fillMaxWidth(),

            title = {
                Text(text = "AddressBook", color = white)
            }, actions = {

                ActionMenu(
                    numIcons = 1,

                    items = listOf(

                        ActionItem(
                            nameRes = R.string.add,
                            icon = Icons.Filled.AddCircleOutline,
                            overflowMode = OverflowMode.NEVER_OVERFLOW
                        ) {
                            navigateToEdit()
                        },

                        ActionItem(
                            nameRes = R.string.logout,
                            icon = Icons.Filled.Logout,
                            overflowMode = OverflowMode.ALWAYS_OVERFLOW
                        ) {
                            showLogoutAlertDialog = true
                        },

                        ActionItem(
                            nameRes = R.string.delete_permanently,
                            icon = Icons.Filled.DeleteForever,
                            overflowMode = OverflowMode.ALWAYS_OVERFLOW
                        ) {
                            showDeletePermanentlyAlertDialog = true
                        })
                )
            })
    }

    /**
     * Display list of addresses.
     */
    @Composable
    private fun SetAddresses() {

        addressViewModel.run {

            if (isAddressesLoaded) {

                if (mutableAddress.size == 0) {

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        ShowLottieLogo()
                    }
                    return
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(addressViewModel.mutableAddress) { addressBook ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = addressBook.name ?: "Unknown",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = addressBook.email ?: "-")
                                Text(text = addressBook.phoneNumber ?: "-")

                                Row(modifier = Modifier.align(Alignment.End)) {

                                    IconButton(modifier = Modifier.size(24.dp), onClick = {
                                        navigateToEdit(addressBook)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = "Edit",
                                        )
                                    }

                                    IconButton(modifier = Modifier
                                        .padding(start = 16.dp)
                                        .size(24.dp),
                                        onClick = {
                                            deleteAddress = addressBook
                                        }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator(color = Purple500)
                }
            }
        }
    }

    /**
     * Show no address animation.
     * No need to destroy or stop animation
     * [LottieAnimationState] will automatically do this for us.
     */
    @Composable
    fun ShowLottieLogo() {

        val res = if (isDarkMode) R.raw.search_empty_dark else R.raw.search_empty_light
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
                .fillMaxSize(0.5f)
                .aspectRatio(1f)
        )
    }

    /**
     * Close AddressBook database.
     * Clear current user [Credentials].
     * Logout from app and navigate user to [SignInActivity].
     */
    private fun logout() {
        addressViewModel.closeDatabase().also {
            val cleared = Credentials(this).clearLoginCredentials()
            if (cleared) {
                navigateToSignIn()
            } else toast(this, "Something went wrong")
        }
    }

    /**
     * Delete AddressBook database.
     * Delete [Credentials] permanently.
     * Logout from app and navigate user to [SignInActivity].
     */
    private fun deletePermanently() {
        addressViewModel.clearAndCloseDatabase()
        Credentials(this).clearAccount()
        navigateToSignIn()
    }

    /**
     * After successful logout navigate to [SignInActivity].
     */
    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Navigate user to [EditActivity] to explore address
     * and to do some changes in selected address.
     */
    private fun navigateToEdit(addressBook: AddressBook? = null) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("addressBook", addressBook)
        startActivity(intent)
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AddressBookJetpackComposeTheme(darkTheme = false) {
            InitUI()
        }
    }
}

