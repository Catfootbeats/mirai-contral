package xyz.catfootbeats.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object CommandConfig:ReadOnlyPluginConfig("Command") {
    @ValueDescription("修改触发命令")
    val say: MutableList<String> by value(mutableListOf("/发送"))
    val withdraw: MutableList<String> by value(mutableListOf("/撤回"))
    val addCommand: MutableList<String> by value(mutableListOf("/添加"))
}