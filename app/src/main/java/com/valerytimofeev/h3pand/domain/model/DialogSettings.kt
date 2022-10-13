package com.valerytimofeev.h3pand.domain.model

import androidx.compose.runtime.Composable
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.*

/**
 * Contains header and body pairs for [DialogScreen].
 */
enum class DialogSettings(
    val header: @Composable () -> Unit,
    val body: @Composable () -> Unit,
) {
    CLOSED({ }, { }),

    GuardStageOne( { SearchButtonHeader(SearchType.GUARD) }, { ChooseGuardStage1() }),
    GuardStageTwo( {  }, { ChooseGuardStage2() }),
    GuardStageThree( {  }, { ChooseGuardStage3() }),
    GuardSearch( { SearchGuardHeader() }, { SearchGuardDialog() }),

    AddValueStageOne( { SearchButtonHeader(SearchType.ADDVALUE) }, { AddValueDialogStage1() }),
    AddValueStageTwo( {}, { AddValueDialogStage2() }),
    AddValueStageThree( {}, { AddValueDialogStage3() }),
    AddValueSearch( { SearchAddValueHeader() }, { SearchAddValueDialog() }),

    CastleZoneDialog( {}, { CastleZoneDialog() }),

    DwellingDialog( {}, { DwellingDialog() }),
    CustomValueDialog( {}, { CustomValueDialog() }),
}