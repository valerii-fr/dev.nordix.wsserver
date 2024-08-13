package dev.nordix.wsserver.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import dev.nordix.wsserver.Constants.SHORT_APP_TITLE
import dev.nordix.wsserver.server.KtorStatus

@Composable
fun WindowScope.NordixTitle(
    ktorState: KtorStatus
) {
    WindowDraggableArea {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource("nordix_w.png"),
                contentDescription = null,
                modifier = Modifier.padding(12.dp)
            )
            Column {
                Text(
                    text = SHORT_APP_TITLE,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Status: $ktorState",
                    style = MaterialTheme.typography.caption,
                    color = if (ktorState is KtorStatus.Stopped) Color.Red else Color.Unspecified
                )
            }
        }
    }
}
