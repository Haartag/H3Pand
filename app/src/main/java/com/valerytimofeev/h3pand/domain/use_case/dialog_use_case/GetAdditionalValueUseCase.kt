package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject

class GetAdditionalValueUseCase @Inject constructor(
    private val repository: PandRepository,
) {
    suspend fun getFullList(
        map: MapSettings,
        zone: Int
    ): Resource<List<AdditionalValueItem>> {
        val addValueResource = repository.getFullAdditionalValueList()
        if (addValueResource.status == Status.ERROR) {
            return Resource.error(addValueResource.message ?: "An unknown error occurred", null)
        }
        val addValueList = addValueResource.data!!


        if (map.modifiedAddValues?.modifiedAddValues != null
            || map.valueRanges[zone].zoneModifiedAddValues?.modifiedAddValues != null
        ) {
            return Resource.success(
                changeValues(
                    addValueList,
                    map.modifiedAddValues?.modifiedAddValues,
                    map.valueRanges[zone].zoneModifiedAddValues?.modifiedAddValues
                )
            )
        }
        return Resource.success(addValueList)
    }

    suspend fun getListByTypeAndSubtype(
        addValueType: String,
        addValueSubtype: String,
        map: MapSettings,
        zone: Int
    ): Resource<List<AdditionalValueItem>> {

        val addValueResource = repository.getAdditionalValuesList(
            addValueType, addValueSubtype
        )
        if (addValueResource.status == Status.ERROR) {
            return Resource.error(addValueResource.message ?: "An unknown error occurred", null)
        }
        val addValueList = addValueResource.data!!
        if (map.modifiedAddValues?.modifiedAddValues != null
            || map.valueRanges[zone].zoneModifiedAddValues?.modifiedAddValues != null
        ) {
            return Resource.success(
                changeValues(
                    addValueList,
                    map.modifiedAddValues?.modifiedAddValues,
                    map.valueRanges[zone].zoneModifiedAddValues?.modifiedAddValues
                )
            )
        }
        return Resource.success(addValueList)

    }

    private fun changeValues(
        addValues: List<AdditionalValueItem>,
        newMapValues: Map<Int, Int>?,
        newZoneValues: Map<Int, Int>?,
    ): List<AdditionalValueItem> {
        addValues.forEach {
            if (newMapValues?.contains(it.id) == true) {
                it.value = newMapValues[it.id]!!
            }
            if (newZoneValues?.contains(it.id) == true) {
                it.value = newZoneValues[it.id]!!
            }
        }
        return addValues
    }
}