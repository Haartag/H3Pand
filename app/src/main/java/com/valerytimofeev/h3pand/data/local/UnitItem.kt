package com.valerytimofeev.h3pand.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UnitDB")
data class UnitItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val nameRu: String,
    @ColumnInfo val AIValue: Int,
    @ColumnInfo val minOnMap: Int,
    @ColumnInfo val maxOnMap: Int,
    @ColumnInfo val weeklyGain: Int,
    @ColumnInfo val numberInBox: Int,
    @ColumnInfo val dwellingName: String?,
    @ColumnInfo val dwellingNameRu: String?,
    @ColumnInfo val castle: Int,
    @ColumnInfo val img: String,
)

data class Guard(
    val name: String,
    val nameRu: String,
    val AIValue: Int,
    val minOnMap: Int,
    val maxOnMap: Int,
    val img: String
)

data class UnitBox(
    val name: String,
    val nameRu: String,
    val AIValue: Int,
    val numberInBox: Int,
    val castle: Int,
    val img: String
)

data class Dwelling(
    val dwellingName: String,
    val dwellingNameRu: String,
    val name: String,
    val nameRu: String,
    val AIValue: Int,
    val weeklyGain: Int,
    val castle: Int,
)


