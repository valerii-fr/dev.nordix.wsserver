package dev.nordix.wsserver.devices

enum class DeviceType(val typeName: String) {
    Button("button")
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
