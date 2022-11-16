package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.domain.model.SearchItem

/**
 * Merge [AdditionalValueItem] list and [Dwelling] list into [SearchItem] list.
 */
class ConvertToSearchListUseCase {
    operator fun invoke(
        addValueList: List<AdditionalValueItem>,
        dwellingList: List<Dwelling>
    ): List<SearchItem> {

        val resultList = mutableListOf<SearchItem>()

        resultList.addAll(addValueList.map {
            SearchItem(
                itemName = it.name,
                itemNameRu = it.nameRu,
                isDwelling = false,
                addItemValue = it.value,
                type = it.type
            )
        })
        resultList.addAll(dwellingList.map {
            SearchItem(
                itemName = it.dwellingName,
                itemNameRu = it.dwellingNameRu,
                isDwelling = true,
                unitValue = it.AIValue,
                weeklyGain = it.weeklyGain,
                castle = it.castle,
                type = "Dwelling"
            )
        })

        return resultList
    }
}