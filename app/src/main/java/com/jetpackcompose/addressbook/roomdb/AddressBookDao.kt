package com.jetpackcompose.addressbook.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
@Dao
interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddressBook(addressBook: AddressBook)

    @Update
    suspend fun updateAddressBook(addressBook: AddressBook)

    @Delete
    suspend fun deleteAddressBook(addressBook: AddressBook)

    @Query("SELECT * FROM AddressBookTable ORDER BY name")
    fun getAllAddressBook() :LiveData<List<AddressBook>>

    @Query("DELETE FROM AddressBookTable")
    suspend fun clearAddress()

    @Query("DELETE FROM AddressBookTable WHERE id=:id")
    suspend fun deleteAddressBook(id: Long)

}