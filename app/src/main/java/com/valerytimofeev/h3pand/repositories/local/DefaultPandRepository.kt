package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.data.local.PandDao
import javax.inject.Inject

class DefaultPandRepository @Inject constructor(
    private val pandDao: PandDao
): PandRepository {
    override suspend fun getGuardByName(id: Int): GuardItem {
        return pandDao.getGuardByName(id)
    }

    override suspend fun getAdditionalValueByName(id: Int): AdditionalValueItem {
        return pandDao.getAdditionalValueByName(id)
    }

}