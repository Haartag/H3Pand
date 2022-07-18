package com.valerytimofeev.h3pand.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PandDao {

    @Query("SELECT * FROM GuardDB WHERE id = :id")
    suspend fun getGuardById(id: Int): GuardItem?

    @Query("SELECT * FROM AdditionalDB WHERE id = :id")
    suspend fun getAdditionalValueById(id: Int): AdditionalValueItem?

}