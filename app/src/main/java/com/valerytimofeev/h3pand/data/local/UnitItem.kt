package com.valerytimofeev.h3pand.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UnitDB")
data class UnitItem(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val AIValue: Int,
    @ColumnInfo val minOnMap: Int,
    @ColumnInfo val maxOnMap: Int,
    @ColumnInfo val numberInBox: Int,
    @ColumnInfo val castle: Int,
    @ColumnInfo val img: String,
) {
/*    fun UnitItem.getGuard(): Guard {
        return Guard(
            name = this.name,
            AIValue = this.AIValue,
            minOnMap = this.minOnMap,
            maxOnMap = this.maxOnMap
        )
    }

    fun UnitItem.getUnitBox(): UnitBox {
        return UnitBox(
            name = this.name,
            AIValue = this.AIValue,
            numberInBox = this.numberInBox,
            castle = this.castle
        )
    }*/
}

data class Guard(
    val name: String,
    val AIValue: Int,
    val minOnMap: Int,
    val maxOnMap: Int,
)

data class UnitBox(
    val name: String,
    val AIValue: Int,
    val numberInBox: Int,
    val castle: Int,
)


