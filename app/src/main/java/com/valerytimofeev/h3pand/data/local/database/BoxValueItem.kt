package com.valerytimofeev.h3pand.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BoxDB")
data class BoxValueItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val boxContent: String,
    @ColumnInfo val boxContentRu: String,
    @ColumnInfo val value: Int,
    @ColumnInfo val img: String,
    @ColumnInfo val type: String,
)