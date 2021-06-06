package com.example.memo

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo)

        this.actionBar?.title = "メモ作成"


        // Todo メモの情報を取得し反映する
        val title = findViewById<EditText>(R.id.memo_title_edit)
        val memoData = intent.getStringExtra("memo")
        title.setText(memoData, TextView.BufferType.NORMAL);
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.add_memo -> {
//                backMemoList()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun backMemoList() {
        finish()
    }


}

