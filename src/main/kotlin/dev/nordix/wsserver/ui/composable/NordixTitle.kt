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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import dev.nordix.wsserver.server.model.KtorStatus
import me.sample.library.resources.Res
import me.sample.library.resources.nordix
import me.sample.library.resources.title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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
                imageVector = vectorResource(Res.drawable.nordix),
                contentDescription = null,
                modifier = Modifier.padding(12.dp).fillMaxHeight()
            )
            Column {
                Text(
                    text = stringResource(Res.string.title),
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
