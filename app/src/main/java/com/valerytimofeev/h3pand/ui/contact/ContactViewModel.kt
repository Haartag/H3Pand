package com.valerytimofeev.h3pand.ui.contact

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.data.additional_data.TextStorage
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import com.valerytimofeev.h3pand.domain.use_case.SendEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    val getLocalizedTextUseCase: GetLocalizedTextUseCase,
    val sendEmailUseCase: SendEmailUseCase
) : ViewModel() {

    val contactTitleText = getLocalizedTextUseCase(TextStorage.ContactTitle.text)

    val mailText = TextWithLocalization(
        enText = "Contact by email",
        ruText = "Связаться по электронной почте"
    )

    fun sendEmail(context: Context) {
        val result = sendEmailUseCase(context)
        if (!result) {
            Toast.makeText(
                context,
                "Unexpected error when sending an email",
                Toast.LENGTH_SHORT)
                .show()
        }
    }
}
