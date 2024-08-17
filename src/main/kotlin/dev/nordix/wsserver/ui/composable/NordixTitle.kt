package dev.nordix.wsserver.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import dev.nordix.wsserver.server.model.ConnectedDevice
import dev.nordix.wsserver.server.model.KtorStatus
import me.sample.library.resources.Res
import me.sample.library.resources.nordix
import me.sample.library.resources.title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WindowScope.NordixTitle(
    ktorState: KtorStatus,
    connectedDevices: Set<ConnectedDevice>,
    expandConnections: Boolean
) {
    WindowDraggableArea {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            if (connectedDevices.isNotEmpty() && !expandConnections) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    connectedDevices.forEach { device ->
                        Icon(
                            imageVector = device.type.icon,
                            contentDescription = device.type.typeName
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
            }
            Image(
                painter = painterResource(Res.drawable.nordix),
                contentDescription = null,
                modifier = Modifier.padding(8.dp).height(48.dp).width(130.dp),
                contentScale = ContentScale.Fit
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
