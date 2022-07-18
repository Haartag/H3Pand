package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.utils.Resource

class FakePandRepository: PandRepository {

    private val fakeGuardDatabase = listOf<GuardItem>(
        GuardItem(1, "test name 1", 80),
        GuardItem(2, "test name 2", 250),
        GuardItem(3, "test name 3", 1218),
    )

    private val fakeAdditionalValueDatabase = listOf<AdditionalValueItem>(
        AdditionalValueItem(1, "add name 1", 1400),
        AdditionalValueItem(2, "add name 2", 2000),
        AdditionalValueItem(3, "add name 3", 5000),
    )

    override suspend fun getGuardById(id: Int): Resource<GuardItem> {
        return when (fakeGuardDatabase.find { it.id == id }) {
            null -> Resource.error("Error: no such unit in database", null)
            else -> Resource.success(fakeGuardDatabase.find { it.id == id })
        }
    }

    override suspend fun getAdditionalValueById(id: Int): Resource<AdditionalValueItem> {
        return when (fakeAdditionalValueDatabase.find { it.id == id }) {
            null -> Resource.error("Error: no such item in database", null)
            else -> Resource.success(fakeAdditionalValueDatabase.find { it.id == id })
        }
    }
}