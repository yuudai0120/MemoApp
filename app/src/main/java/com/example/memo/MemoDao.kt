package com.example.memo

import androidx.room.*

@Dao
interface MemoDao  {
    // シンプルなSELECTクエリ
    @Query("SELECT * FROM memo")
    suspend fun getAllMemo(): List<Memo>

    // データモデルのクラスを引数に渡すことで、データの作成ができる。
    @Insert
    suspend fun insert(memo: Memo)

    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して削除する場合。
    @Delete
    suspend fun delete(memo: Memo)

    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して更新する場合。
    @Update
    suspend fun update(memo: Memo)
}