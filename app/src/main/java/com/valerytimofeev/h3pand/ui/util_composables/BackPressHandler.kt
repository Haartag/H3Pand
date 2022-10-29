package com.valerytimofeev.h3pand.ui.util_composables

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

/**
 * Handle BackPress and replace it with [onBackPressed] lambda
 */
@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {

    val currentOnBackPressed = rememberUpdatedState(newValue = onBackPressed)
    val backCallback = remember {
        object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed.value()
            }
        }
    }
    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}