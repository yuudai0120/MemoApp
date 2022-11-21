package com.example.memo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memo::class], version = 1, exportSchema = false) // Kotlin 1.2からは arrayOf(Memo::class)の代わりに[Memo::class]と書ける
abstract class MemoDatabase : RoomDatabase() {

    // DAOを取得する。
    abstract fun memoDao(): MemoDao
}