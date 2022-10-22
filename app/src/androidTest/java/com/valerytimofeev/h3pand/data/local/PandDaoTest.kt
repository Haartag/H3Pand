package com.valerytimofeev.h3pand.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class PandDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: PandDatabase
    private lateinit var dao: PandDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.pandDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun getAllGuardList_returnCorrectGuardAndListSize() = runTest {
        val result = dao.getGuardsByCastle(5)
        assertThat(result[0]).isEqualTo(
            Guard(
                name = "Gnoll",
                "Гноллы",
                AIValue = 56,
                minOnMap = 20,
                maxOnMap = 50,
                img = "Pawn"
            )
        )
        assertThat(result.size).isEqualTo(14)
    }

    @Test
    fun getAllGuardList_invalidCastle_returnEmptyList() = runTest {
        val result = dao.getGuardsByCastle(20)
        assertThat(result).isEmpty()
    }

    @Test
    fun getFullAdditionalValueList_returnCorrectAddValueAndListSize() = runTest {
        val result = dao.getFullAdditionalValueList()
        assertThat(result[0]).isEqualTo(
            AdditionalValueItem(
                1,
                "Altar of sacrifice",
                "Жертвенный алтарь",
                100,
                "Misc.",
                "Разное",
                "Expirience",
                "Опыт",
                0
            )
        )
        assertThat(result.size).isEqualTo(232)
    }

    @Test
    fun getAdditionalValueTypesList_returnCorrectTypes() = runTest {
        val result = dao.getAdditionalValueTypesList()
        assertThat(result).isEqualTo(
            listOf(
                TextWithLocalization(enText = "Misc.", ruText = "Разное"),
                TextWithLocalization(enText = "Resource", ruText = "Ресурсы"),
                TextWithLocalization(enText = "Bank", ruText = "Банк"),
                TextWithLocalization(enText = "Artifact", ruText = "Артефакт")
            )
        )
    }

    @Test
    fun getAdditionalValueSubtypesList_returnCorrectSubtypes() = runTest {
        val result = dao.getAdditionalValueSubtypesList("Artifact")
        assertThat(result).isEqualTo(
            listOf(
                TextWithLocalization(enText = "Treasure artifact", ruText = "Артефакты-сокровища"),
                TextWithLocalization(enText = "Minor artifact", ruText = "Малые артефакты"),
                TextWithLocalization(enText = "Major artifact", ruText = "Великие артефакты"),
                TextWithLocalization(enText = "Relic artifact", ruText = "Реликты")
            )
        )
    }

    @Test
    fun getAdditionalValueSubtypesList_invalidInput() = runTest {
        val result = dao.getAdditionalValueSubtypesList("Test type")
        assertThat(result).isEqualTo(
            listOf<String>()
        )
    }

    @Test
    fun getAdditionalValuesList_returnCorrectAdditionalValueItemAndListSize() = runTest {
        val result = dao.getAdditionalValuesList("Bank", "Artifact bank")
        assertThat(result[0]).isEqualTo(
            AdditionalValueItem(
                id = 31,
                name = "Crypt",
                nameRu = "Склеп",
                value = 1000,
                type = "Bank",
                typeRu = "Банк",
                subtype = "Artifact bank",
                subtypeRu = "Банки артефактов",
                castle = 0
            )
        )
        assertThat(result.size).isEqualTo(5)
    }

    @Test
    fun getAdditionalValuesList_invalidType_returnEmptyList() = runTest {
        val result = dao.getAdditionalValuesList("TestType", "")
        assertThat(result).isEmpty()
    }

    @Test
    fun getAdditionalValuesList_invalidSubtype_returnEmptyList() = runTest {
        val result = dao.getAdditionalValuesList("Bank", "Test subtype")
        assertThat(result).isEmpty()
    }

    @Test
    fun getAdditionalValuesList_returnValidSubtype() = runTest {
        val result = dao.getAdditionalValuesList("Artifact", "Treasure artifact")
        assertThat(result[0].subtype).isEqualTo("Treasure artifact")
    }

    @Test
    fun getNonUnitBoxes_validInput() = runTest {
        val minRange = 5000
        val maxRange = 8000
        val result = dao.getNonUnitBoxesInRange(minRange, maxRange)

        assertThat(result).isEqualTo(
            listOf(
                BoxValueItem(
                    id = 1,
                    boxContent = "5000 gold",
                    boxContentRu = "5000 золото",
                    value = 5000,
                    img = "gold"
                ),
                BoxValueItem(
                    id = 5,
                    boxContent = "5000 exp.",
                    boxContentRu = "5000 опыт",
                    value = 6000,
                    img = "exp"
                ),
                BoxValueItem(
                    id = 13,
                    boxContent = "1 lvl. spells",
                    boxContentRu = "Заклинания 1 уровня",
                    value = 5000,
                    img = "spells1"
                ),
                BoxValueItem(
                    id = 14,
                    boxContent = "2 lvl. spells",
                    boxContentRu = "Заклинания 2 уровня",
                    value = 7500,
                    img = "spells1"
                )
            )
        )
    }

    @Test
    fun getNonUnitBoxes_tooLowRange_returnEmptyList() = runTest {
        val minRange = 1000
        val maxRange = 4000
        val result = dao.getNonUnitBoxesInRange(minRange, maxRange)

        assertThat(result).isEmpty()
    }

    @Test
    fun getNonUnitBoxes_tooHiRange_returnEmptyList() = runTest {
        val minRange = 35000
        val maxRange = 40000
        val result = dao.getNonUnitBoxesInRange(minRange, maxRange)

        assertThat(result).isEmpty()
    }

    @Test
    fun getUnitBoxes_validInput() = runTest {
        val minRange = 5000
        val maxRange = 8000
        val castle = 4
        val result = dao.getUnitBoxesInRange(minRange, maxRange, castle)

        assertThat(result).isEqualTo(
            listOf(
                UnitBox(
                    name = "Infernal troglodyte",
                    nameRu = "Троглодиты-охотники",
                    AIValue = 84,
                    numberInBox = 60,
                    castle = 4,
                    img = "Pawn"
                ),
                UnitBox(
                    name = "Harpy",
                    nameRu = "Гарпии",
                    AIValue = 154,
                    numberInBox = 45,
                    castle = 4,
                    img = "Pawn"
                ),
                UnitBox(
                    name = "Harpy hag",
                    nameRu = "Гарпии-ведьмы",
                    AIValue = 238,
                    numberInBox = 30,
                    castle = 4,
                    img = "Pawn"
                ),
            )
        )
    }

    @Test
    fun getUnitBoxes_tooLowRange_returnEmptyList() = runTest {
        val minRange = 1000
        val maxRange = 4000
        val castle = 1
        val result = dao.getUnitBoxesInRange(minRange, maxRange, castle)

        assertThat(result).isEmpty()
    }

    @Test
    fun getUnitBoxes_tooHiRange_returnEmptyList() = runTest {
        val minRange = 35000
        val maxRange = 40000
        val castle = 1
        val result = dao.getUnitBoxesInRange(minRange, maxRange, castle)

        assertThat(result).isEmpty()
    }

    @Test
    fun getUnitBoxes_invalidCastle_returnEmptyList() = runTest {
        val minRange = 5000
        val maxRange = 8000
        val castle = 20
        val result = dao.getUnitBoxesInRange(minRange, maxRange, castle)

        assertThat(result).isEmpty()
    }

    @Test
    fun getDwellings_validInput() = runTest {
        val result = dao.getDwellingsByCastle(1)
        assertThat(result[0]).isEqualTo(
            Dwelling(
                "Guardhouse",
                "Сторожевой пост",
                "Pikeman",
                "Копейщики",
                80,
                14,
                1
            )
        )
    }

    @Test
    fun getDwellings_invalidCastle_returnEmptyList() = runTest {
        val result = dao.getDwellingsByCastle(20)
        assertThat(result).isEmpty()
    }
}