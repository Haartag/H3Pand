package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.*
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

class DefaultPandRepository @Inject constructor(
    private val pandDao: PandDao
) : PandRepository {

    override suspend fun getAllGuardsList(castle: Int): Resource<List<Guard>> {
        return try {
            val guardList = pandDao.getAllGuardsList(castle)
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

    override suspend fun getAdditionalValuesList(type: String): Resource<List<AdditionalValueItem>> {
        return try {
            val addValuesList = pandDao.getAdditionalValuesList(type)
            if (addValuesList.isEmpty()) return Resource.error(
                "An unknown database error occurred: database_2.0",
                null
            )
            addValuesList.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_2.1", null)
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

}