package xyz.catfootbeats.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import xyz.catfootbeats.Control
import xyz.catfootbeats.Control.reload
import kotlin.concurrent.timer

object Setting:ReadOnlyPluginConfig("Setting") {
    private val file = Control.resolveConfigFile("Setting.yml")
    private var lastModify = file.lastModified()
    @Suppress("unused")
    private var loader = timer("config-reloader",true,1000,1000){
        if (file.lastModified()!= lastModify){
            reload()
            lastModify = file.lastModified()
        }
    }

    @ValueDescription(
        """
                  ! ! !警告! ! !
        修改以下选项可能导致你的机器人被其他人控制
        控制模式
        0 主人可以控制
        1 拥有权限(admin)者可控
        修改后可自动加载变更
        """
    )
    val mode by value(0)
    @ValueDescription("主人的QQ号")
    val masterID by value(mutableListOf<Long>(1145141919810))
    @ValueDescription("没有权限时的提示词")
    val tip by value("我才不会听你的话呢!(σ｀д′)σ")
    @ValueDescription("没有内容时的提醒次")
    val nullTip by value("喂喂，什么都没有啊！(￣_,￣ )")
    @ValueDescription("没有此群的提醒词")
    val noGroup by value("没有加入这个群哦(～￣▽￣)～,或者。。被禁言了w(ﾟДﾟ)w")
    @ValueDescription("无权撤回")
    val notRecall by value("没有权力撤回别人的发言")
    @ValueDescription("撤回超时(不知道为啥这玩意用不了)")
    val outOffTimeRecall by value("发言就像泼出去的水，收不回来啦")
    @ValueDescription("取消发送的关键字")
    val cancelWord by value("取消")
    @ValueDescription("取消发送成功")
    val canceled: String by value("好哒")
    @ValueDescription("获取下一条消息(send指令)时的提示语句")
    val waitingNextMessage by value("请发送需要发送的消息，如需取消请发送 '$cancelWord' (记得去掉单引号ヾ(•ω•`)o)")
    @ValueDescription("获取下一条消息(send指令)时的等待时间")
    val waitTime: Long by value(30*1000L)
    @ValueDescription("通过回复撤回消息时，若没有找到回复消息")
    val referenceNotFound: String by value("没有找到你说的这条消息呢ㄟ( ▔, ▔ )ㄏ")
    @ValueDescription("撤回指令的前缀")
    val recallCommand: MutableList<String> by value(mutableListOf("/recall"))


}