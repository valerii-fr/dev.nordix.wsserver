package dev.nordix.wsserver.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.wsserver.devices.DeviceType
import dev.nordix.wsserver.server.model.ConnectedDevice
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DevicesDrawer(
    devices: Set<ConnectedDevice>
) {
    val lazyListSState = rememberLazyListState()

    LazyColumn(
        state = lazyListSState,
        modifier = Modifier.width(250.dp).fillMaxHeight(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = devices.toList(),
            key = { it.name + it.host },
            itemContent = { ConnectedDeviceCard(it) }
        )
    }
}

@Composable
private fun ConnectedDeviceCard(
    device: ConnectedDevice
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(5)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "host: ${device.host}", style = MaterialTheme.typography.caption)
                Text(text = "name: ${device.name}", style = MaterialTheme.typography.caption)
                Text(
                    text = device.connectedAt.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_TIME),
                    style = MaterialTheme.typography.caption
                )
            }
            Icon(
                imageVector = device.type.icon,
                contentDescription = null,
            )
        }
    }
}