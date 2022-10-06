package com.valerytimofeev.h3pand.ui.pandcalculation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.domain.use_case.*
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.*
import com.valerytimofeev.h3pand.utils.Constants.MIN_PERCENT
import com.valerytimofeev.h3pand.utils.MapSettings.Companion.getMapSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PandCalculationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PandRepository,
    private val getBoxesUseCase: GetBoxesUseCase,
    private val getDwellingsListUseCase: GetDwellingsListUseCase,
    private val findItemInAdditionalValuesUseCase: FindItemInAdditionalValuesUseCase,
    private val chooseDialogContentUseCase: ChooseDialogContentUseCase,
    private val getDwellingValueUseCase: GetDwellingValueUseCase,
) : ViewModel() {


    /**
     * Map settings
     */
    //savedStateHandle to take nav argument
    private val currentMap: String = checkNotNull(savedStateHandle["mapName"])
    val maxCastleNumber = getMapSettings(currentMap)?.numberOfZones?.toFloat() ?: 5.0f
    val castleNamesList = CastleSettings.values().map { it.castleName }
    val castleListSize = getRowCount(CastleSettings.values().size)

    /**
     * Calculation variables
     */

    val additionalValueType = mutableStateOf("")
    val additionalValueSubtype = mutableStateOf("")
    val additionalValueMap = mutableStateMapOf<Int, Int>()
    val dwellingMap = mutableStateMapOf<Int, Dwelling>()

    var boxesWithPercents = mutableStateListOf<BoxWithDropPercent>()

    /**
     * Bottom sheet status variables
     */

    val weekSliderPosition = mutableStateOf(0f)
    val castlesSliderPosition = mutableStateOf(1f)
    val zoneSliderPosition = mutableStateOf(0f)
    val castleSliderSteps = if (maxCastleNumber.toInt() >= 2) maxCastleNumber.toInt() - 2 else 0

    val chosenCastle = mutableStateOf(-1)
    val chosenCastleZone = mutableStateOf(1)
    val currentImg = mutableStateOf(R.drawable.ic_dice)
    val clickedAddValue = mutableStateOf(0)

    /**
     * Dialog status variables
     */
    val dialogState = mutableStateOf(Dialog.closed(null))

    val chosenGuard = mutableStateOf<Guard?>(null)
    val chosenGuardRange = mutableStateOf(-1)

    var guardList = mutableStateListOf<Guard>()
    var dwellingList = mutableStateListOf<Dwelling>()
    var additionalValueTypesList = mutableStateListOf<String>()
    var additionalValueSubtypesList = mutableStateListOf<String>()
    var additionalValueList = mutableStateListOf<AdditionalValueItem>()

    /**
     * Error status variables
     */
    val isErrorShowed = mutableStateOf(false)
    val errorText = mutableStateOf("")


    /**
     * UI functions
     */
    fun closeError() {
        isErrorShowed.value = false
        errorText.value = ""
    }

    fun getDialogHeader(): @Composable () -> Unit {
        return dialogState.value.data?.header ?: { Text(text = "Some error occured") }
    }

    fun getDialogBody(): @Composable () -> Unit {
        return dialogState.value.data?.body ?: { Text(text = "Some error occured") }
    }

    fun setDialogState(dialogType: String) {
        dialogState.value = if (dialogType == "Close") {
            Dialog.closed(null)
        } else {
            chooseDialogContentUseCase(dialogType)
        }
    }

    /**
     * UI calculations
     */
    fun getRowCount(tabSize: Int): Int {
        return if (tabSize % 2 == 0) {
            tabSize / 2
        } else {
            tabSize / 2 + 1
        }
    }

    /**
     * Get info from DB repository
     */
    private suspend fun getGuardsList(clickedItem: Int) {
        guardList.clear()
        //offset by 1 to match CastleSettings
        chosenCastle.value = if (clickedItem in 0..9) clickedItem + 1 else 0
        guardList.addAll(
            repository.getAllGuardsList(chosenCastle.value).data ?: emptyList()
        )
        //Cove - add empty guard to align rows after Sea dogs
        if (chosenCastle.value == 3) {
            guardList.add(7, Guard("", 0, 0, 0, ""))
        }
    }

    fun chooseGuardDialogGoToStage2(clickedItem: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            getGuardsList(clickedItem)
        }.invokeOnCompletion {
            setDialogState("GuardStageTwo")
        }
    }

    fun chooseGuardDialogGoToStage3(clickedItem: Int) {
        //Cove - add empty guard to align rows after Sea dogs
        if (guardList[clickedItem].name != "") {
            chosenGuard.value = guardList[clickedItem]
            currentImg.value = getItemImage(null, chosenGuard.value?.img)
            guardList.clear()
            setDialogState("GuardStageThree")
        }
    }

    fun closeChooseGuardDialog(guardRange: Int) {
        chosenGuardRange.value = guardRange
        getBoxesList()
        setDialogState("Close")
    }

    fun getAdditionalValueTypesList() {
        viewModelScope.launch {
            additionalValueTypesList.add("Custom value")
            additionalValueTypesList.addAll(
                repository.getAdditionalValueTypesList().data ?: emptyList()
            )
            additionalValueTypesList.add("Dwelling")
        }
    }

    fun getAdditionalValueSubtypesList(type: String) {
        viewModelScope.launch {
            additionalValueSubtypesList.addAll(
                repository.getAdditionalValueSubtypesList(type).data ?: emptyList()
            )
        }
    }

    fun getAdditionalValueList() {
        viewModelScope.launch {
            additionalValueList.addAll(
                repository.getAdditionalValuesList(
                    additionalValueType.value,
                    additionalValueSubtype.value,
                ).data ?: emptyList()
            )
        }
    }

    fun getDwellingList() {
        viewModelScope.launch {
            dwellingList.addAll(
                repository.getDwellingsByCastle(
                    chosenCastleZone.value,
                ).data ?: emptyList()
            )
            if (chosenCastleZone.value == 2) dwellingList.add(
                Dwelling("Elemental conflux", "various", 0, 0, 2)
            )
            if (chosenCastleZone.value == 10 || chosenCastleZone.value == 0) dwellingList.add(
                Dwelling("Golem factory", "various", 0, 0, 10)
            )
        }
    }

    fun closeAddValueDialog(value: Int) {
        additionalValueMap[clickedAddValue.value] = value
        getBoxesList()
        additionalValueList.clear()
        additionalValueTypesList.clear()
    }

    fun closeDwellingDialog(value: Int) {
        additionalValueMap[clickedAddValue.value] = value
        getBoxesList()
        additionalValueList.clear()
        additionalValueTypesList.clear()
    }

    fun closeCustomValueDialog(value: Int) {
        additionalValueMap[clickedAddValue.value] = value
        getBoxesList()
        additionalValueList.clear()
        additionalValueTypesList.clear()
    }

    fun updateDwellings() {
        dwellingMap.forEach {
            additionalValueMap[it.key] = getDwellingValueUseCase(
                dwellingMap[it.key]!!,
                maxCastleNumber,
                castlesSliderPosition.value,
            )
        }
    }

    fun closeDialogAndWipeData() {
        setDialogState("Close")
        dwellingList.clear()
        additionalValueList.clear()
        additionalValueTypesList.clear()
        additionalValueSubtypesList.clear()
    }


    /**
     * Get info from domain
     */

    fun getBoxesList() {
        viewModelScope.launch {
            boxesWithPercents.clear()
            if (chosenGuard.value != null) {
                val boxList = getBoxesUseCase(
                    guardUnit = chosenGuard.value!!,
                    guardRangeIndex = chosenGuardRange.value,
                    zoneType = zoneSliderPosition.value.toInt(),
                    mapName = currentMap,
                    week = weekSliderPosition.value.toInt() + 1,
                    additionalValue = additionalValueMap.values.sum(),
                    castle = chosenCastleZone.value,
                    castleZones = castlesSliderPosition.value.toInt()
                )
                if (boxList.status == Status.SUCCESS) {
                    boxesWithPercents.addAll(boxList.data!!.filter { it.dropChance >= MIN_PERCENT })
                } else {
                    isErrorShowed.value = true
                    errorText.value = boxList.message ?: "error"
                }
            }
        }
    }

    fun getItemImage(itemNumber: Int?, itemName: String? = null): Int {
        val itemImage = when {
            itemNumber != null -> boxesWithPercents[itemNumber].img
            itemName != null -> itemName
            else -> ""
        }
        return GetItemImageAndColorUseCase(itemImage).getItemImage()
    }

    fun getItemColor(itemNumber: Int): Color {
        val itemImage = boxesWithPercents[itemNumber].img
        return GetItemImageAndColorUseCase(itemImage).getItemColor()
    }

    fun getDwellingValue(indexInList: Int): Int {
        dwellingMap[clickedAddValue.value] = dwellingList[indexInList]
        return getDwellingValueUseCase(
            dwellingList[indexInList],
            maxCastleNumber,
            castlesSliderPosition.value,
        )
    }
}