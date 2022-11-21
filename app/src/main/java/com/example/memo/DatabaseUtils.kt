package com.example.memo

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.*

class DatabaseUtils {

    companion object {

        val dataBaseName = "database-name"

        // DBからメモリストを取得する
        suspend fun getAllMemo(context: Context) : List<Memo>{
            val database =
                Room.databaseBuilder(context, MemoDatabase::class.java, dataBaseName)
                    .build()
            return database.memoDao().getAllMemo()
        }

        // メモ情報をDBにインサートする
        suspend fun memoInsert(context: Context, memo:Memo) {
            val database =
                Room.databaseBuilder(context, MemoDatabase::class.java, dataBaseName)
                    .build()
            database.memoDao().insert(memo)
        }

        // メモ情報をDBから削除する
        suspend fun memoDelete(context: Context, memo:Memo) {
            val database =
                Room.databaseBuilder(context, MemoDatabase::class.java, dataBaseName)
                    .build()
            database.memoDao().delete(memo)
        }
    }
}