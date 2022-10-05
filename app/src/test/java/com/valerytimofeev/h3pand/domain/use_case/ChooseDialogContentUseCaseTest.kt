package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.utils.Dialog
import com.valerytimofeev.h3pand.utils.DialogSettings
import com.valerytimofeev.h3pand.utils.DialogStatus
import org.junit.Before
import org.junit.Test

class ChooseDialogContentUseCaseTest {

    private lateinit var chooseDialogContentUseCase: ChooseDialogContentUseCase

    @Before
    fun setup() {
        chooseDialogContentUseCase = ChooseDialogContentUseCase()
    }

    @Test
    fun `Get dialog, valid input` () {
        val result = chooseDialogContentUseCase("GuardStageOne")
        Truth.assertThat(result).isEqualTo(Dialog(DialogStatus.OPENED, DialogSettings.GuardStageOne))
    }

    @Test
    fun `Get dialog, invalid input` () {
        val result = chooseDialogContentUseCase("Test")
        Truth.assertThat(result).isEqualTo(Dialog(DialogStatus.CLOSED, null))
    }

}