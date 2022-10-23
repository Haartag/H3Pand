package com.valerytimofeev.h3pand.domain.model

import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.data.local.Guard

/**
 * Mix of AddValueItem and Dwelling for search.
 */
data class SearchItem(
    val itemName: String,
    val itemNameRu: String,
    val isDwelling: Boolean,
    val type: String? = null,
    val addItemValue: Int? = null,
    val unitValue: Int? = null,
    val weeklyGain: Int? = null,
    val castle: Int? = null
)

data class GuardAndNumber(
    val guard: Guard,
    val numberRangeIndex: Int
)

data class AddValueTypeButtonData(
    val destination: DialogState,
    val dwellingList: List<Dwelling>?,
    val addValueSubtypeList: List<TextWithLocalization>?
)

data class AddValueAndSlot(
    val addValue: AdditionalValueItem,
    val slot: Int
)

data class DwellingAndSlot(
    val dwelling: Dwelling,
    val slot: Int
)

data class SearchItemAndSlot(
    val searchItem: SearchItem,
    val slot: Int
)
