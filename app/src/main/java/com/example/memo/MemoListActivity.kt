package com.example.memo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MemoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_list)
        this.actionBar?.title = "メモ一覧だよ"
        setRecyclerView()
        getMemoData()


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
        var mainAdapter: MemoAdapter? = null

        val recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
        // RecyclerViewのレイアウトサイズを変更しない設定をONにする
        // パフォーマンス向上のための設定。
        recyclerView.setHasFixedSize(true)
        // RecyclerViewにlayoutManagerをセットする。
        // このlayoutManagerの種類によって「1列のリスト」なのか「２列のリスト」とかが選べる。
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        // Adapter生成してRecyclerViewにセット
//        mainAdapter = MemoAdapter(createRowData(page))

        val database =
            Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
                .build()

        // データを保存
        GlobalScope.launch(Dispatchers.IO) { // 非同期処理
            val dataSet: MutableList<RowData> = ArrayList()
            val memoList = database.memoDao().getAllMemo()
            for (memo in memoList) {
                val data = RowData()
                data.memoTitle = memo.title
                dataSet.add(data)
            }
            GlobalScope.launch(Dispatchers.Main) {  // main thread
                mainAdapter = MemoAdapter(dataSet)
                recyclerView.adapter = mainAdapter
                mainAdapter!!.setOnItemClickListener(View.OnClickListener {
                    val intent = Intent(this@MemoListActivity, MemoActivity::class.java)
                    intent.putExtra("memo",memoList[0].title)
                    startActivity(intent)
                })
            }
        }
    }

    /**
     * 一行分のデータ
     */
    inner class RowData {
        var memoTitle: String? = null
    }

    private fun getMemoData() {
//        val database =
//            Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
//                .build()
//        // データを保存
//        GlobalScope.launch(Dispatchers.IO) { // 非同期処理
//            val data = database.memoDao().getAllMemo()
//            Log.v("TAG", "after insert ${database.memoDao().getAllMemo()}")
//            GlobalScope.launch(Dispatchers.Main) {  // main thread
//                Toast.makeText(applicationContext, "メモ取得しました", Toast.LENGTH_SHORT).show()
//            }
//            Log.v("TAG", "data: ${data.size}")
//            for (a in data){
//                a.title
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setRecyclerView()
    }

//    override fun onPause() {
//        super.onPause()
//        setRecyclerView()
//    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()

    }
}