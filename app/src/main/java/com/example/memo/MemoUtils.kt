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
            if (id == DIALOG_ID_ALREADY) {
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.confirmation))
                    .setMessage(context.getString(R.string.memo_title_duplicate_message))
                    .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                    }
                    .show()
            }
            if (id == DIALOG_ID_EMPTY) {
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.illegal_message_title))
                    .setMessage(context.getString(R.string.illegal_message))
                    .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                    }
                    .show()
            }
            if (id == DIALOG_ID_LIMIT) {
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.confirmation))
                    .setMessage(context.getString(R.string.limit_message))
                    .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                    }
                    .show()
            }
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

        fun checkMemo(context: Context): Boolean {
            val memoList = getMemoList(context)
            if (memoList.size < MEMO_LIMIT ) {
                return true
            }
            return false
        }
    }
}