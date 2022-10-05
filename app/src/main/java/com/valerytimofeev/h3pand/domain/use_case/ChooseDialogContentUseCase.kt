package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.utils.Dialog
import com.valerytimofeev.h3pand.utils.DialogSettings


class ChooseDialogContentUseCase() {
    operator fun invoke(
        dialogType: String
    ): Dialog<DialogSettings> {
        return try {
            Dialog.opened(DialogSettings.valueOf(dialogType))
        } catch (e: Exception) {
            Dialog.closed(null)
        }

    }
}
