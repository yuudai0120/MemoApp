package com.example.memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room


class MemoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_list)
        this.actionBar?.title = "メモ一覧"
        setRecyclerView()
        // 永続データベースを作成
        val db = Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name").build()

//        val memo = Memo()
//        memo.id = Random().nextInt()
//        memo.title = "タイトル"
//        memo.body = "内容"
//
//        thread {
//            db.memoDao().insert(memo)
//        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.add_memo -> {
                addMemo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addMemo() {
        // 起動する対象をクラスオブジェクトで指定する
        val intent = Intent(this, MemoActivity::class.java)
        startActivity(intent)
    }

    fun setRecyclerView() {
        var page = 1
        var mainAdapter: MemoAdapter? = null

        val recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
        // RecyclerViewのレイアウトサイズを変更しない設定をONにする
        // パフォーマンス向上のための設定。
        recyclerView.setHasFixedSize(true)
        // RecyclerViewにlayoutManagerをセットする。
        // このlayoutManagerの種類によって「1列のリスト」なのか「２列のリスト」とかが選べる。
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Adapter生成してRecyclerViewにセット
        mainAdapter = MemoAdapter(createRowData(page))
        recyclerView.adapter = mainAdapter

        mainAdapter.setOnItemClickListener(View.OnClickListener {
            val intent = Intent(this@MemoListActivity, MemoActivity::class.java)
            intent.putExtra("memo",mainAdapter.toString())
            startActivity(intent)
        })
    }


    /**
     * 20行追加する
     */
    private fun createRowData(page: Int): List<RowData> {
        val dataSet: MutableList<RowData> = ArrayList()
        var i = 1
        while (i < page * 20) {
            val data = RowData()
            data.memoTitle = "Title" + Integer.toString(i)
            dataSet.add(data)
            i += 1
        }
        
        return dataSet
    }

    /**
     * 一行分のデータ
     */
    inner class RowData {
        var memoTitle: String? = null
    }
}