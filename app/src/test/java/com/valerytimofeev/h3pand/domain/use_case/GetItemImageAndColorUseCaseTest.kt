package com.valerytimofeev.h3pand.domain.use_case

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.ui.theme.ExpColor
import org.junit.Test


class GetItemImageAndColorUseCaseTest {

    @Test
    fun `Get color, valid input` () {
        val result = GetItemImageAndColorUseCase("exp")

        assertThat(result.getItemColor()).isEqualTo(ExpColor)
    }

    @Test
    fun `Get color, invalid input` () {
        val result = GetItemImageAndColorUseCase("test")

        assertThat(result.getItemColor()).isEqualTo(Color.White)
    }
}