package com.compose.wanandroid.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.compose.wanandroid.ui.theme.AppTheme
import com.compose.wanandroid.ui.theme.progress

@Preview
@Composable
fun ProgressDialogPreview() {
    AppTheme {
        ProgressDialog {

        }
    }
}

@Composable
fun ProgressDialog(text: String = "加载中...", onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(120.dp)
                .background(AppTheme.colors.secondaryBackground, shape = RoundedCornerShape(10.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                strokeWidth = 3.dp,
                color = AppTheme.colors.progress
            )
            Text(
                text = text,
                style = AppTheme.typography.body1,
                color = AppTheme.colors.textPrimary.copy(ContentAlpha.disabled),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}