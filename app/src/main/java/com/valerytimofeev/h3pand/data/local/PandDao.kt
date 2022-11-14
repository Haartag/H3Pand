package com.valerytimofeev.h3pand.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization

@Dao
interface PandDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM UnitDB WHERE castle = :castle")
    suspend fun getGuardsByCastle(castle: Int): List<Guard>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM UnitDB")
    suspend fun getAllGuards(): List<Guard>

    @Query("SELECT * FROM AdditionalDB")
    suspend fun getFullAdditionalValueList(): List<AdditionalValueItem>

    @Query("SELECT DISTINCT type AS enText, typeRu AS ruText FROM AdditionalDB")
    suspend fun getAdditionalValueTypesList(): List<TextWithLocalization>

    @Query("SELECT DISTINCT subtype AS enText, subtypeRu AS ruText FROM AdditionalDB WHERE type = :type")
    suspend fun getAdditionalValueSubtypesList(type: String): List<TextWithLocalization>

    @Query("SELECT * FROM AdditionalDB WHERE type = :type AND subtype = :subtype")
    suspend fun getAdditionalValuesList(type: String, subtype: String): List<AdditionalValueItem>

    @Query("SELECT * FROM BoxDB")
    suspend fun getAllNonUnitBoxes(): List<BoxValueItem>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM UnitDB WHERE castle = :castle")
    suspend fun getAllUnitBoxesByCastle(castle: Int): List<UnitBox>

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

    @Query("SELECT * FROM AdditionalDB WHERE frequency IS NOT NULL")
    suspend fun getAdditionalValueWithFrequency(): List<AdditionalValueItem>
}