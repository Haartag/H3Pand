package com.valerytimofeev.h3pand.domain.use_case

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.valerytimofeev.h3pand.BuildConfig
import com.valerytimofeev.h3pand.domain.model.CurrentLocal

/**
 * Intent to open the default email client and start a new email to the developer with the
 * application version and in-app locale template at the beginning of the email body.
 */
class SendEmailUseCase {
    operator fun invoke(
        context: Context
    ): Boolean {
        val recipient =
            arrayOf("llin.special@gmail.com") //If it is a String, the recipient in gmail will be empty.
        val subject = "Heroes Helper in-app mail form: "
        val body = "appVersion: ${BuildConfig.VERSION_NAME} :${CurrentLocal.local} \n\n"

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")

        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            return false
        }
        return true
    }
}