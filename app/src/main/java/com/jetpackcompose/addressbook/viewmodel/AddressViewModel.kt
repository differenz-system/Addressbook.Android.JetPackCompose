package com.jetpackcompose.addressbook.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.addressbook.repositories.AddressBookRepository
import com.jetpackcompose.addressbook.roomdb.AddressBook
import com.jetpackcompose.addressbook.roomdb.AddressBookDatabase
import com.jetpackcompose.addressbook.ui.theme.isInspectionMode
import kotlinx.coroutines.launch

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class AddressViewModel(context: Context, private val lifecycleOwner: LifecycleOwner) : ViewModel() {

    private lateinit var repository: AddressBookRepository
    private lateinit var database: AddressBookDatabase

    var mutableAddress = mutableStateListOf<AddressBook>()
    var isAddressesLoaded by mutableStateOf(value = isInspectionMode)

    init {
        //initialization can throw error if default preview mode is enabled,
        //to handle put into isInspectionMode condition
        if (!isInspectionMode) {
            database = AddressBookDatabase(context = context)
            repository = AddressBookRepository(addressBookDatabase = database)
        }
    }

    /**
     * Insert new address into [AddressBookDatabase].
     */
    fun insertAddressBook(addressBook: AddressBook) {
        viewModelScope.launch {
            repository.insertAddressBook(addressBook = addressBook)
        }
    }

    /**
     * Update exist address into [AddressBookDatabase].
     */
    fun updateAddressBook(addressBook: AddressBook) {
        viewModelScope.launch {
            repository.updateAddressBook(addressBook = addressBook)
        }
    }

    /**
     * Delete address from [AddressBookDatabase].
     */
    fun deleteAddressBook(addressBook: AddressBook) {
        viewModelScope.launch {
            repository.deleteAddressBook(addressBook = addressBook)
        }
    }

    /**
     * Delete address from [AddressBookDatabase].
     *
     * @param id A unique value for each address which stored in [AddressBookDatabase].
     */
    fun deleteAddressBookById(id: Int) {
        viewModelScope.launch {
            repository.deleteAddressBookById(id = id)
        }
    }

    /**
     * Clear all stored addresses from [AddressBookDatabase].
     */
    fun clearAddresses() {
        viewModelScope.launch {
            repository.clearAddress()
        }
    }

    /**
     * Retrieve all stored addresses from [AddressBookDatabase].
     */
    fun getAllAddresses() {
        repository.getAllAddress().observe(lifecycleOwner) {
            mutableAddress.clear()
            mutableAddress.addAll(it)
            isAddressesLoaded = true
        }
    }

    /**
     * Clear all addresses and Close [AddressBookDatabase] before exist from app.
     */
    fun clearAndCloseDatabase() {
        clearAddresses()
        closeDatabase()
    }

    /**
     * Close [AddressBookDatabase].
     */
    fun closeDatabase() {
        AddressBookDatabase.close()
    }
}