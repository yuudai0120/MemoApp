package com.example.memo

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*

class MemoActivity : AppCompatActivity() {

    private var isNewMemo = true
    private var updateMemo = Memo(0, "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo)

        title = getString(R.string.memo_create)

        val memoTitle = findViewById<EditText>(R.id.memo_title_edit)
        val memoBody = findViewById<EditText>(R.id.memo_body_edit)
        val memoData = intent.getStringExtra("memo")
        if (!memoData.isNullOrEmpty()) {
            isNewMemo = false
            updateMemo =
                Memo(
                    memoData.split("\n")[0].toInt(),
                    memoData.split("\n")[1],
                    memoData.split("\n")[2]
                )
            memoTitle.setText(memoData.split("\n")[1], TextView.BufferType.NORMAL)
            memoBody.setText(memoData.split("\n")[2], TextView.BufferType.NORMAL)
            title = getString(R.string.memo_detail)
        } else {
            isNewMemo = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.save_memo -> {
                if (isNewMemo) saveMemoList() else updateMemoList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateMemoList() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.update))
            .setMessage(getString(R.string.update_confirmation))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                val title = findViewById<EditText>(R.id.memo_title_edit)
                //バリデーションチェックの結果
                val check = validationCheck(title)
                if (check) {
                    updateMemoData()
                    finish()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
            .show()
    }

    private fun updateMemoData() {
        val database =
            Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
                .build()
        val title = findViewById<EditText>(R.id.memo_title_edit)
        val body = findViewById<EditText>(R.id.memo_body_edit)
        updateMemo.title = title.text.toString()
        updateMemo.body = body.text.toString()

        // データを更新
        GlobalScope.launch(Dispatchers.IO) { // 非同期処理
            database.memoDao().update(updateMemo)

            GlobalScope.launch(Dispatchers.Main) {  // main thread
                Toast.makeText(applicationContext, getString(R.string.update_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveMemoList() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.save))
            .setMessage(getString(R.string.save_confirmation))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                val title = findViewById<EditText>(R.id.memo_title_edit)
                //バリデーションチェックの結果
                val check = validationCheck(title)
                if (check) {
                    insertMemoData()
                    finish()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
            .show()
    }

    private fun insertMemoData() {
        // IDを作成
        val id = MemoUtils.createId(applicationContext)
        val title = findViewById<EditText>(R.id.memo_title_edit)
        val body = findViewById<EditText>(R.id.memo_body_edit)
        val memo = Memo(id, title.text.toString(), body.text.toString())
        // データを保存
        MemoUtils.memoInsert(applicationContext, memo)
    }

    //バリデーションチェックするためのメソッド
    private fun validationCheck(title: EditText): Boolean {
        var boolean = true
        // タイトルの入力値がない場合
        if (title.text.toString().isEmpty()) {
            // 画面の下にToastエラーメッセージを表示
            Toast.makeText(applicationContext, getString(R.string.non_memo_title_message), Toast.LENGTH_SHORT).show()
            return false
        }
        val memoList = MemoUtils.getMemoList(applicationContext)
        for (memo in memoList) {
            // タイトルが重複しているかどうか
            if (memo.title == title.text.toString()) {
                MemoUtils.createDialog(this, MemoUtils.DIALOG_ID_ALREADY)
                boolean = false
            }
        }
        return boolean
    }
}

