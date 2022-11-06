package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.domain.model.*
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
import com.valerytimofeev.h3pand.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DialogButtonHandleUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakePandRepository: FakePandRepository
    private lateinit var buttonHandleUseCase: DialogButtonHandleUseCase

    @Before
    fun setup() {
        fakePandRepository = FakePandRepository()
        buttonHandleUseCase = DialogButtonHandleUseCase(fakePandRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `guardDialogCastleButton, valid castle index, return success`() = runTest {
        val result = buttonHandleUseCase.guardDialogCastleButton(0)

        Truth.assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    Guard(
                        name = "test name 1",
                        nameRu = "тестовое имя 1",
                        AIValue = 80,
                        minOnMap = 20,
                        maxOnMap = 50,
                        img = "img1"
                    )
                )
            )
        )
    }

    @Test
    fun `guardDialogCastleButton, valid castle index, return success (with additional item)`() =
        runTest {
            val result = buttonHandleUseCase.guardDialogCastleButton(2)

            Truth.assertThat(result.data!!.size).isEqualTo(
                9
            )
            Truth.assertThat(result.data!![7]).isEqualTo(
                Guard(name = "", nameRu = "", AIValue = 0, minOnMap = 0, maxOnMap = 0, img = "")
            )
        }

    @Test
    fun `guardDialogCastleButton, big castle index, return success (neutral guard)`() = runTest {
        val result = buttonHandleUseCase.guardDialogCastleButton(20)

        Truth.assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    Guard(
                        name = "test name 9",
                        nameRu = "тестовое имя 9",
                        AIValue = 1210,
                        minOnMap = 5,
                        maxOnMap = 12,
                        img = "img4"
                    )
                )
            )
        )
    }

    @Test
    fun `guardDialogUnitButton, valid index`() = runTest {
        val result = buttonHandleUseCase.guardDialogUnitButton(
            0,
            listOf(
                Guard(
                    name = "test name 1",
                    nameRu = "тестовое имя 1",
                    AIValue = 80,
                    minOnMap = 20,
                    maxOnMap = 50,
                    img = "img1"
                )
            )
        )
        Truth.assertThat(result).isEqualTo(
            Guard(
                name = "test name 1",
                nameRu = "тестовое имя 1",
                AIValue = 80,
                minOnMap = 20,
                maxOnMap = 50,
                img = "img1"
            )
        )
    }

    @Test
    fun `guardDialogNumberButton, valid index`() = runTest {
        val result = buttonHandleUseCase.guardDialogNumberButton(
            0,
            Guard(
                name = "test name 1",
                nameRu = "тестовое имя 1",
                AIValue = 80,
                minOnMap = 20,
                maxOnMap = 50,
                img = "img1"
            )
        )
        Truth.assertThat(result).isEqualTo(
            GuardAndNumber(
                guard = Guard(
                    name = "test name 1",
                    nameRu = "тестовое имя 1",
                    AIValue = 80,
                    minOnMap = 20,
                    maxOnMap = 50,
                    img = "img1"
                ), numberRangeIndex = 0
            )
        )
    }

    @Test
    fun `addValueDialogTypeButton, valid input, return success`() = runTest {
        val result = buttonHandleUseCase.addValueDialogTypeButton(
            "Misc.",
            1
        )
        Truth.assertThat(result).isEqualTo(
            Resource.success(
                AddValueTypeButtonData(
                    destination = DialogState(
                        dialogStatus = DialogStatus.OPENED,
                        dialogSettings = DialogSettings.AddValueStageTwo,
                        searchState = SearchState.CLOSED,
                        searchType = SearchType.NONE
                    ),
                    dwellingList = null,
                    addValueSubtypeList = listOf(
                        TextWithLocalization("Morale/Luck", "Мораль/Удача"),
                        TextWithLocalization("Trade", "Торговля"),
                    )
                )
            )
        )
    }

    @Test
    fun `addValueDialogTypeButton, wrong item, return error`() = runTest {
        val result = buttonHandleUseCase.addValueDialogTypeButton(
            "Test",
            1
        )
        Truth.assertThat(result).isEqualTo(
            Resource.error("Error", null)
        )
    }

    @Test
    fun `addValueDialogTypeButton, dwelling item, return success`() = runTest {
        val result = buttonHandleUseCase.addValueDialogTypeButton(
            "Dwellings",
            1
        )
        Truth.assertThat(result).isEqualTo(
            Resource.success(
                AddValueTypeButtonData(
                    destination = DialogState(
                        dialogStatus = DialogStatus.OPENED,
                        dialogSettings = DialogSettings.DwellingDialog,
                        searchState = SearchState.CLOSED,
                        searchType = SearchType.NONE
                    ),
                    dwellingList = listOf(
                        Dwelling(
                            dwellingName = "test dwelling 1",
                            dwellingNameRu = "тестовое жилище 1",
                            name = "test name 1",
                            nameRu = "тестовое имя 1",
                            AIValue = 80,
                            weeklyGain = 15,
                            castle = 1
                        )
                    ),
                    addValueSubtypeList = null
                )
            )
        )
    }

    @Test
    fun `addValueDialogTypeButton, dwelling item, castle zone 2 (additional item), return success`() =
        runTest {
            val result = buttonHandleUseCase.addValueDialogTypeButton(
                "Dwellings",
                2
            )
            Truth.assertThat(result).isEqualTo(
                Resource.success(
                    AddValueTypeButtonData(
                        destination = DialogState(
                            dialogStatus = DialogStatus.OPENED,
                            dialogSettings = DialogSettings.DwellingDialog,
                            searchState = SearchState.CLOSED,
                            searchType = SearchType.NONE
                        ),
                        dwellingList = listOf(
                            Dwelling(
                                dwellingName = "test dwelling 2",
                                dwellingNameRu = "тестовое жилище 2",
                                name = "test name 2",
                                nameRu = "тестовое имя 2",
                                AIValue = 60,
                                weeklyGain = 15,
                                castle = 2
                            ),
                            Dwelling(
                                "Elemental conflux",
                                "Сопряжение стихий",
                                "various",
                                "различное",
                                0,
                                0,
                                2
                            )
                        ),
                        addValueSubtypeList = null
                    )
                )
            )
        }

    @Test
    fun `addValueDialogTypeButton, dwelling item, castle zone 0 (additional item), return success`() =
        runTest {
            val result = buttonHandleUseCase.addValueDialogTypeButton(
                "Dwellings",
                0
            )
            Truth.assertThat(result).isEqualTo(
                Resource.success(
                    AddValueTypeButtonData(
                        destination = DialogState(
                            dialogStatus = DialogStatus.OPENED,
                            dialogSettings = DialogSettings.DwellingDialog,
                            searchState = SearchState.CLOSED,
                            searchType = SearchType.NONE
                        ),
                        dwellingList = listOf(
                            Dwelling(
                                dwellingName = "test dwelling 6",
                                dwellingNameRu = "тестовое жилище 6",
                                name = "test name 9",
                                nameRu = "тестовое имя 9",
                                AIValue = 1210,
                                weeklyGain = 2,
                                castle = 0
                            ),
                            Dwelling(
                                "Golem factory",
                                "Фабрика големов",
                                "various",
                                "различное",
                                0,
                                0,
                                10
                            )
                        ),
                        addValueSubtypeList = null
                    )
                )
            )
        }

    @Test
    fun `addValueDialogTypeButton, custom value item, return success`() = runTest {
        val result = buttonHandleUseCase.addValueDialogTypeButton(
            "Choose a value",
            1
        )
        Truth.assertThat(result).isEqualTo(
            Resource.success(
                AddValueTypeButtonData(
                    destination = DialogState(
                        dialogStatus = DialogStatus.OPENED,
                        dialogSettings = DialogSettings.CustomValueDialog,
                        searchState = SearchState.CLOSED,
                        searchType = SearchType.NONE
                    ),
                    dwellingList = null,
                    addValueSubtypeList = null
                )
            )
        )
    }

    @Test
    fun `addValueDialogSubtypeButton, valid input`() = runTest {
        val result = buttonHandleUseCase.addValueDialogSubtypeButton(
            addValueSubtype = "Trade",
            addValueType = "Misc."
        )
        Truth.assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    AdditionalValueItem(
                        3,
                        "add name 3",
                        "доп. имя 3",
                        5000,
                        100,
                        "Misc.",
                        "Разное",
                        "Trade",
                        "Торговля",
                        0
                    ),
                )
            )
        )
    }

    @Test
    fun `addValueDialogSubtypeButton, wrong input`() = runTest {
        val result = buttonHandleUseCase.addValueDialogSubtypeButton(
            addValueSubtype = "Test",
            addValueType = "Misc."
        )
        Truth.assertThat(result).isEqualTo(
            Resource.error(
                "An unknown error occurred",
                null
            )
        )
    }

    @Test
    fun `addValueDialogItemButton, valid input`() = runTest {
        val result = buttonHandleUseCase.addValueDialogItemButton(
            addValueAndSlot = AddValueAndSlot(
                addValue = AdditionalValueItem(
                    3,
                    "add name 3",
                    "доп. имя 3",
                    5000,
                    100,
                    "Misc.",
                    "Разное",
                    "Trade",
                    "Торговля",
                    0
                ),
                slot = 1
            )
        )
        Truth.assertThat(result).isEqualTo(
            AddValueAndSlot(
                addValue = AdditionalValueItem(
                    3,
                    "add name 3",
                    "доп. имя 3",
                    5000,
                    100,
                    "Misc.",
                    "Разное",
                    "Trade",
                    "Торговля",
                    0
                ),
                slot = 1
            )
        )
    }

    @Test
    fun `dwellingDialogItemButton, valid input`() = runTest {
        val result = buttonHandleUseCase.dwellingDialogItemButton(
            dwellingAndSlot = DwellingAndSlot(
                dwelling = Dwelling(
                    dwellingName = "test dwelling 1",
                    dwellingNameRu = "тестовое жилище 1",
                    name = "test name 1",
                    nameRu = "тестовое имя 1",
                    AIValue = 80,
                    weeklyGain = 15,
                    castle = 1
                ),
                slot = 4
            )
        )
        Truth.assertThat(result).isEqualTo(
            DwellingAndSlot(
                dwelling = Dwelling(
                    dwellingName = "test dwelling 1",
                    dwellingNameRu = "тестовое жилище 1",
                    name = "test name 1",
                    nameRu = "тестовое имя 1",
                    AIValue = 80,
                    weeklyGain = 15,
                    castle = 1
                ),
                slot = 4
            )
        )
    }

    @Test
    fun `addValueSearchDialogButton, valid input`() = runTest {
        val result = buttonHandleUseCase.addValueSearchDialogButton(
            searchItemAndSlot = SearchItemAndSlot(
                searchItem = SearchItem(
                    itemName = "add name 1",
                    itemNameRu = "доп. имя 1",
                    isDwelling = false,
                    addItemValue = 1400,
                    unitValue = null,
                    weeklyGain = null,
                    castle = null
                ),
                slot = 4
            )
        )
        Truth.assertThat(result).isEqualTo(
            SearchItemAndSlot(
                searchItem = SearchItem(
                    itemName = "add name 1",
                    itemNameRu = "доп. имя 1",
                    isDwelling = false,
                    addItemValue = 1400,
                    unitValue = null,
                    weeklyGain = null,
                    castle = null
                ),
                slot = 4
            )
        )
    }

}

