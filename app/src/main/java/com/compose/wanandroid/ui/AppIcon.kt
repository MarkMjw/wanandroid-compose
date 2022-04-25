package com.compose.wanandroid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.wanandroid.R
import com.compose.wanandroid.ui.theme.icon

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun PreviewAppIcon() {
    AppIcon()
}

@Composable
private fun AppIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .background(MaterialTheme.colors.icon, RoundedCornerShape(50.dp))
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().padding(30.dp)
        )
    }
}
