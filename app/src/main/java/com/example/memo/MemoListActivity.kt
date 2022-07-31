package com.example.memo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MemoListActivity : AppCompatActivity() {

    var mainAdapter: MemoAdapter? = null
    private var memoList: List<Memo> = ArrayList()

    /**
     * メニューレイアウト管理用変数
     */
    var mMenuType = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_list)
        title = getString(R.string.memo_list)
        setRecyclerView()
    }

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
                    memoTitleList.add(memoList[x].toString())
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
        val check = MemoUtils.checkMemo(this)
        if (check) {
            // 起動する対象をクラスオブジェクトで指定する
            val intent = Intent(this@MemoListActivity, MemoActivity::class.java)
            startActivity(intent)
        } else {
            MemoUtils.createDialog(this, MemoUtils.DIALOG_ID_LIMIT)
        }
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

        // データを保存
        val dataSet: MutableList<RowData> = ArrayList()
        memoList = MemoUtils.getMemoList(applicationContext)
        if (memoList.isEmpty()) {
            val nonMemoText = findViewById<TextView>(R.id.non_memo)
            nonMemoText.visibility = View.VISIBLE
        } else {
            val nonMemoText = findViewById<TextView>(R.id.non_memo)
            nonMemoText.visibility = View.GONE
        }

        for (memo in memoList) {
            val data = RowData()
            data.memoTitle = memo.title
            dataSet.add(data)
        }

        mainAdapter = MemoAdapter(applicationContext, dataSet, false)
        recyclerView.adapter = mainAdapter
        mainAdapter!!.setOnItemClickListener(View.OnClickListener {

            if (mainAdapter!!.getSelectedItemPositions().isNotEmpty()) {
                mMenuType = MENU_SELECT_MODE
                invalidateOptionsMenu()
            } else {
                mMenuType = MENU_STANDARD_MODE
                invalidateOptionsMenu()
            }

            if (mainAdapter!!.getModeStatus().contains(2)) {
                val intent = Intent(this@MemoListActivity, MemoActivity::class.java)
                val memoListData =
                    memoList[mainAdapter!!.getClickItemPositions()].id.toString() + "\n" +
                            memoList[mainAdapter!!.getClickItemPositions()].title + "\n" +
                            memoList[mainAdapter!!.getClickItemPositions()].body
                intent.putExtra(
                    "memo",
                    memoListData
                )
                startActivity(intent)
            }
        })
    }

    /**
     * 一行分のデータ
     */
    inner class RowData {
        var memoTitle: String? = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()

    }


    private fun deleteMemoList() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_confirmation))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                deleteMemoData()
                setRecyclerView()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
            .show()
    }

    private fun deleteMemoData() {
        // 削除するデータリストを作成
        val deleteMemoList: MutableList<Memo> = ArrayList()
        // 削除するpositionのリストを作成
        val deletePosition = mainAdapter!!.getSelectedItemPositions()
        for (position in deletePosition) {
            memoList[position].let { deleteMemoList.add(it) }
        }
        // データを保存
        for (deleteMemo in deleteMemoList) {
            MemoUtils.memoDelete(this, deleteMemo)
        }
        mMenuType = 1
        invalidateOptionsMenu()
    }

    companion object {
        /* 通常時に表示するメニューを表す */
        private const val MENU_STANDARD_MODE = 1
        /* 選択モード時に表示するメニューを表す */
        private const val MENU_SELECT_MODE = 2
    }
}