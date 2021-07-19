package com.example.memo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemoDao  {
    // シンプルなSELECTクエリ
    @Query("SELECT * FROM memo")
    suspend fun getAllMemo(): List<Memo>

    // メソッドの引数をSQLのパラメーターにマッピングするには :引数名 と書く
    @Query("select * from Memo where title = :title")
    suspend fun getMemo(vararg title: String): List<Memo>

    // データモデルのクラスを引数に渡すことで、データの作成ができる。
    @Insert
    suspend fun insert(user: Memo)

    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して削除する場合。
    @Delete
    suspend fun delete(user: Memo)

    // 複雑な条件で削除したい場合は、@Queryを使ってSQLを書く
    @Query("DELETE FROM Memo WHERE title = :title")
    suspend fun selectDelete(title: String)
}