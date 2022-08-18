package com.valerytimofeev.h3pand.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PandDao {

    @Query("SELECT name, AIValue, minOnMap, maxOnMap FROM UnitDB WHERE castle = :castle")
    suspend fun getAllGuardsList(castle: Int): List<Guard>

    @Query("SELECT DISTINCT type FROM AdditionalDB")
    suspend fun getAdditionalValueTypesList(): List<String>

    @Query("SELECT * FROM AdditionalDB WHERE type = :type")
    suspend fun getAdditionalValuesList(type: String): List<AdditionalValueItem>

    @Query("SELECT * FROM BoxDB WHERE (value BETWEEN :minValue AND :maxValue) ")
    suspend fun getNonUnitBoxesInRange(minValue: Int, maxValue: Int): List<BoxValueItem>

    @Query("SELECT name, AIValue, numberInBox, castle FROM UnitDB WHERE " +
            "((AIValue * numberInBox) BETWEEN :minValue AND :maxValue) AND castle = :castle ")
    suspend fun getUnitBoxesInRange(minValue: Int, maxValue: Int, castle: Int): List<UnitBox>

}