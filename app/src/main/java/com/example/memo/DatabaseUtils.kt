package com.example.memo

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.*

class DatabaseUtils {

    companion object {

        // DBからメモリストを取得する
        suspend fun getAllMemo(context: Context) : List<Memo>{
            val database =
                Room.databaseBuilder(context, MemoDatabase::class.java, "database-name")
                    .build()
            return database.memoDao().getAllMemo()
        }

        // メモ情報をDBにインサートする
        suspend fun memoInsert(context: Context, memo:Memo) {
            val database =
                Room.databaseBuilder(context, MemoDatabase::class.java, "database-name")
                    .build()
            database.memoDao().insert(memo)
            Log.v("TAG", "after insert ${MemoUtils.getMemoList(context)}")
        }

        // メモ情報をDBから削除する
        suspend fun memoDelete(context: Context, memo:Memo) {
            val database =
                Room.databaseBuilder(context, MemoDatabase::class.java, "database-name")
                    .build()
            database.memoDao().delete(memo)
        }
    }
}