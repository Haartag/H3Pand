package com.valerytimofeev.h3pand.ui.pandcalculation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.data.additional_data.GuardRanges
import com.valerytimofeev.h3pand.data.additional_data.MapSettings.Companion.getMapSettings
import com.valerytimofeev.h3pand.data.additional_data.TextStorage
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.domain.model.*
import com.valerytimofeev.h3pand.domain.use_case.*
import com.valerytimofeev.h3pand.domain.use_case.dialog_use_case.GetAdditionalValueListUseCase
import com.valerytimofeev.h3pand.utils.*
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Main ViewModel for PandCalculation screens: Screen, Error, ItemList, BottomSheet.
 * For Dialog use [DialogViewModel]
 */
@HiltViewModel
class PandCalculationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBoxesUseCase: GetBoxesUseCase,
    private val getValueForSearchItemUseCase: GetValueForSearchItemUseCase,
    private val getAdditionalValueListUseCase: GetAdditionalValueListUseCase,
    val getLocalizedTextUseCase: GetLocalizedTextUseCase,
    private val getItemListGroupsUseCase: GetItemListGroupsUseCase,
    private val settingsDataStorage: SettingsDataStorage,
) : ViewModel() {

    //settingsDataStorage
    var isGroup = true

    //savedStateHandle to take nav argument
    private val currentMap: String = checkNotNull(savedStateHandle["mapName"])
    val fullMapName = getMapSettings(currentMap).mapName
    val mapNameTypo = getMapSettings(currentMap).tileTypo
    val maxCastleNumber = getMapSettings(currentMap).numberOfZones.toFloat()
    val castleNamesList = CastleSettings.values().map { getLocalizedTextUseCase(it.castleName) }
    var additionalValueTypesList = listOf<TextWithLocalization>()

    init {
        viewModelScope.launch {
            isGroup = settingsDataStorage.getListType.first()
            val additionalValueTypesResource = getAdditionalValueListUseCase()
            if (additionalValueTypesResource.status == Status.ERROR) {
                errorText.value =
                    additionalValueTypesResource.message ?: "An unknown error occurred"
            } else {
                additionalValueTypesList = additionalValueTypesResource.data!!
            }
        }
    }

    //States

    val weekSliderPosition = mutableStateOf(0f)
    val castlesSliderPosition = mutableStateOf(1f)
    val zoneSliderPosition = mutableStateOf(0f)
    val castleSliderSteps = if (maxCastleNumber.toInt() >= 2) maxCastleNumber.toInt() - 2 else 0
    val chosenCastleZone = mutableStateOf(1)

    val currentGuardImg = mutableStateOf(R.drawable.ic_dice)
    val chosenGuard = mutableStateOf<Guard?>(null)
    val chosenGuardRange = mutableStateOf(-1)

    val additionalValueMap = mutableStateMapOf<Int, SearchItem>()

    val isErrorShowed = mutableStateOf(false)
    val errorText = mutableStateOf("")

    var boxesWithPercents = mutableStateListOf<BoxWithDropPercent>()

    val boxGroups = mutableStateListOf<GetItemListGroupsUseCase.ListGroup>()
    val expandedTiles = mutableStateMapOf<Int, Boolean>()

    val isSpecifyDialogShown = mutableStateOf(false)
    val specifySliderPosition = mutableStateOf(0f)


    //Texts
    val totalValueText = getLocalizedTextUseCase(TextStorage.SheetTotalValue.text)

    val weekAndMonthText: String //Custom getter to update variables when they change
        get() = String.format(
            getLocalizedTextUseCase(TextStorage.SheetWeekAndMonth.text),
            (weekSliderPosition.value / 4 + 1).toInt().toString(),
            (weekSliderPosition.value % 4 + 1).toInt().toString()
        )

    val townZoneNumberText: String
        get() = String.format(
            getLocalizedTextUseCase(TextStorage.SheetNumberOfTownZones.text),
            castlesSliderPosition.value.roundToInt()
        )

    val typeOfZoneText: String
        get() = String.format(
            getLocalizedTextUseCase(TextStorage.SheetZoneName.text),
            getLocalizedTextUseCase(
                getMapSettings(currentMap).valueRanges[zoneSliderPosition.value.toInt()].zoneName
            )
        )

    val mainTownText: String
        get() = getLocalizedTextUseCase(TextStorage.SheetZoneTownName.text)

    val townName: String
        get() = getLocalizedTextUseCase(
            CastleSettings.values()
                .find { it.id == chosenCastleZone.value }?.castleName!!
        )

    val unitButtonText: String
        get() = if (chosenGuard.value == null || chosenGuardRange.value !in 0..10) {
            getLocalizedTextUseCase(TextStorage.SheetChooseGuard.text)
        } else {
            getLocalizedTextUseCase(chosenGuard.value) +
                    "\n" +
                    GuardRanges.range.getOrDefault(chosenGuardRange.value, "").toString()
                        .replace("..", "–")
        }

    fun tileTypeText(type: String): String {
        return when (type) {
            "Exp" -> getLocalizedTextUseCase(TextStorage.TileNameExp.text)
            "Gold" -> getLocalizedTextUseCase(TextStorage.TileNameGold.text)
            "Spell" -> getLocalizedTextUseCase(TextStorage.TileNameSpell.text)
            "Unit" -> getLocalizedTextUseCase(TextStorage.TileNameUnit.text)
            else -> ""
        }
    }

    fun tilePercentText(index: Int) =
        "${boxGroups[index].summaryPercent.roundToTwoDigits()} %"

    fun itemNameText(name: TextWithLocalization): String {
        return getLocalizedTextUseCase(name)
    }
    fun itemPercentText(percent: Double) = "$percent %"
    fun itemGuardRangeText(guardRange: IntRange): String {
        return String.format(
            getLocalizedTextUseCase(TextStorage.ItemGuard.text),
            guardRange.toString().replace("..", "–")
        )
    }
    fun itemMostLikelyText(mostLikelyGuard: Int): String {
        return String.format(
            getLocalizedTextUseCase(TextStorage.ItemMostLikely.text),
            mostLikelyGuard
        )
    }

    /**
     * Close error and wipe error text
     */
    fun closeError() {
        isErrorShowed.value = false
        errorText.value = ""
    }

    /**
     * Sum values of all addValues
     */
    fun getValueSum(): Int {
        var summ = 0
        additionalValueMap.forEach {
            val item = getValueForSearchItemUseCase(
                it.value,
                maxCastleNumber,
                castlesSliderPosition.value
            )
            if (item.status == Status.SUCCESS) {
                summ += item.data!!
            } else {
                errorText.value = item.message ?: "An unknown error occurred"
                isErrorShowed.value = true
            }
        }
        return summ
    }

    /**
     * Get Guard data from [DialogViewModel]
     */
    fun getGuardData(guardAndNumber: GuardAndNumber) {
        chosenGuard.value = guardAndNumber.guard
        currentGuardImg.value = getItemImage(guardAndNumber.guard.img)
        chosenGuardRange.value = guardAndNumber.numberRangeIndex //TODO
        getBoxesList()
    }

    /**
     * Get addValue data from [DialogViewModel]
     */
    fun getAddValueData(data: AddValueAndSlot) {
        additionalValueMap[data.slot] = SearchItem(
            data.addValue.name,
            data.addValue.nameRu,
            false,
            data.addValue.type,
            data.addValue.value
        )
        getBoxesList()
    }

    /**
     * Get Dwelling data from [DialogViewModel]
     */
    fun getDwellingData(data: DwellingAndSlot) {
        additionalValueMap[data.slot] = SearchItem(
            data.dwelling.dwellingName,
            data.dwelling.dwellingNameRu,
            true,
            "Dwelling",
            null,
            data.dwelling.AIValue,
            data.dwelling.weeklyGain,
            data.dwelling.castle
        )
        getBoxesList()
    }

    /**
     * Get Search addValue data from [DialogViewModel]
     */
    fun getSearchItemData(data: SearchItemAndSlot) {
        additionalValueMap[data.slot] = data.searchItem
        getBoxesList()
    }

    /**
     * Get boxes list to show in [ItemsListWithGroups]
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
                    additionalValue = getValueSum(),
                    castle = chosenCastleZone.value,
                    castleZones = castlesSliderPosition.value.toInt()
                )
                if (boxList.status == Status.SUCCESS) {
                    boxesWithPercents.addAll(boxList.data!!)
                    if (isGroup) {
                        boxGroups.clear()
                        expandedTiles.clear()
                        boxGroups.addAll(getItemListGroupsUseCase(boxesWithPercents))
                        for (index in boxGroups.indices) {
                            expandedTiles[index] = false
                        }
                    }
                } else {
                    isErrorShowed.value = true
                    errorText.value = boxList.message ?: "error"
                }
            }
        }
    }

    fun getAddValueImage(index: Int): Int {
        val item = additionalValueMap[index]?.type ?: "null"
        return GetItemImageAndColorUseCase(item).getAddValueImage()
    }

    fun getItemImage(itemName: String): Int {
        return GetItemImageAndColorUseCase(itemName).getItemImage()
    }

    fun getItemColor(itemName: String, isLight: Boolean = false): Color {
        return GetItemImageAndColorUseCase(itemName).getItemColor(isLight)
    }

    fun getSheetHeight(screenWidth: Dp): Dp {
        val handle = 45.dp
        val addValueHeight = (screenWidth / 4 + 16.dp)
        return handle + addValueHeight
    }

    fun showSpecifyDialog() {
        if (chosenGuard.value != null && chosenGuardRange.value != -1) {
            isSpecifyDialogShown.value = true
        }
    }
    fun closeSpecifyDialog() {
        isSpecifyDialogShown.value = false
    }

    fun addOrRemoveAddValue(
        row: Int,
        column: Int
    ): Boolean {
        val index = row * 4 + column
        return if (additionalValueMap.getOrDefault(
                index,
                defaultValue = 0
            ) == 0
        ) {
            true
        } else {
            additionalValueMap.remove(index)
            getBoxesList()
            false
        }
    }
}
