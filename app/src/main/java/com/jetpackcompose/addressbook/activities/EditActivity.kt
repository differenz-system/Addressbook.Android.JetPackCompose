package com.jetpackcompose.addressbook.activities

import android.os.Build
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetpackcompose.addressbook.roomdb.AddressBook
import com.jetpackcompose.addressbook.ui.theme.*
import com.jetpackcompose.addressbook.utils.ShowAlertDialog
import com.jetpackcompose.addressbook.utils.Validation
import com.jetpackcompose.addressbook.utils.toast
import com.jetpackcompose.addressbook.viewmodel.AddressViewModel

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class EditActivity : ComponentActivity() {

    private var addressBook: AddressBook? = null
    private var nameError by mutableStateOf(value = false)
    private var emailError by mutableStateOf(value = false)
    private var phoneNumberError by mutableStateOf(value = false)
    private var showUpdateAlertDialog by mutableStateOf(value = false)
    private var showDeleteAlertDialog by mutableStateOf(value = false)

    private val addressViewModel: AddressViewModel by lazy {
        AddressViewModel(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addressBook = getIntentData()

        setContent {
            AddressBookJetpackComposeTheme {
                InitUI()
            }
        }
    }

    private fun getIntentData(): AddressBook? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("addressBook", AddressBook::class.java)
        } else {
            intent.getSerializableExtra("addressBook").let {
                if (it is AddressBook) {
                    it
                } else null
            }
        }
    }

    @Composable
    fun InitUI() {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top, horizontalAlignment = CenterHorizontally
            ) {

                var name by remember { mutableStateOf(value = addressBook?.name ?: "") }
                var email by remember { mutableStateOf(value = addressBook?.email ?: "") }
                var phoneNumber by remember {
                    mutableStateOf(value = addressBook?.phoneNumber ?: "")
                }

                SetTopAppBar()

                Spacer(modifier = Modifier.height(16.dp))

                SetTextField(
                    value = name,
                    label = "Enter Name",
                    isError = nameError,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ) {
                    name = it
                    nameError = false
                }

                Spacer(modifier = Modifier.height(16.dp))

                SetTextField(
                    value = email,
                    label = "Enter Email",
                    isError = emailError,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ) {
                    email = it
                    emailError = false
                }

                Spacer(modifier = Modifier.height(16.dp))

                SetTextField(
                    value = phoneNumber,
                    label = "Enter Phone Number",
                    isError = phoneNumberError,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone
                ) {
                    phoneNumber = it
                    phoneNumberError = false
                }

                Spacer(modifier = Modifier.weight(1f))

                if (addressBook != null) {
                    SetButton(text = "Delete", backgroundColor = Color.Red) { //on click
                        showDeleteAlertDialog = true
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SetButton(text = "Update") { //on click
                        showUpdateAlertDialog = isEntriesValid(name, email, phoneNumber)
                    }
                } else {
                    SetButton(text = "Save") { //on click

                        if (!isEntriesValid(name, email, phoneNumber)) return@SetButton

                        val addressBook = AddressBook(
                            name = name, email = email, phoneNumber = phoneNumber
                        )
                        addressViewModel.insertAddressBook(addressBook).also {
                            finish()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (showUpdateAlertDialog) {
                    ShowAlertDialog(title = "Update",
                        text = "Are you sure want to update this address?",
                        confirmButtonText = "Update",
                        dismissButtonText = "Cancel",
                        onClick = { isPositive ->

                            showUpdateAlertDialog = false

                            if (isPositive) {
                                val update = AddressBook(
                                    id = addressBook?.id,
                                    name = name,
                                    email = email,
                                    phoneNumber = phoneNumber,
                                    isActive = addressBook?.isActive
                                )
                                addressViewModel.updateAddressBook(update).also {
                                    finish()
                                }
                            }
                        })
                }

                if (showDeleteAlertDialog) {
                    ShowAlertDialog(title = "Delete",
                        text = "Are you sure want to delete this address?",
                        confirmButtonText = "Delete",
                        dismissButtonText = "Cancel",
                        onClick = { isPositive ->

                            showDeleteAlertDialog = false

                            if (isPositive) {
                                val delete = AddressBook(
                                    id = addressBook?.id,
                                    name = name,
                                    email = email,
                                    phoneNumber = phoneNumber,
                                    isActive = addressBook?.isActive
                                )
                                addressViewModel.deleteAddressBook(delete).also {
                                    finish()
                                }
                            }
                        })
                }
            }
        }
    }

    @Composable
    private fun SetTopAppBar() {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Text(
                text = addressBook?.name ?: "New Address", color = white
            )
        }, navigationIcon = {
            IconButton(onClick = {
                onBackPressedDispatcher.onBackPressed()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = white
                )
            }
        })
    }

    @Composable
    private fun SetTextField(
        value: String,
        label: String,
        isError: Boolean,
        imeAction: ImeAction,
        keyboardType: KeyboardType,
        onValueChangeListener: (value: String) -> Unit
    ) {
        TextField(modifier = Modifier.fillMaxWidth(0.87f),
            value = value,
            label = { Text(text = label) },
            onValueChange = { onValueChangeListener.invoke(it) },
            colors = fieldColors(),
            shape = RoundedCornerShape(percent = 16),
            singleLine = true,
            isError = isError,
            keyboardActions = KeyboardActions(onDone = { localFocusManager?.clearFocus() }),
            keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType)
        )
    }

    @Composable
    private fun SetButton(
        text: String, backgroundColor: Color = MaterialTheme.colors.primary, onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.87f)
                .clip(RoundedCornerShape(16))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16),
            backgroundColor = backgroundColor
        ) {
            Text(
                modifier = Modifier.padding(16.dp), text = text, style = TextStyle(
                    color = white
                ), textAlign = TextAlign.Center
            )
        }
    }

    /**
     * Check all field is valid or not.
     */
    private fun isEntriesValid(name: String?, email: String?, phoneNumber: String?): Boolean {
        return Validation(
            context = this, name = name, email = email, phoneNumber = phoneNumber
        ).run {
            isNameValid().also {
                nameError = !it
            } && isEmailValid().also {
                emailError = !it
            } && isPhoneNumberValid().also {
                phoneNumberError = !it
            } && isAnyChangeAvailable(name = name, email = email, phoneNumber = phoneNumber)
        }
    }

    /**
     * Check is there any change available in fields.
     */
    private fun isAnyChangeAvailable(name: String?, email: String?, phoneNumber: String?): Boolean {
        if (name?.equals(addressBook?.name) == true && email?.equals(addressBook?.email) == true && phoneNumber?.equals(
                addressBook?.phoneNumber
            ) == true
        ) {
            toast(this, "There is a no change for update")
            return false
        }
        return true
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AddressBookJetpackComposeTheme(darkTheme = false) {
            InitUI()
        }
    }
}