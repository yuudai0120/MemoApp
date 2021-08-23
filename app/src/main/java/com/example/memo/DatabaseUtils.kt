package com.example.memo

import android.content.Context
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

        fun getMemoList(context: Context): Deferred<List<Memo>> = GlobalScope.async(Dispatchers.Default) {
            getAllMemo(context)
        }


//
//        fun createId(context: Context): Int {
//            GlobalScope.launch(Dispatchers.IO) { // 非同期処理
//                val memoId = 0
//                val memoIdRange: IntRange = 1..100
//                val memoList = getMemoList(context)
//                for (i in memoIdRange){
//                    for(memo in memoList) {
//                        if(memo) {
//                            return
//                        }
//                    }
//                }
//            }
//
//            return 1
//        }

    }
}