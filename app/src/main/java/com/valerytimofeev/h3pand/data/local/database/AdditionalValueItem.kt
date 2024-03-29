package com.valerytimofeev.h3pand.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AdditionalDB")
data class AdditionalValueItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val nameRu: String,
    @ColumnInfo var value: Int, // Make mutable value another way
    @ColumnInfo val frequency: Int?,
    @ColumnInfo val type: String,
    @ColumnInfo val typeRu: String,
    @ColumnInfo val subtype: String,
    @ColumnInfo val subtypeRu: String,
    @ColumnInfo val castle: Int
)
