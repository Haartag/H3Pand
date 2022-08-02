package com.valerytimofeev.h3pand.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "UnitBoxDB")
data class UnitBoxValueItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val unitName: String,
    @ColumnInfo val unitNumber: Int,
    @ColumnInfo val castle: Int,
    @ColumnInfo val img: String,
)