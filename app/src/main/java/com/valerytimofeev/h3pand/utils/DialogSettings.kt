package com.valerytimofeev.h3pand.utils

import androidx.compose.runtime.Composable
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.*


enum class DialogSettings(
    val header: @Composable () -> Unit,
    val body: @Composable () -> Unit,
) {
    GuardStageOne( {}, { ChooseGuardStage1() }),
    GuardStageTwo( {}, { ChooseGuardStage2() }),
    GuardStageThree( {}, { ChooseGuardStage3() }),

    AddValueStageOne( {}, { AddValueDialogStage1() }),
    AddValueStageTwo( {}, { AddValueDialogStage2() }),
    AddValueStageThree( {}, { AddValueDialogStage3() }),

    CastleZoneDialog( {}, { CastleZoneDialog() }),

    DwellingDialog( {}, { DwellingDialog() }),
    CustomValueDialog( {}, { CustomValueDialog() }),
}