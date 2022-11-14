package com.valerytimofeev.h3pand.ui.contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.R
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
        
        Column() {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .clickable {
                        contactViewModel.sendEmail(context = context)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    Icons.Default.MailOutline,
                    modifier = Modifier
                        .size(24.dp),
                    contentDescription = "Mail"
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = contactViewModel.getLocalizedTextUseCase(contactViewModel.mailText))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .clickable {
                        contactViewModel.openDiscord(context = context)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    painterResource(id = R.drawable.ic_discord),
                    modifier = Modifier
                        .size(24.dp),
                    contentDescription = "Discord",
                    tint = Color(0xFF5865F2)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = contactViewModel.getLocalizedTextUseCase(contactViewModel.discordText))
            }
        }
    }
}