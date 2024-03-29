package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.utils.Status
import org.junit.Before
import org.junit.Test

class GetDwellingValueUseCaseTest {

    private lateinit var getDwellingValueUseCase: GetDwellingValueUseCase

    @Before
    fun setup() {
        getDwellingValueUseCase = GetDwellingValueUseCase()
    }

    @Test
    fun `Get dwelling value, valid input, returns success`() {

        val dwelling = Dwelling("Guardhouse","Сторожевой пост", "Pikeman", "Копейщики", 80, 14, 1)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(1384)
    }

    @Test
    fun `Get dwelling value, dwelling with increased value input, returns success`() {

        val dwelling = Dwelling("Estate", "Поместье", "Вампиры", "Vampire", 555, 4, 7)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(8823)
    }

    @Test
    fun `Get dwelling value, valid input, number of zones modified, returns success`() {

        val dwelling = Dwelling("Guardhouse","Сторожевой пост", "Pikeman", "Копейщики", 80, 14, 1)
        val numberOfZones = 20.0f
        val numberOfUnitZones = 1.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(1216)
    }

    @Test
    fun `Get dwelling value, valid input, number of unit zones modified, returns success`() {

        val dwelling = Dwelling("Guardhouse","Сторожевой пост", "Pikeman", "Копейщики", 80, 14, 1)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 3.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(1912)
    }

    @Test
    fun `Get dwelling value, number of unit zones improperly modified, returns error`() {

        val dwelling = Dwelling("Guardhouse","Сторожевой пост", "Pikeman", "Копейщики", 80, 14, 1)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 6.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `Get dwelling value, special dwelling input 1, returns success`() {

        val dwelling = Dwelling("Elemental conflux", "Сопряжение стихий", "various", "различное", -1, -1, 2)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(4580)
    }

    @Test
    fun `Get dwelling value, special dwelling input 2, returns success`() {

        val dwelling = Dwelling("Golem factory", "Фабрика големов", "various", "различное", -1, -1, 10)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(4903)
    }

    @Test
    fun `Get dwelling value, special dwelling input 1, number of unit zones modified, returns success`() {

        val dwelling = Dwelling("Elemental conflux", "Сопряжение стихий", "various", "различное", -1, -1, 10)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 2.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(5623)
    }

    @Test
    fun `Get dwelling value, special dwelling input 2, number of unit zones modified, returns success`() {

        val dwelling = Dwelling("Golem factory", "Фабрика големов", "various", "различное", -1, -1, 10)
        val numberOfZones = 5.0f
        val numberOfUnitZones = 2.0f

        val value = getDwellingValueUseCase(
            dwelling,
            numberOfZones,
            numberOfUnitZones,
        )
        assertThat(value.data).isEqualTo(5809)
    }


}