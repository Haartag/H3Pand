package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.*
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

class DefaultPandRepository @Inject constructor(
    private val pandDao: PandDao
) : PandRepository {

    override suspend fun getGuardsByCastle(castle: Int): Resource<List<Guard>> {
        return try {
            val guardList = pandDao.getGuardsByCastle(castle)
            if (guardList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_1.0",
                null
            )
            guardList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_1.1", null)
        }
    }

    override suspend fun getAllGuards(): Resource<List<Guard>> {
        return try {
            val guardList = pandDao.getAllGuards()
            if (guardList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_1.2",
                null
            )
            guardList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_1.3", null)
        }
    }

    override suspend fun getFullAdditionalValueList(): Resource<List<AdditionalValueItem>> {
        return try {
            val fullAddValueList = pandDao.getFullAdditionalValueList()
            if (fullAddValueList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_1.4",
                null
            )
            fullAddValueList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_1.5", null)
        }
    }

    override suspend fun getAdditionalValueTypesList(): Resource<List<String>> {
        return try {
            val addValueTypesList = pandDao.getAdditionalValueTypesList()
            if (addValueTypesList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_2.0",
                null
            )
            addValueTypesList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_2.1", null)
        }
    }

    override suspend fun getAdditionalValueSubtypesList(type: String): Resource<List<String>> {
        return try {
            val addValueSubtypesList = pandDao.getAdditionalValueSubtypesList(type)
            if (addValueSubtypesList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_2.2",
                null
            )
            addValueSubtypesList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_2.3", null)
        }
    }

    override suspend fun getAdditionalValuesList(
        type: String,
        subtype: String
    ): Resource<List<AdditionalValueItem>> {
        return try {
            val addValuesList = pandDao.getAdditionalValuesList(type, subtype)
            if (addValuesList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_2.4",
                null
            )
            addValuesList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_2.5", null)
        }
    }

    override suspend fun getNonUnitBoxesInRange(
        minValue: Int,
        maxValue: Int
    ): Resource<List<BoxValueItem>> {
        return try {
            val boxesInRange = pandDao.getNonUnitBoxesInRange(minValue, maxValue)
            boxesInRange.let {
                if (it.isNotEmpty()) {
                    return@let Resource.success(it)
                } else {
                    return@let Resource.error("No boxes found", null)
                }
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_3.1", null)
        }
    }

    override suspend fun getUnitBoxesInRange(
        minValue: Int,
        maxValue: Int,
        castle: Int
    ): Resource<List<UnitBox>> {
        return try {
            val boxesInRange = pandDao.getUnitBoxesInRange(minValue, maxValue, castle)
            boxesInRange.let {
                if (it.isNotEmpty()) {
                    return@let Resource.success(it)
                } else {
                    return@let Resource.error("No boxes found", null)
                }
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_4.1", null)
        }
    }

    override suspend fun getDwellingsByCastle(
        castle: Int
    ): Resource<List<Dwelling>> {
        return try {
            val dwellings = pandDao.getDwellingsByCastle(castle)
            if (dwellings.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_5.0",
                null
            )
            dwellings.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_5.1", null)
        }
    }

}