package com.valerytimofeev.h3pand.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GuardDB")
data class GuardItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val AIValue: Int
)
