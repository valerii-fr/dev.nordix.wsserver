package dev.nordix.wsserver.ui.composable

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NordixIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}
