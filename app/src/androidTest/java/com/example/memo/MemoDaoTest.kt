package com.example.memo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MemoDaoTest {
    private lateinit var memoDao: MemoDao
    private lateinit var db: MemoDatabase

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MemoDatabase::class.java
        ).build()
        memoDao = db.memoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAll_memo_ok() = runBlocking {
        // 初期メモ情報を作成
        val memoList: MutableList<Memo> = ArrayList()
        val memo1 = Memo(
            id = 1,
            title = "title1",
            body = "body1"
        )
        val memo2 = Memo(
            id = 2,
            title = "title2",
            body = "body2"
        )
        memoList.add(memo1)
        memoList.add(memo2)
        for (memoTestData in memoList) {
            memoDao.insert(memoTestData)
        }

        val dbMemoList = memoDao.getAllMemo()
        Assert.assertEquals(memoList, dbMemoList)
    }

    @Test
    fun insert_memo_ok() = runBlocking {
        val memoList: MutableList<Memo> = ArrayList()
        val memo1 = Memo(
            id = 1,
            title = "title1",
            body = "body1"
        )
        memoList.add(memo1)
        memoDao.insert(memo1)
        val dbMemoList = memoDao.getAllMemo()
        Assert.assertEquals(memoList, dbMemoList)
    }

    @Test
    fun update_memo_ok() = runBlocking {
        val memoList: MutableList<Memo> = ArrayList()
        // アップデート前のメモ情報
        val beforeMemoData = Memo(
            id = 1,
            title = "title1",
            body = "beforeMemoData"
        )
        memoDao.insert(beforeMemoData)

        // アップデート後のメモ情報
        val updateMemoData = Memo(
            id = 1,
            title = "title_update",
            body = "body_update"
        )
        memoList.add(updateMemoData)
        memoDao.update(updateMemoData)
        val dbMemoList = memoDao.getAllMemo()
        Assert.assertEquals(memoList, dbMemoList)
    }

    @Test
    fun delete_memo_ok() = runBlocking {
        val memoList: MutableList<Memo> = ArrayList()
        val memo1 = Memo(
            id = 1,
            title = "title1",
            body = "body1"
        )
        val memo2 = Memo(
            id = 2,
            title = "title2",
            body = "body2"
        )
        val deleteMemoData = Memo(
            id = 1,
            title = "title1",
            body = "body1"
        )
        memoDao.insert(memo1)
        memoDao.insert(memo2)
        memoDao.delete(deleteMemoData)
        val dbMemoList = memoDao.getAllMemo()
        memoList.add(memo2)
        Assert.assertEquals(memoList, dbMemoList)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.memo", appContext.packageName)
    }
}