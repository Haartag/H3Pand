package com.valerytimofeev.h3pand.ui.contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar

@Composable
fun ContactScreen(
    navController: NavController,
    contactViewModel: ContactViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MainTopBar(
            title = contactViewModel.contactTitleText,
            buttonIcon = {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back button",
                )
            },
            onButtonClicked = { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    contactViewModel.sendEmail(context = context)
                }
        ) {
            Icon(
                Icons.Default.MailOutline,
                contentDescription = "Mail"
            )
            Text(text = contactViewModel.getLocalizedTextUseCase(contactViewModel.mailText))
        }

    }
}