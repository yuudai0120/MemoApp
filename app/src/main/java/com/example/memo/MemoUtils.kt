package com.example.memo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*

class MemoUtils {
    companion object {
        val DIALOG_ID_OVERRAPPING = 0
        val DIALOG_ID_EMPTY = 1

        /**
         * ダイアログ生成
         * @param id
         * @return
         */
        fun createDialog(context: Context,id: Int): Dialog? {
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
}