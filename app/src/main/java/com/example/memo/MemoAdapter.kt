package com.example.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MemoAdapter internal constructor(private var rowDataList: List<MemoListActivity.RowData>) :
    RecyclerView.Adapter<MainViewHolder>() {
    private val context: Context? = null
    private val nameList: List<String>? = null
    private var listener: View.OnClickListener? = null


    /**
     * ViewHolder作るメソッド
     * 最初しか呼ばれない。
     * ここでViewHolderのlayoutファイルをインフレーとして生成したViewHolderをRecyclerViewに返す。
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_memo, parent, false)
        return MainViewHolder(view)
    }

    /**
     * ViewHolderとRecyclerViewをバインドする
     * 一行のViewに対して共通でやりたい処理をここで書く。今回はテキストのセットしかしてないけど。
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val rowData = rowDataList[position]
        holder.hogeTitle.text = rowData.memoTitle

        holder.hogeTitle.setOnClickListener(View.OnClickListener { view ->
            listener?.onClick(
                view
            )
        })
    }

    fun setOnItemClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    /**
     * リストの行数
     * @return
     */
    override fun getItemCount(): Int {
        return rowDataList.size
    }
}

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var hogeTitle: TextView = itemView.findViewById(R.id.memo_title)
}