package xyz.catfootbeats.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Setting:ReadOnlyPluginConfig("Setting") {
    @ValueDescription(
        """
                  ! ! !警告! ! !
        修改以下选项可能导致你的机器人被其他人控制
        控制模式
        0 主人可以控制
        1 管理员也可以控制
        2 拥有权限(admin)者可控
        """
    )
    val mode by value(1)
    @ValueDescription("主人的QQ号")
    val masterID by value(mutableListOf<Long>(1145141919810))
    @ValueDescription("没有权限时的提示词")
    val tip by value("我才不会听你的话呢!(σ｀д′)σ")
    @ValueDescription("没有内容时的提醒次")
    val nullTip by value("喂喂，什么都没有啊！(￣_,￣ )")
}