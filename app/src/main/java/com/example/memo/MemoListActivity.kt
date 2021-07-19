package com.example.memo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


class MemoListActivity : AppCompatActivity() {

    var mainAdapter: MemoAdapter? = null
    private var memoList: List<Memo>? = null

    /**
     * メニューレイアウト管理用変数
     */
    var mMenuType = 1

    /* 通常時に表示するメニューを表す */
    private val MENU_STANDARD_MODE = 1

    /* 選択モード時に表示するメニューを表す */
    private val MENU_SELECT_MODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_list)
        title = "メモ一覧"
        setRecyclerView()
        getMemoData()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        val inflater: MenuInflater = menuInflater
////        inflater.inflate(R.menu.menu, menu)
////        return true
//        // メニューレイアウト管理用変数が切り替わる度にメニューのレイアウトを切り替える
//        when (mMenuType) {
//            MENU_STANDARD_MODE -> menuInflater.inflate(R.menu.menu, menu)
//            MENU_SELECT_MODE -> menuInflater.inflate(R.menu.menu_select_mode, menu)
//        }
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (mMenuType) {
            MENU_STANDARD_MODE -> menuInflater.inflate(R.menu.menu, menu)
            MENU_SELECT_MODE -> menuInflater.inflate(R.menu.menu_select_mode, menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.add_memo -> {
                addMemo()
                true
            }
            R.id.delete_memo -> {
                val a = mainAdapter!!.getSelectedItemPositions()
                val memoTitleList: MutableList<String> = ArrayList()
                for (x in a) {
                    memoTitleList.add(memoList?.get(x).toString())
                }
                Toast.makeText(
                    applicationContext,
                    memoTitleList.joinToString(separator = "\n"),
                    Toast.LENGTH_SHORT
                ).show()
                deleteMemoList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addMemo() {
        // 起動する対象をクラスオブジェクトで指定する
        val intent = Intent(this@MemoListActivity, MemoActivity::class.java)
        startActivity(intent)
    }

    fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
        // RecyclerViewのレイアウトサイズを変更しない設定をONにする
        // パフォーマンス向上のための設定。
        recyclerView.setHasFixedSize(true)
        // RecyclerViewにlayoutManagerをセットする。
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
            memoList = database.memoDao().getAllMemo()
            for (memo in memoList!!) {
                val data = RowData()
                data.memoTitle = memo.title
                dataSet.add(data)
            }

            GlobalScope.launch(Dispatchers.Main) {  // main thread
                mainAdapter = MemoAdapter(applicationContext, dataSet, false)
                recyclerView.adapter = mainAdapter
                mainAdapter!!.setOnItemClickListener(View.OnClickListener {

                    if (mainAdapter!!.getSelectedItemPositions().isNotEmpty()) {
                        mMenuType = 2
                        invalidateOptionsMenu()
                    } else {
                        mMenuType = 1
                        invalidateOptionsMenu()
                    }

                    if (mainAdapter!!.getModeStatus().contains(2)) {
                        val intent = Intent(this@MemoListActivity, MemoActivity::class.java)
                        val memoListData =
                            memoList!![mainAdapter!!.getClickItemPositions()].title + "\n" + memoList!![mainAdapter!!.getClickItemPositions()].body
                        intent.putExtra(
                            "memo",
                            memoListData
                        )
                        startActivity(intent)
                    }
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


    private fun deleteMemoList() {
        AlertDialog.Builder(this)
            .setTitle("削除")
            .setMessage("選択したメモを削除しますか？ " + "\n" + "※この操作は取り消せません。")
            .setPositiveButton("OK") { dialog, which ->
                deleteMemoData()
                setRecyclerView()
            }
            .setNegativeButton("キャンセル") { dialog, which -> }
            .show()
    }

    private fun deleteMemoData() {
        val database =
            Room.databaseBuilder(applicationContext, MemoDatabase::class.java, "database-name")
                .build()
        // 削除するデータリストを作成
        val deleteMemoList: MutableList<Memo> = ArrayList()
        // 削除するpositionのリストを作成
        val deletePosition = mainAdapter!!.getSelectedItemPositions()
        for (position in deletePosition) {
            memoList?.get(position)?.let { deleteMemoList.add(it) }
        }
        // データを保存
        GlobalScope.launch(Dispatchers.IO) { // 非同期処理
//            for (x in a){
//                database.memoDao().selectDelete(memoList?.get(x).toString())
//            }
//            for (memo in memoList!!){
//                database.memoDao().delete(memo)
//            }
            for (deletMemo in deleteMemoList) {
                database.memoDao().delete(deletMemo)
            }

            Log.v("TAG", "after delete ${database.memoDao().getAllMemo()}")
            GlobalScope.launch(Dispatchers.Main) {  // main thread
                Toast.makeText(applicationContext, "メモを削除しました", Toast.LENGTH_SHORT).show()
                mMenuType = 1
                invalidateOptionsMenu()
            }
        }
    }
}