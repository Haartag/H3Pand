package com.valerytimofeev.h3pand.ui.contact

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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
    val discordText = TextWithLocalization(
        enText = "Discord group",
        ruText = "Группа в Discord"
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

    fun openDiscord(context: Context) {
        val discordIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/jFs8vU7tpU"))
        try {
            context.startActivity(discordIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show()
        }
    }
}
