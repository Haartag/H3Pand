package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.*
import com.valerytimofeev.h3pand.utils.Resource

class FakePandRepository : PandRepository {

    private val fakeGuardDatabase = listOf<UnitItem>(
        UnitItem(1, "test name 1", 80, 20, 50, 80, 1, "img1"),
        UnitItem(2, "test name 2", 250, 12, 25, 30, 3, "img2"),
        UnitItem(3, "test name 3", 1068, 8, 12, 15, 5, "img3"),
    )

    private val fakeAdditionalValueDatabase = listOf<AdditionalValueItem>(
        AdditionalValueItem(1, "add name 1", 1400, "Misc.", 0),
        AdditionalValueItem(2, "add name 2", 2000, "Misc.", 0),
        AdditionalValueItem(3, "add name 3", 5000, "Misc.", 0),
    )

    private val fakeBoxValueDatabase = listOf<BoxValueItem>(
        BoxValueItem(1, "item 1", 5000, img = "img"),
        BoxValueItem(2, "item 2", 7500, img = "img"),
        BoxValueItem(3, "item 3", 10000, img = "img"),
        BoxValueItem(4, "item 3", 12500, img = "img"),
        BoxValueItem(5, "item 3", 15000, img = "img"),
        BoxValueItem(6, "item 3", 17500, img = "img"),
        BoxValueItem(7, "item 3", 20000, img = "img"),
    )

    private var returnError = false
    private var returnEmptyNonUnitBoxList = false
    private var returnEmptyUnitBoxList = false

    fun shouldReturnError(value: Boolean) {
        returnError = value
    }
    fun shouldReturnEmptyNonUnitBoxList(value: Boolean) {
        returnEmptyNonUnitBoxList = value
    }
    fun shouldReturnEmptyUnitBoxList(value: Boolean) {
        returnEmptyUnitBoxList = value
    }

    override suspend fun getAllGuardsList(castle: Int): Resource<List<Guard>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeGuardDatabase.map {
                Guard(
                    it.name,
                    it.AIValue,
                    it.minOnMap,
                    it.maxOnMap
                )
            })
        }
    }

    override suspend fun getAdditionalValueTypesList(): Resource<List<String>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeAdditionalValueDatabase.map { it.type })
        }
    }

    override suspend fun getAdditionalValuesList(type: String): Resource<List<AdditionalValueItem>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeAdditionalValueDatabase.filter { it.type == type })
        }
    }

    override suspend fun getNonUnitBoxesInRange(
        minValue: Int,
        maxValue: Int
    ): Resource<List<BoxValueItem>> {
        val result = fakeBoxValueDatabase.filter { it.value in minValue..maxValue }
        return when {
            returnError -> Resource.error("Error", null)
            result.isEmpty() -> Resource.error("No boxes found", null)
            returnEmptyNonUnitBoxList -> Resource.error("No boxes found", null)
            else -> Resource.success(result)
        }
    }

    override suspend fun getUnitBoxesInRange(
        minValue: Int,
        maxValue: Int,
        castle: Int
    ): Resource<List<UnitBox>> {
        val result =
            fakeGuardDatabase.filter { it.AIValue * it.numberInBox in minValue..maxValue && it.castle == castle }
        return when {
            returnError -> Resource.error("Error", null)
            result.isEmpty() -> Resource.error("No boxes found", null)
            returnEmptyUnitBoxList -> Resource.error("No boxes found", null)
            else -> Resource.success(result.map {
                UnitBox(
                    it.name,
                    it.AIValue,
                    it.numberInBox,
                    it.castle
                )
            })
        }
    }
}