package com.jetpackcompose.addressbook.repositories

import androidx.lifecycle.LiveData
import com.jetpackcompose.addressbook.roomdb.AddressBook
import com.jetpackcompose.addressbook.roomdb.AddressBookDatabase

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
class AddressBookRepository(private val addressBookDatabase: AddressBookDatabase) {

    private var list :LiveData<List<AddressBook>> = addressBookDatabase.getAddressBookDao().getAllAddressBook()

    suspend fun insertAddressBook(addressBook: AddressBook) = addressBookDatabase.getAddressBookDao().insertAddressBook(addressBook)

    suspend fun updateAddressBook(addressBook: AddressBook) = addressBookDatabase.getAddressBookDao().updateAddressBook(addressBook)

    suspend fun deleteAddressBook(addressBook: AddressBook) = addressBookDatabase.getAddressBookDao().deleteAddressBook(addressBook)

    suspend fun clearAddress() = addressBookDatabase.getAddressBookDao().clearAddress()

    suspend fun deleteAddressBookById(id:Int) = addressBookDatabase.getAddressBookDao().deleteAddressBook(id.toLong())

    fun getAllAddress() : LiveData<List<AddressBook>> = list
}