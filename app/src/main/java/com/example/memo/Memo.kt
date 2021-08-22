package com.example.memo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    @PrimaryKey
    var titleKey: String,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "body")
    var body: String?
)