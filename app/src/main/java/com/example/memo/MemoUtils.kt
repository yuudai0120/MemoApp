package com.example.memo

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*

class MemoUtils {
    companion object {
        const val DIALOG_ID_ALREADY = 0
        const val DIALOG_ID_EMPTY = 1
        const val DIALOG_ID_LIMIT = 2

        /** メモの上限数 */
        const val MEMO_LIMIT = 100

        /**
         * ダイアログ生成
         * @param id
         * @return
         */
        fun createDialog(context: Context, id: Int) {
            var title= ""
            var message = ""

            when (id) {
                DIALOG_ID_ALREADY -> {
                    title = context.getString(R.string.confirmation)
                    message = context.getString(R.string.memo_title_duplicate_message)
                }
                DIALOG_ID_EMPTY -> {
                    title = context.getString(R.string.illegal_message_title)
                    message = context.getString(R.string.illegal_message)
                }
                DIALOG_ID_LIMIT -> {
                    title = context.getString(R.string.confirmation)
                    message = context.getString(R.string.limit_message)
                }
            }
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                }
                .show()
        }

        fun getMemoList(context: Context): List<Memo> {
            return runBlocking(Dispatchers.IO) {
                DatabaseUtils.getAllMemo(context)
            }
        }

        fun memoInsert(context: Context, memo: Memo) = runBlocking() {
            DatabaseUtils.memoInsert(context, memo)
            Toast.makeText(context, context.getString(R.string.save_message), Toast.LENGTH_SHORT).show()
        }

        fun memoDelete(context: Context, deleteMemo: Memo) = runBlocking() {
            DatabaseUtils.memoDelete(context, deleteMemo)
                Toast.makeText(context, context.getString(R.string.delete_message), Toast.LENGTH_SHORT).show()
        }

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

        fun checkMemoListLimit(context: Context): Boolean {
            if (getMemoList(context).size < MEMO_LIMIT ) {
                return true
            }
            return false
        }
    }
}