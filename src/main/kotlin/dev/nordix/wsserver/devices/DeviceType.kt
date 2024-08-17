package dev.nordix.wsserver.devices

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.ui.graphics.vector.ImageVector

enum class DeviceType(val typeName: String, val icon: ImageVector) {
    Button("button", Icons.Default.TouchApp),
    YtIntegration("yt_integration", Icons.Default.Analytics)
}

enum class DeviceActions(val actionName: String) {
    Click("click"),
    DoubleClick("double_click"),
    LongClick("long_click"),
}

enum class MessageTypes(val typeName: String) {
    Presentation("presentation"),
    Action("action"),
}
