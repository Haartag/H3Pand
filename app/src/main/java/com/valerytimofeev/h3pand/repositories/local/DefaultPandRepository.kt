package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.data.local.PandDao
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

class DefaultPandRepository @Inject constructor(
    private val pandDao: PandDao
) : PandRepository {
    override suspend fun getGuardById(id: Int): Resource<GuardItem> {
        return try {
            val guard = pandDao.getGuardById(id)
            guard?.let {
                return@let Resource.success(it)
            } ?: Resource.error("Error: no such unit in database", null)
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database1", null)
        }
    }

    override suspend fun getAdditionalValueById(id: Int): Resource<AdditionalValueItem> {
        return try {
            val addValue = pandDao.getAdditionalValueById(id)
            addValue?.let {
                return@let Resource.success(it)
            } ?: Resource.error("Error: no such item in database", null)
        } catch (e: Exception) {
            Resource.error("An unknown database error occurred: database2", null)
        }
    }

}