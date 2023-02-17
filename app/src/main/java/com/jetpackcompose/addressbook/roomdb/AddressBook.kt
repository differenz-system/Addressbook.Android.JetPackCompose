package com.jetpackcompose.addressbook.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
@Entity(tableName = "AddressBookTable")
data class AddressBook(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long? = null,

    @ColumnInfo(name = "name") var name: String? = null,

    @ColumnInfo(name = "email") var email: String? = null,

    @ColumnInfo(name = "phoneNumber") var phoneNumber: String? = null,

    @ColumnInfo(name = "isActive") var isActive: Boolean? = null,

) : Serializable {
    constructor(name: String, email: String, phoneNumber: String) : this(
        name = name, email = email, phoneNumber = phoneNumber, isActive = true
    )
}