package com.valerytimofeev.h3pand.domain.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test


class GetItemImageAndColorUseCaseAndroidTest() {

    lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun getImgValidInput () {
        val result = GetItemImageAndColorUseCase("exp")

        Truth.assertThat(result.getItemImage()).isEqualTo(context.resources.getIdentifier("item_exp", "drawable", "com.valerytimofeev.h3pand"))
    }

    @Test
    fun getImgInvalidInput () {
        val result = GetItemImageAndColorUseCase("test")

        Truth.assertThat(result.getItemImage()).isEqualTo(context.resources.getIdentifier("ic_dice", "drawable", "com.valerytimofeev.h3pand"))
    }

    @Test
    fun getAddValueImgValidInput () {
        val result = GetItemImageAndColorUseCase("Dwelling")

        Truth.assertThat(result.getAddValueImage()).isEqualTo(context.resources.getIdentifier("av_dwelling", "drawable", "com.valerytimofeev.h3pand"))
    }

    @Test
    fun getAddValueImgInvalidInput () {
        val result = GetItemImageAndColorUseCase("test")

        Truth.assertThat(result.getAddValueImage()).isEqualTo(context.resources.getIdentifier("ic_question", "drawable", "com.valerytimofeev.h3pand"))
    }

}