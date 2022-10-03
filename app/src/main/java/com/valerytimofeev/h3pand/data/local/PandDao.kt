package com.valerytimofeev.h3pand.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

@Dao
interface PandDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM UnitDB WHERE castle = :castle")
    suspend fun getAllGuardsList(castle: Int): List<Guard>

    @Query("SELECT * FROM AdditionalDB")
    suspend fun getFullAdditionalValueList(): List<AdditionalValueItem>

    @Query("SELECT DISTINCT type FROM AdditionalDB")
    suspend fun getAdditionalValueTypesList(): List<String>

    @Query("SELECT DISTINCT subtype FROM AdditionalDB WHERE type = :type")
    suspend fun getAdditionalValueSubtypesList(type: String): List<String>

    @Query("SELECT * FROM AdditionalDB WHERE type = :type AND subtype = :subtype")
    suspend fun getAdditionalValuesList(type: String, subtype: String): List<AdditionalValueItem>

    @Query("SELECT * FROM BoxDB WHERE (value BETWEEN :minValue AND :maxValue) ")
    suspend fun getNonUnitBoxesInRange(minValue: Int, maxValue: Int): List<BoxValueItem>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM UnitDB WHERE " +
                "((AIValue * numberInBox) BETWEEN :minValue AND :maxValue) AND castle = :castle "
    )
    suspend fun getUnitBoxesInRange(minValue: Int, maxValue: Int, castle: Int): List<UnitBox>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM UnitDB WHERE castle = :castle AND dwellingName IS NOT NULL")
    suspend fun getDwellingsByCastle(castle: Int): List<Dwelling>
}