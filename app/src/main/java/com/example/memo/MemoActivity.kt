package com.example.memo

import android.app.AlertDialog
import android.app.Dialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class MemoActivity : AppCompatActivity() {

    private val DIALOG_ID_OVERRAPPING = 0
    private val DIALOG_ID_EMPTY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo)

        this.actionBar?.title = "メモ作成"


        // Todo メモの情報を取得し反映する
        val title = findViewById<EditText>(R.id.memo_title_edit)
        val memoData = intent.getStringExtra("memo")
        title.setText(memoData, TextView.BufferType.NORMAL);
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
                saveMemoList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveMemoList() {
        AlertDialog.Builder(this)
            .setTitle("保存")
            .setMessage("入力した内容で保存しますか？")
            .setPositiveButton("OK") { dialog, which ->
                val title = findViewById<EditText>(R.id.memo_title_edit)
                val body = findViewById<EditText>(R.id.memo_body_edit)
                //バリデーションチェックの結果
                val check = validationCheck(title, body)
                if (check) {
                    insertMemoData()
                    finish()
                } else {
                    createDialog(DIALOG_ID_EMPTY)
                }


            }
            .setNegativeButton("キャンセル") { dialog, which -> }
            .show()
    }

    private fun insertMemoData() {
        val database =
            Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
                .build()
        // データモデルを作成
        val id = Random.nextInt(1000)
        val title = findViewById<EditText>(R.id.memo_title_edit)
        val body = findViewById<EditText>(R.id.memo_body_edit)

        val memo = Memo(id, title.text.toString(), body.text.toString())
        // データを保存
        GlobalScope.launch(Dispatchers.IO) { // 非同期処理
            database.memoDao().insert(memo)
            Log.v("TAG", "after insert ${database.memoDao().getAllMemo()}")
            GlobalScope.launch(Dispatchers.Main) {  // main thread
                Toast.makeText(applicationContext, "メモ保存をしました", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //バリデーションチェックするためのメソッド
    private fun validationCheck(title: EditText, body: EditText): Boolean {
        //タイトルの入力値がない場合
        if (title.text.toString().isEmpty()) {
            //画面の下にToastエラーメッセージを表示
            Toast.makeText(applicationContext, "メモのタイトルを入力してください。", Toast.LENGTH_SHORT).show()
            return false
        }
//        GlobalScope.launch(Dispatchers.IO) {
//            val memoList = create()
//            for (memo in memoList) {
//                if (memo.title == title.text.toString()) {
//                    createDialog(DIALOG_ID_OVERRAPPING)
//                }
//            }
//        }


//        val database =
//            Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
//                .build()
//        GlobalScope.launch(Dispatchers.IO) { // 非同期処理
//            val memoList = database.memoDao().getAllMemo()
//            for (memo in memoList) {
//                if (memo.title == title.text.toString()) {
//                    GlobalScope.launch(Dispatchers.Main) {  // main thread
//                        createDialog(DIALOG_ID_OVERRAPPING)
//                    }
//                }
//            }
//        }

        return true
    }

    /**
     * ダイアログ生成
     * @param id
     * @return
     */
    private fun createDialog(id: Int): Dialog? {
        if (id == DIALOG_ID_OVERRAPPING) {
            AlertDialog.Builder(this@MemoActivity)
                .setTitle("不正な入力です。")
                .setMessage("再度正しく入力してください。")
                .setPositiveButton("OK") { dialog, which ->
                    Toast.makeText(applicationContext, "同じタイトルのメモが存在しています。", Toast.LENGTH_SHORT)
                        .show()
                }
                .show()
        }
        if (id == DIALOG_ID_EMPTY) {
            AlertDialog.Builder(this)
                .setTitle("不正な入力です。")
                .setMessage("再度正しく入力してください。")
                .setPositiveButton("OK") { dialog, which ->
                }
                .show()
        }
        return null
    }


//    suspend fun create(): List<Memo> {
//        return withContext(Dispatchers.IO) {
//            val database =
//                Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
//                    .build()
//            val dao = database.memoDao()
//            dao.getAllMemo()
//        }
//    }


}

