package com.example.memo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*

class MemoUtils {
    companion object {
        const val DIALOG_ID_OVERRAPPING = 0
        const val DIALOG_ID_EMPTY = 1

        /**
         * ダイアログ生成
         * @param id
         * @return
         */
        fun createDialog(context: Context, id: Int): Dialog? {
            if (id == DIALOG_ID_OVERRAPPING) {
                AlertDialog.Builder(context)
                    .setTitle("不正な入力です。")
                    .setMessage("再度正しく入力してください。")
                    .setPositiveButton("OK") { dialog, which ->
                        Toast.makeText(context, "同じタイトルのメモが存在しています。", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .show()
            }
            if (id == DIALOG_ID_EMPTY) {
                AlertDialog.Builder(context)
                    .setTitle("不正な入力です。")
                    .setMessage("再度正しく入力してください。")
                    .setPositiveButton("OK") { dialog, which ->
                    }
                    .show()
            }
            return null
        }

//        fun getMemoList(context: Context): Deferred<List<Memo>> =
//            GlobalScope.async(Dispatchers.Default) {
//                DatabaseUtils.getAllMemo(context)
//            }

        fun getMemoList(context: Context): List<Memo> {
            return runBlocking(Dispatchers.IO) {
                DatabaseUtils.getAllMemo(context)
            }
        }

        fun memoInsert(context: Context, memo: Memo) = runBlocking() {
            DatabaseUtils.memoInsert(context, memo)
            GlobalScope.launch(Dispatchers.Main) {  // main thread
                Toast.makeText(context, "メモを保存しました", Toast.LENGTH_SHORT).show()
            }
        }

        fun memoDelete(context: Context, deleteMemo: Memo) = runBlocking() {
            DatabaseUtils.memoDelete(context, deleteMemo)
            GlobalScope.launch(Dispatchers.Main) {  // main thread
                Toast.makeText(context, "メモを削除しました", Toast.LENGTH_SHORT).show()
            }
        }


//        fun createId(context: Context): Deferred<Int> =
//            GlobalScope.async(Dispatchers.Default) {
//                var memoId = 1
//                val memoIdRange: IntRange = 1..100
//                val memoList = getMemoList(context).await()
//                val memoIdList: MutableList<Int> = ArrayList()
//                for (memo in memoList) {
//                    memoIdList.add(memo.id)
//                }
//
//                for (i in memoIdRange) {
//                    if (!memoIdList.contains(i)) {
//                        memoId = i
//                    }
//                }
//                memoId
//            }

        fun createId(context: Context): Int {
            return runBlocking(Dispatchers.IO) {
                var memoId = 1
                val memoIdRange: IntRange = 1..100
                val memoList = getMemoList(context)
                val memoIdList: MutableList<Int> = ArrayList()
                for (memo in memoList) {
                    memoIdList.add(memo.id)
                }

                for (i in memoIdRange) {
                    if (!memoIdList.contains(i)) {
                        memoId = i
                    }
                }
                memoId
            }
        }
    }

//        fun main(context: Context,) = runBlocking() {
////            val memoList = async {
////                DatabaseUtils.getMemoList(context)
////            }.await()
//            val memoList = DatabaseUtils.getMemoList(context).await()
//            for (memo in memoList) {
//                // タイトルが重複しているかどうか
//                if (memo.title == title.text.toString()) {
//                    GlobalScope.launch(Dispatchers.Main) {  // main thread
//                        // Todo ダイアログ出す(だすとクラッシュする。)
//                        createDialog(DIALOG_ID_OVERRAPPING)
//                        Toast.makeText(applicationContext, "メモタイトルが重複しています", Toast.LENGTH_SHORT)
//                            .show()
//                        boolean = false
//                    }
//                }
//            }
//        }
}
