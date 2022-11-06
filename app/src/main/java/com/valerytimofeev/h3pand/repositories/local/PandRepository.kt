package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.*
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.utils.Resource


interface PandRepository {

    suspend fun getGuardsByCastle(castle: Int): Resource<List<Guard>>

    suspend fun getAllGuards(): Resource<List<Guard>>

    suspend fun getFullAdditionalValueList(): Resource<List<AdditionalValueItem>>

    suspend fun getAdditionalValueTypesList(): Resource<List<TextWithLocalization>>

    suspend fun getAdditionalValueSubtypesList(type: String): Resource<List<TextWithLocalization>>

    suspend fun getAdditionalValuesList(type: String, subtype: String): Resource<List<AdditionalValueItem>>

    suspend fun getNonUnitBoxesInRange(minValue: Int, maxValue: Int): Resource<List<BoxValueItem>>

    suspend fun getUnitBoxesInRange(minValue: Int, maxValue: Int, castle: Int): Resource<List<UnitBox>>

    suspend fun getDwellingsByCastle(castle: Int): Resource<List<Dwelling>>

    suspend fun getAdditionalValueWithFrequency(): Resource<List<AdditionalValueItem>>
}