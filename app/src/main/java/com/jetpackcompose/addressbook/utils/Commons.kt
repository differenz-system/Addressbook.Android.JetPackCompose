package com.jetpackcompose.addressbook.utils

import android.content.Context
import android.widget.Toast

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.lifecycle.MutableLiveData

fun <E> mutableListWithSize(size: Int = 0): MutableList<E?> {
    return MutableList(size) {
        null
    }
}

fun <E> mutableLiveDataWithSize(size: Int = 0): MutableLiveData<ArrayList<E?>> {
    return MutableLiveData<ArrayList<E?>>().apply {
        value?.apply {
            List(size) { index ->
                set(index, null)
            }
        }
    }
}

fun <E> mutableStateListWithSize(size: Int = 0): SnapshotStateList<E?> {
    return mutableStateListOf<E?>().apply {
        List(size) { index ->
            set(index, null)
        }
    }
}

/**
 * Show alert.
 */
@Composable
fun ShowAlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onClick: (isPositive: Boolean) -> Unit
) {
    AlertDialog(title = {
        Text(text = title, fontWeight = FontWeight.Bold)
    }, text = {
        Text(text = text)
    }, onDismissRequest = {
        onClick(false)
    }, confirmButton = {
        TextButton(
            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
            onClick = { onClick(true) }) {
            Text(text = confirmButtonText, color = MaterialTheme.colors.primary)
        }
    }, dismissButton = {
        TextButton(
            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
            onClick = { onClick(false) }) {
            Text(text = dismissButtonText, color = MaterialTheme.colors.primary)
        }
    })
}

/**
 * Global level toast.
 */
fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}