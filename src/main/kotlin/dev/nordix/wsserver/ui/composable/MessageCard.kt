package dev.nordix.wsserver.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.wsserver.server.Message
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MessageCard(item: Message) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        shape = RoundedCornerShape(5),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = item.text, style = MaterialTheme.typography.body1)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = DateTimeFormatter.ofPattern("HH:mm:ss").format(item.time.atZone(ZoneId.systemDefault())),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    text = item.from,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
