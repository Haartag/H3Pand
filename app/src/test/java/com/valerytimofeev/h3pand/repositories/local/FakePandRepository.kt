package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.utils.Resource

class FakePandRepository : PandRepository {

    private val fakeGuardDatabase = listOf<GuardItem>(
        GuardItem(1, "test name 1", 80),
        GuardItem(2, "test name 2", 250),
        GuardItem(3, "test name 3", 1218),
    )

    private val fakeAdditionalValueDatabase = listOf<AdditionalValueItem>(
        AdditionalValueItem(1, "add name 1", 1400, 0),
        AdditionalValueItem(2, "add name 2", 2000, 0),
        AdditionalValueItem(3, "add name 3", 5000, 1),
    )

    private val fakeBoxValueDatabase = listOf<BoxValueItem>(
        BoxValueItem(1, "item 1", 5000, 0),
        BoxValueItem(2, "item 2", 6000, 0),
        BoxValueItem(3, "item 3", 9600, 0),
    )

    private var returnError = false

    fun shouldReturnError(value: Boolean) {
        returnError = value
    }


    override suspend fun getAllGuardsList(): Resource<List<GuardItem>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeGuardDatabase)
        }
    }

    override suspend fun getAllAdditionalValuesList(): Resource<List<AdditionalValueItem>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeAdditionalValueDatabase)
        }
    }

    override suspend fun getAllBoxesInRange(
        minValue: Int,
        maxValue: Int
    ): Resource<List<BoxValueItem>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeBoxValueDatabase.filter { it.value in minValue..maxValue })
        }
    }

}