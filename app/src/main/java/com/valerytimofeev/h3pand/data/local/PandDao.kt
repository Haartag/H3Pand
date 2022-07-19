package com.valerytimofeev.h3pand.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PandDao {

    @Query("SELECT * FROM GuardDB")
    suspend fun getAllGuardsList(): List<GuardItem>

    @Query("SELECT * FROM AdditionalDB")
    suspend fun getAllAdditionalValuesList(): List<AdditionalValueItem>

    @Query("SELECT * FROM BoxDB WHERE (value BETWEEN :minValue AND :maxValue) ")
    suspend fun getAllBoxesInRange(minValue: Int, maxValue: Int): List<BoxValueItem>

}