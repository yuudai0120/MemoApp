package com.example.memo

import android.content.Context
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class MemoAdapter internal constructor(
    private val context: Context,
    private var itemDataList: List<MemoListActivity.RowData>,
    private val isAlwaysSelectable: Boolean
) :
    RecyclerView.Adapter<MainViewHolder>() {
    private var listener: View.OnClickListener? = null

    //isAlwaysSelectableがONのときは最初から選択モード
    private var isSelectableMode = isAlwaysSelectable
    private val selectedItemPositions = mutableSetOf<Int>()
    private var clickItemPositions = 0
    // 状態をアクティビティに伝えるためのリスト
    private var modeStatus = mutableSetOf<Int>()



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
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_memo1, parent, false)
        return MainViewHolder(view)
    }

    /**
     * ViewHolderとRecyclerViewをバインドする
     * 一行のViewに対して共通でやりたい処理をここで書く。
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val rowData = itemDataList[position]
        holder.hogeTitle.text = rowData.memoTitle
        //このアイテムが選択済みの場合はチェックを入れる（✓のイメージを表示する）
        holder.checkBox.visibility = if (isSelectedItem(position)) View.VISIBLE else View.GONE
//        holder.hogeTitle.setOnClickListener(View.OnClickListener { view ->

//        holder.memoLayout.setOnClickListener(View.OnClickListener { view ->
//            listener?.onClick(
//                //選択モードでないときは普通のクリックとして扱う
//                if (!isSelectableMode && !isAlwaysSelectable) Toast.makeText(context, "Normal click", Toast.LENGTH_SHORT).show()
//                else {
//                    if (isSelectedItem(position)) removeSelectedItem(position)
//                    else addSelectedItem(position)
//
//                    onBindViewHolder(holder, position)
//                }
//            )
//        })

        holder.memoLayout.setOnClickListener {
            //選択モードでないときは普通のクリックとして扱う
            if (!isSelectableMode && !isAlwaysSelectable) {
                clickItemPositions = position
                modeStatus.add(2)
                listener?.onClick(
                    it
                )
            } else {
                if (isSelectedItem(position)) {
                    removeSelectedItem(position)
                    listener?.onClick(
                        it
                    )
                }
                else addSelectedItem(position)

                onBindViewHolder(holder, position)
            }
        }

        holder.memoLayout.setOnLongClickListener {

            //ロングクリックで選択モードに入る
            if (isSelectedItem(position)) {
                removeSelectedItem(position)
                listener?.onClick(
                    it
                )
            } else {
                addSelectedItem(position)
                listener?.onClick(
                    it
                )
            }
            onBindViewHolder(holder, position)
            return@setOnLongClickListener true
        }

    }

    fun setOnItemClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    /**
     * リストの行数
     * @return
     */
    override fun getItemCount(): Int {
        return itemDataList.size
    }

    fun getClickItemPositions(): Int {
        return clickItemPositions
    }

    //選択済みのアイテムのPositionが記録されたSetを外部に渡す
    fun getSelectedItemPositions() = selectedItemPositions.toSet()

    fun getModeStatus() = modeStatus.toSet()

    //指定されたPositionのアイテムが選択済みか確認する
    private fun isSelectedItem(position: Int): Boolean = (selectedItemPositions.contains(position))

    //選択モードでないときは選択モードに入る
    private fun addSelectedItem(position: Int) {
        if (selectedItemPositions.isEmpty() && !isAlwaysSelectable) {
            isSelectableMode = true
            Toast.makeText(context, "Selectable Mode ON", Toast.LENGTH_SHORT).show()
            modeStatus.add(1)
        }
        selectedItemPositions.add(position)
    }

    //選択モードで最後の一個が選択解除された場合は、選択モードをOFFにする
    private fun removeSelectedItem(position: Int) {
        selectedItemPositions.remove(position)
        if (selectedItemPositions.isEmpty() && !isAlwaysSelectable) {
            isSelectableMode = false
            Toast.makeText(context, "Selectable Mode OFF", Toast.LENGTH_SHORT).show()
            modeStatus.clear()
        }
    }
}

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var hogeTitle: TextView = itemView.findViewById(R.id.memo_title)

    // 1行のレイアウトにイベントをつけるため定義
    var memoLayout: LinearLayout = itemView.findViewById(R.id.memo_holder)
    val checkBox: CheckBox = itemView.findViewById(R.id.memo_checkbox)
}