package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.domain.model.*
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject


class DialogButtonHandleUseCase @Inject constructor(
    private val repository: PandRepository,

    ) {

    suspend fun guardDialogCastleButton(
        index: Int
    ): Resource<List<Guard>> {
        val castle =
            if (index in 0..(CastleSettings.values().size - 2)) index + 1 else 0 // 0 - neutral zone, but it is at the bottom of the list
        val guardResource = repository.getGuardsByCastle(castle)
        return when {
            guardResource.status == Status.ERROR -> Resource.error(
                guardResource.message!!, null
            )
            castle == 10 -> Resource.success( //If castle is cove - add empty guard to align rows after Sea dogs
                guardResource.data!!.toMutableList().also {
                    it.add(7, Guard("", "", 0, 0, 0, ""))
                })
            else -> guardResource
        }
    }

    fun guardDialogUnitButton(
        index: Int, guardList: List<Guard>
    ): Guard {
        return guardList[index]
    }


    fun guardDialogNumberButton(
        index: Int, chosenGuard: Guard
    ): GuardAndNumber {
        return GuardAndNumber(
            chosenGuard, index
        )
    }

    suspend fun addValueDialogTypeButton(
        item: String, chosenCastleZone: Int
    ): Resource<AddValueTypeButtonData> {
        when (item) {
            "Dwellings" -> {
                val dwellingList = mutableListOf<Dwelling>()
                dwellingList.addAll(
                    repository.getDwellingsByCastle(
                        chosenCastleZone,
                    ).data ?: emptyList()
                )
                if (chosenCastleZone == 9) dwellingList.add(
                    Dwelling(
                        "Elemental conflux",
                        "Сопряжение стихий",
                        "various",
                        "различное",
                        0,
                        0,
                        9
                    )
                )
                if (chosenCastleZone == 3 || chosenCastleZone == 0) dwellingList.add(
                    Dwelling(
                        "Golem factory",
                        "Фабрика големов",
                        "various",
                        "различное",
                        0,
                        0,
                        3
                    )
                )
                return Resource.success(
                    AddValueTypeButtonData(
                        DialogState.Companion.DialogUiPresets.ADDVALUE_DWELLING.dialogUiState,
                        dwellingList,
                        null
                    )
                )
            }
            "Choose a value" -> {
                return Resource.success(
                    AddValueTypeButtonData(
                        DialogState.Companion.DialogUiPresets.ADDVALUE_CUSTOMVALUE.dialogUiState,
                        null,
                        null
                    )
                )
            }
            else -> {
                val addValueSubtypesResource = repository.getAdditionalValueSubtypesList(item)
                if (addValueSubtypesResource.data.isNullOrEmpty()) {
                    return Resource.error(
                        addValueSubtypesResource.message ?: "An unknown error occurred", null
                    )
                }
                return Resource.success(
                    AddValueTypeButtonData(
                        DialogState.Companion.DialogUiPresets.ADDVALUE_SUBTYPE.dialogUiState,
                        null,
                        addValueSubtypesResource.data
                    )
                )
            }
        }
    }

    suspend fun addValueDialogSubtypeButton(
        addValueSubtype: String, addValueType: String
    ): Resource<List<AdditionalValueItem>> {
        val result = repository.getAdditionalValuesList(
            addValueType, addValueSubtype
        )
        return if (result.data.isNullOrEmpty()) {
            Resource.error(
                result.message ?: "An unknown error occurred", null
            )
        } else {
            result
        }
    }

    fun addValueDialogItemButton(
        addValueAndSlot: AddValueAndSlot
    ): AddValueAndSlot {
        return addValueAndSlot
    }

    fun dwellingDialogItemButton(
        dwellingAndSlot: DwellingAndSlot
    ): DwellingAndSlot {
        return dwellingAndSlot
    }

    fun addValueSearchDialogButton(
        searchItemAndSlot: SearchItemAndSlot
    ): SearchItemAndSlot {
        return searchItemAndSlot
    }
}



