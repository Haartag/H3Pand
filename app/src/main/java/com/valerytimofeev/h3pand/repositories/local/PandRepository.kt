package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.*
import com.valerytimofeev.h3pand.utils.Resource


interface PandRepository {

    suspend fun getAllGuardsList(castle: Int): Resource<List<Guard>>

    suspend fun getAdditionalValueTypesList(): Resource<List<String>>

    suspend fun getAdditionalValuesList(type: String): Resource<List<AdditionalValueItem>>

    suspend fun getNonUnitBoxesInRange(minValue: Int, maxValue: Int): Resource<List<BoxValueItem>>

    suspend fun getUnitBoxesInRange(minValue: Int, maxValue: Int, castle: Int): Resource<List<UnitBox>>

    suspend fun getDwellingsByCastle(castle: Int): Resource<List<Dwelling>>
}