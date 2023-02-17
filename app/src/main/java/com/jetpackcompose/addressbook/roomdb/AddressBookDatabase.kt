package com.jetpackcompose.addressbook.roomdb

import android.content.Context
import androidx.room.*
import com.jetpackcompose.addressbook.utils.Credentials

/**
 * 24th Jan 2023.
 *
 * @since 1.0.
 */
@Database(
    entities = [AddressBook::class], version = 1, exportSchema = true
)
abstract class AddressBookDatabase : RoomDatabase() {

    abstract fun getAddressBookDao(): AddressBookDao

    companion object {

        private const val DB_NAME = "addressbook.db"

        @Volatile
        private var instance: AddressBookDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        /**
         * Build new instance of database.
         */
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AddressBookDatabase::class.java,
            Credentials(context).getEmail() ?: DB_NAME
        ).build()

        /**
         * Close opened database.
         */
        fun close() {
            instance?.close()
            instance = null
        }
    }
}