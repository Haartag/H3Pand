package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.database.*
import com.valerytimofeev.h3pand.utils.Resource

class FakePandRepository(
    private val fakeGuardDatabase: List<UnitItem> = listOf(
        UnitItem(
            1,
            "test name 1",
            "тестовое имя 1",
            80,
            20,
            50,
            15,
            60,
            "test dwelling 1",
            "тестовое жилище 1",
            1,
            "img1"
        ),
        UnitItem(
            2,
            "test name 2",
            "тестовое имя 2",
            60,
            20,
            50,
            15,
            80,
            "test dwelling 2",
            "тестовое жилище 2",
            9,
            "img1"
        ),
        UnitItem(
            3,
            "test name 3",
            "тестовое имя 3",
            240,
            16,
            25,
            8,
            30,
            null,
            null,
            9,
            "img1"
        ),
        UnitItem(
            4,
            "test name 4",
            "тестовое имя 4",
            160,
            16,
            25,
            8,
            45,
            "test dwelling 3",
            "тестовое жилище 3",
            10,
            "img1"
        ),
        UnitItem(
            5,
            "test name 5",
            "тестовое имя 5",
            480,
            10,
            20,
            4,
            25,
            "test dwelling 4",
            "тестовое жилище 4",
            10,
            "img2"
        ),
        UnitItem(
            6,
            "test name 6",
            "тестовое имя 6",
            2400,
            5,
            10,
            2,
            8,
            null,
            null,
            10,
            "img4"
        ),
        UnitItem(
            7,
            "test name 7",
            "тестовое имя 7",
            530,
            10,
            16,
            5,
            20,
            null,
            null,
            4,
            "img2"
        ),
        UnitItem(
            8,
            "test name 8",
            "тестовое имя 8",
            1020,
            8,
            16,
            3,
            15,
            "test dwelling 5",
            "тестовое жилище 5",
            5,
            "img3"
        ),
        UnitItem(
            9,
            "test name 9",
            "тестовое имя 9",
            1210,
            5,
            12,
            2,
            15,
            "test dwelling 6",
            "тестовое жилище 6",
            0,
            "img4"
        ),

        UnitItem(
            10,
            "test name 6",
            "тестовое имя 6",
            2400,
            5,
            10,
            2,
            8,
            null,
            null,
            10,
            "img4"
        ),
        UnitItem(
            11,
            "test name 6",
            "тестовое имя 6",
            2400,
            5,
            10,
            2,
            8,
            null,
            null,
            10,
            "img4"
        ),
        UnitItem(
            12,
            "test name 6",
            "тестовое имя 6",
            2400,
            5,
            10,
            2,
            8,
            null,
            null,
            10,
            "img4"
        ),
        UnitItem(
            13,
            "test name 6",
            "тестовое имя 6",
            2400,
            5,
            10,
            2,
            8,
            null,
            null,
            10,
            "img4"
        ),
        UnitItem(
            14,
            "test name 6",
            "тестовое имя 6",
            2400,
            5,
            10,
            2,
            8,
            null,
            null,
            10,
            "img4"
        ),
    )
) : PandRepository {

    private val fakeAdditionalValueDatabase = listOf(
        AdditionalValueItem(
            1,
            "add name 1",
            "доп. имя 1",
            1400,
            50,
            "Misc.",
            "Разное",
            "Morale/Luck",
            "Мораль/Удача",
            0
        ),
        AdditionalValueItem(
            2,
            "add name 2",
            "доп. имя 2",
            2000,
            null,
            "Artifact",
            "Артефакты",
            "Treasure artifact",
            "Артефакты-сокровища",
            0
        ),
        AdditionalValueItem(
            3,
            "add name 3",
            "доп. имя 3",
            5000,
            100,
            "Misc.",
            "Разное",
            "Trade",
            "Торговля",
            0
        ),
    )

    private val fakeBoxValueDatabase = listOf(
        BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
        BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
        BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
        BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
        BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
        BoxValueItem(6, "item 6", "предмет 6", 17500, img = "img", "Spell"),
        BoxValueItem(7, "item 7", "предмет 7", 20000, img = "img", "Gold"),
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

    override suspend fun getGuardsByCastle(castle: Int): Resource<List<Guard>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeGuardDatabase.filter { it.castle == castle }.map {
                Guard(
                    it.name,
                    it.nameRu,
                    it.AIValue,
                    it.minOnMap,
                    it.maxOnMap,
                    it.img
                )
            })
        }
    }

    override suspend fun getAllGuards(): Resource<List<Guard>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeGuardDatabase.map {
                Guard(
                    it.name,
                    it.nameRu,
                    it.AIValue,
                    it.minOnMap,
                    it.maxOnMap,
                    it.img
                )
            })
        }
    }

    override suspend fun getFullAdditionalValueList(): Resource<List<AdditionalValueItem>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeAdditionalValueDatabase)
        }
    }

    override suspend fun getAdditionalValueTypesList(): Resource<List<TextWithLocalization>> {
        val result = fakeAdditionalValueDatabase.map {
            TextWithLocalization(it.type, it.typeRu)
        }.distinct()
        return when {
            (returnError) -> Resource.error("Error", null)
            result.isEmpty() -> Resource.error("Error", null)
            else -> Resource.success(result)
        }
    }

    override suspend fun getAdditionalValueSubtypesList(type: String): Resource<List<TextWithLocalization>> {
        val result = fakeAdditionalValueDatabase.filter { it.type == type }.map {
            TextWithLocalization(it.subtype, it.subtypeRu)
        }
        return when {
            (returnError) -> Resource.error("Error", null)
            result.isEmpty() -> Resource.error("Error", null)
            else -> Resource.success(result)
        }
    }

    override suspend fun getAdditionalValuesList(
        type: String,
        subtype: String
    ): Resource<List<AdditionalValueItem>> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(fakeAdditionalValueDatabase
                .filter { it.type == type }
                .filter { it.subtype == subtype })
        }
    }

    override suspend fun getAllNonUnitBoxes(): Resource<List<BoxValueItem>> {
        val result = fakeBoxValueDatabase
        return when {
            returnError -> Resource.error("Error", null)
            result.isEmpty() -> Resource.error("No boxes found", null)
            returnEmptyNonUnitBoxList -> Resource.error("No boxes found", null)
            else -> Resource.success(result)
        }
    }

    override suspend fun getAllUnitBoxesByCastle(castle: Int): Resource<List<UnitBox>> {
        val result = fakeGuardDatabase.filter { it.castle == castle }
        return when {
            returnError -> Resource.error("Error", null)
            result.isEmpty() -> Resource.error("No boxes found", null)
            returnEmptyUnitBoxList -> Resource.error("No boxes found", null)
            else -> Resource.success(result.map {
                UnitBox(
                    it.id,
                    it.name,
                    it.nameRu,
                    it.AIValue,
                    it.numberInBox,
                    it.castle,
                    it.img
                )
            })
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
                    it.id,
                    it.name,
                    it.nameRu,
                    it.AIValue,
                    it.numberInBox,
                    it.castle,
                    it.img
                )
            })
        }
    }

    override suspend fun getDwellingsByCastle(
        castle: Int
    ): Resource<List<Dwelling>> {
        val result =
            fakeGuardDatabase.filter { it.castle == castle && !it.dwellingName.isNullOrEmpty() }
                .map {
                    Dwelling(
                        it.dwellingName!!,
                        it.dwellingNameRu!!,
                        it.name,
                        it.nameRu,
                        it.AIValue,
                        it.weeklyGain,
                        it.castle
                    )
                }
        if (result.isEmpty()) return Resource.error(
            "An unknown database error occurred: database_5.0",
            null
        )
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(result)
        }
    }

    override suspend fun getAdditionalValueWithFrequency(): Resource<List<AdditionalValueItem>> {
        val result = fakeAdditionalValueDatabase.filter { it.frequency != null }
        if (result.isEmpty()) return Resource.error(
            "An unknown database error occurred: database_6.0",
            null
        )
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(result)
        }
    }
}