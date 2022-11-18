package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class GetChosenGuardRangeUseCaseTest() {

    private lateinit var getChosenGuardRange: GetChosenGuardRangeUseCase

    @Before
    fun setup() {
        getChosenGuardRange = GetChosenGuardRangeUseCase()
    }

    @Test
    fun `get chosen guard range, index input`() {
        val result = getChosenGuardRange(2)
        assertThat(result).isEqualTo(5..9)
    }

    @Test
    fun `get chosen guard range, wrong index input`() {
        val result = getChosenGuardRange(20)
        assertThat(result).isEqualTo(-1..-1)
    }

    @Test
    fun `get chosen guard range, range input`() {
        val result = getChosenGuardRange(28..64)
        assertThat(result).isEqualTo(28..64)
    }
}