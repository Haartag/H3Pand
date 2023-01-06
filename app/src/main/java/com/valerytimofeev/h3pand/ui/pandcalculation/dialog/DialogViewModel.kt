package com.valerytimofeev.h3pand.ui.pandcalculation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.additional_data.TextStorage
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.domain.model.*
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import com.valerytimofeev.h3pand.domain.use_case.dialog_use_case.DialogButtonHandleUseCase
import com.valerytimofeev.h3pand.domain.use_case.dialog_use_case.FindItemInAdditionalValuesUseCase
import com.valerytimofeev.h3pand.domain.use_case.dialog_use_case.FindItemInGuardsUseCase
import com.valerytimofeev.h3pand.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val findItemInAdditionalValuesUseCase: FindItemInAdditionalValuesUseCase,
    private val findItemInGuardsUseCase: FindItemInGuardsUseCase,
    private val dialogButtonHandleUseCase: DialogButtonHandleUseCase,
    val getLocalizedTextUseCase: GetLocalizedTextUseCase
) : ViewModel() {

    private val dialogState = mutableStateOf(DialogState(DialogStatus.CLOSED))
    fun getDialogState(): DialogState = dialogState.value
    fun isDialogOpen(): Boolean = getDialogState().dialogStatus != DialogStatus.CLOSED
    fun setDialogState(dialogUiState: DialogState) {
        dialogState.value = dialogUiState
    }

    val isDialogError = mutableStateOf(false)
    val dialogErrorText = mutableStateOf("")

    private lateinit var mapSettings: MapSettings
    private var zoneSettings = 0


    //Texts
    val searchText = getLocalizedTextUseCase(TextStorage.DialogSearch.text)
    val searchGuardResultText: List<String>
        get() = searchGuardResult.map { getLocalizedTextUseCase(it) }
    val searchAddValueResultText: List<String>
        get() = searchAddValueResult.map { getLocalizedTextUseCase(it) }


    var guardList = listOf<Guard>()
    lateinit var chosenGuard: Guard
    lateinit var guardAndNumberOutput: GuardAndNumber
    val searchGuardInput = mutableStateOf("")
    val searchGuardResult = mutableStateListOf<Guard>()

    fun guardDialogCastleButton(index: Int) { //Guard dialog stage 1
        viewModelScope.launch {
            val dialogDataResource = dialogButtonHandleUseCase.guardDialogCastleButton(index)
            if (dialogDataResource.data == null) {
                isDialogError.value = true
                dialogErrorText.value = dialogDataResource.message ?: "An unknown error occurred"
                closeDialogAndWipeData()
            } else {
                guardList = dialogDataResource.data
                setDialogState(DialogState.Companion.DialogUiPresets.GUARD_UNIT.dialogUiState)
            }
        }
    }

    fun guardDialogUnitButton( //Guard dialog stage 2, search guard dialog
        index: Int,
        guardList: List<Guard>
    ) {
        val guard = dialogButtonHandleUseCase.guardDialogUnitButton(index, guardList)
        if (guard.name != "") {
            chosenGuard = guard
            setDialogState(DialogState.Companion.DialogUiPresets.GUARD_NUMBER.dialogUiState)
        }
    }

    fun guardDialogNumberButton(index: Int) { //Guard dialog stage 3
        guardAndNumberOutput = dialogButtonHandleUseCase.guardDialogNumberButton(
            index,
            chosenGuard
        )
        closeDialogAndWipeData()
    }

    fun getMapSettings(
        map: MapSettings,
        zone: Int
    ) {
        mapSettings = map
        zoneSettings = zone
    }

    /**
     * Search text substring in [Guard] names
     */
    fun searchGuard() {
        if (searchGuardInput.value.length >= 3) {
            searchGuardResult.clear()
            viewModelScope.launch {
                val resultList = findItemInGuardsUseCase(
                    searchGuardInput.value,
                )
                if (!resultList.data.isNullOrEmpty()) {
                    searchGuardResult.addAll(resultList.data)
                }
            }
        }
    }

    var currentAddValueSlot = 0
    var dwellingList = listOf<Dwelling>()
    var addValueSubtypeList = listOf<TextWithLocalization>()
    var chosenCastleZone = 1
    var additionalValueList = listOf<AdditionalValueItem>()
    var addValueType = ""
    val searchAddValueInput = mutableStateOf("")
    val searchAddValueResult = mutableStateListOf<SearchItem>()

    fun addValueTypeButton( // Additional value dialog stage 1
        item: String,
        castleZone: Int
    ) {
        addValueType = item
        viewModelScope.launch {
            val dialogDataResource = dialogButtonHandleUseCase.addValueDialogTypeButton(
                item,
                castleZone
            )
            if (dialogDataResource.status == Status.ERROR) {
                isDialogError.value = true
                dialogErrorText.value = dialogDataResource.message ?: "An unknown error occurred"
                closeDialogAndWipeData()
            } else {
                val dialogData = dialogDataResource.data!!
                if (!dialogData.dwellingList.isNullOrEmpty()) dwellingList = dialogData.dwellingList
                if (!dialogData.addValueSubtypeList.isNullOrEmpty()) {
                    addValueSubtypeList = dialogData.addValueSubtypeList
                }
                setDialogState(dialogData.destination)
            }

        }
    }

    fun addValueSubtypeButton(addValueSubtype: TextWithLocalization) { // Additional value dialog stage 2
        viewModelScope.launch {
            val addValueResource = dialogButtonHandleUseCase.addValueDialogSubtypeButton(
                addValueSubtype.enText,
                addValueType,
                mapSettings,
                zoneSettings
            )
            if (addValueResource.data == null) {
                isDialogError.value = true
                dialogErrorText.value = addValueResource.message ?: "An unknown error occurred"
                closeDialogAndWipeData()
            } else {
                additionalValueList = addValueResource.data
                setDialogState(DialogState.Companion.DialogUiPresets.ADDVALUE_ITEM.dialogUiState)
            }
        }
    }

    fun addValueItemButton(addValueItem: AdditionalValueItem): AddValueAndSlot { // Additional value dialog stage 3, custom value dialog
        closeDialogAndWipeData()
        return dialogButtonHandleUseCase.addValueDialogItemButton(
            AddValueAndSlot(
                addValueItem,
                currentAddValueSlot
            )
        )
    }

    fun dwellingItemButton(dwelling: Dwelling): DwellingAndSlot { // Dwelling dialog
        closeDialogAndWipeData()
        return dialogButtonHandleUseCase.dwellingDialogItemButton(
            DwellingAndSlot(
                dwelling,
                currentAddValueSlot
            )
        )
    }

    fun addValueSearchItemButton(index: Int): SearchItemAndSlot { // Add value search
        val searchItemAndSlot = SearchItemAndSlot(
            searchAddValueResult[index],
            currentAddValueSlot
        )
        closeDialogAndWipeData()
        return dialogButtonHandleUseCase.addValueSearchDialogButton(searchItemAndSlot)
    }

    /**
     * Search text substring in [AdditionalValueItem] and [Dwelling] names
     */
    fun searchAddValue() {
        if (searchAddValueInput.value.length >= 3) {
            searchAddValueResult.clear()
            viewModelScope.launch {
                val resultList = findItemInAdditionalValuesUseCase(
                    searchAddValueInput.value,
                    chosenCastleZone,
                    mapSettings,
                    zoneSettings
                )
                if (!resultList.data.isNullOrEmpty()) {
                    searchAddValueResult.addAll(resultList.data)
                }
            }
        }
    }

    fun castleZoneDialogButton(index: Int): Int { //Castle zone dialog
        setDialogState(DialogState.Companion.DialogUiPresets.CLOSED.dialogUiState)
        return if (index == 10) {
            0
        } else {
            index + 1
        }
    }

    fun openSearch(
        type: SearchType
    ) {
        if (type == SearchType.GUARD) {
            setDialogState(DialogState.Companion.DialogUiPresets.GUARD_SEARCH.dialogUiState)
        } else {
            setDialogState(DialogState.Companion.DialogUiPresets.ADDVALUE_SEARCH.dialogUiState)
        }
    }

    fun getDialogHeader(): @Composable () -> Unit {
        return getDialogState().dialogSettings.header
    }

    fun getDialogBody(): @Composable () -> Unit {
        return getDialogState().dialogSettings.body
    }

    /**
     * Close dialog and clean search information
     */
    fun closeDialogAndWipeData() {
        searchAddValueInput.value = ""
        searchAddValueResult.clear()
        searchGuardInput.value = ""
        searchGuardResult.clear()
        setDialogState(DialogState.Companion.DialogUiPresets.CLOSED.dialogUiState)
    }


    fun getRowCount(tabSize: Int): Int {
        return if (tabSize % 2 == 0) {
            tabSize / 2
        } else {
            tabSize / 2 + 1
        }
    }

    fun getAddValueSlot(slot: Int) {
        currentAddValueSlot = slot
    }

    fun getChosenCastleZone(index: Int) {
        chosenCastleZone = index
    }
}