package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.data.local.PandDao
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

class DefaultPandRepository @Inject constructor(
    private val pandDao: PandDao
) : PandRepository {

    override suspend fun getAllGuardsList(): Resource<List<GuardItem>> {
        return try {
            val guardList = pandDao.getAllGuardsList()
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

    override suspend fun getAllAdditionalValuesList(): Resource<List<AdditionalValueItem>> {
        return try {
            val addValuesList = pandDao.getAllAdditionalValuesList()
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

    override suspend fun getAllBoxesInRange(
        minValue: Int,
        maxValue: Int
    ): Resource<List<BoxValueItem>> {
        return try {
            val boxesInRange = pandDao.getAllBoxesInRange(minValue, maxValue)
            boxesInRange.let {
                return@let Resource.success(it)
            }
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database_3.1", null)
        }
    }

}