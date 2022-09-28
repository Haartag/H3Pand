package com.valerytimofeev.h3pand.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AdditionalDB")
data class AdditionalValueItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val value: Int,
    @ColumnInfo val type: String,
    @ColumnInfo val subtype: String?,
    @ColumnInfo val castle: Int
)