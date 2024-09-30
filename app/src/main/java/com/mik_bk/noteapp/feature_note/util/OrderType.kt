package com.mik_bk.noteapp.feature_note.util

sealed class OrderType {
    data object  Ascending: OrderType()
    data object Descending: OrderType()
}