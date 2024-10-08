package dev.nordix.wsserver.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.server.MessageRepo
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import dev.nordix.wsserver.ui.composable.DevicesDrawer
import dev.nordix.wsserver.ui.composable.MessageCard
import dev.nordix.wsserver.ui.composable.NordixIconButton
import dev.nordix.wsserver.ui.composable.NordixTitle
import org.koin.compose.koinInject

@Composable
fun WindowScope.NordixApp(
    onClose: () -> Unit,
    windowState: WindowState,
) {
    val collapsedSize = DpSize(DefaultWidth, TopBarHeight)
    val expandedSize = DpSize(DefaultWidth, ExpandedHeight)
    var expandConnections by remember { mutableStateOf(false) }

    val observer = koinInject<KtorLifecycleObserver>()
    val server = koinInject<CommonServer>()
    val messageRepo = koinInject<MessageRepo>()

    val messages by messageRepo.messages.collectAsState()
    val ktorState by observer.currentStatus.collectAsState()
    val toggleState by server.toggleState.collectAsState()
    val connectedDevices by server.connectedHosts.collectAsState()

    val infiniteTransition = rememberInfiniteTransition()
    val toggleRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0F,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing)
        )
    )

    windowState.size = if (expandConnections) expandedSize else collapsedSize

    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(TopBarHeight),
                    title = { NordixTitle(
                        ktorState = ktorState,
                        connectedDevices = connectedDevices,
                        expandConnections = expandConnections,
                    ) },
                    actions = {
                        NordixIconButton(
                            icon = if (expandConnections) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            onClick = { expandConnections = !expandConnections }
                        )
                        if (expandConnections) {
                            NordixIconButton(
                                icon = Icons.Default.PlaylistRemove,
                                onClick = messageRepo::clear
                            )
                        }
                        NordixIconButton(
                            icon = if (connectedDevices.isNotEmpty()) {
                                if (toggleState) Icons.Default.ChangeCircle else Icons.Default.HourglassDisabled
                            } else {
                                Icons.Default.SensorsOff
                            },
                            onClick = { if (connectedDevices.isNotEmpty()) server.toggleAll() },
                            enabled = connectedDevices.isNotEmpty(),
                            modifier = Modifier.rotate(
                                if(toggleState) toggleRotation else 0f
                            )
                        )
                    },
                    navigationIcon = {
                        Row {
                            NordixIconButton(
                                icon = Icons.Default.Close,
                                onClick = onClose
                            )
                            NordixIconButton(
                                icon = Icons.Default.Minimize,
                                onClick = { windowState.isMinimized = windowState.isMinimized.not() }
                            )
                        }
                    },
                )
            }
        ) { paddingValues ->
            if (expandConnections) {
                Row(modifier = Modifier.fillMaxWidth().height(600.dp).padding(paddingValues)) {
                    DevicesDrawer(connectedDevices)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
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
    }
}

val DefaultWidth = 800.dp
val TopBarHeight = 48.dp
private val ExpandedHeight = 800.dp
