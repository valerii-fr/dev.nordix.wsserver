package dev.nordix.wsserver.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.server.MessageRepo
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import dev.nordix.wsserver.ui.composable.MessageCard
import dev.nordix.wsserver.ui.composable.NordixIconButton
import dev.nordix.wsserver.ui.composable.NordixTitle
import org.koin.compose.koinInject

@Composable
fun WindowScope.app(
    onClose: () -> Unit,
) {

    val observer = koinInject<KtorLifecycleObserver>()
    val server = koinInject<CommonServer>()
    val messageRepo = koinInject<MessageRepo>()

    val messages by messageRepo.messages.collectAsState()
    val ktorState by observer.currentStatus.collectAsState()
    val toggleState by server.toggleState.collectAsState()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { NordixTitle(ktorState) },
                    actions = {
                        NordixIconButton(
                            icon = Icons.Default.Delete,
                            onClick = messageRepo::clear
                        )
                        NordixIconButton(
                            icon = if (!toggleState) Icons.Default.PlayArrow else Icons.Default.Lock,
                            onClick = server::toggle
                        )
                    },
                    navigationIcon = {
                        NordixIconButton(
                            icon = Icons.Default.Close,
                            onClick = onClose
                        )
                    },
                )
            },
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(
                    items = messages,
                    key = { it.time},
                    itemContent = { MessageCard(it) }
                )
            }
        }
    }
}
