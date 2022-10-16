package xyz.catfootbeats.Listener

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.scopeWith
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.nextMessage
import xyz.catfootbeats.Control
import xyz.catfootbeats.Control.checkPermission
import xyz.catfootbeats.config.CommandConfig
import xyz.catfootbeats.config.Setting

fun SayListenerRegister() {
    GlobalEventChannel.subscribeGroupMessages{
        always {
            CommandConfig.say.startWith(message.contentToString()).let {
                if (it != null){
                    if(checkPermission(sender)){
                        val msg = it.ifEmpty {
                            group.sendMessage(message.quote() + Setting.nullTip)
                            nextMessage().contentToString()
                        }//掐掉头
                        if (message[Image]!=null){
                            val image:Image? = message[Image]
                            group.sendMessage(PlainText(msg)+ image!!)
                        }else{
                            group.sendMessage(msg)
                        }
                    }else{
                        group.sendMessage(Setting.tip)
                    }
                }
            }
        }
    }
}

object Command : CompositeCommand(
    Control, "contral",
    description = "示例指令",
    //prefixOptional = true // 还有更多参数可填, 此处忽略
) {

    // [参数智能解析]
    //
    // 在控制台执行 "/contral <群号>.<群员> <持续时间>",
    // 或在聊天群内发送 "/contral <@一个群员> <持续时间>",
    // 或在聊天群内发送 "/contral <目标群员的群名> <持续时间>",
    // 或在聊天群内发送 "/contral <目标群员的账号> <持续时间>"
    // 时调用这个函数
    @SubCommand
    suspend fun CommandSender.say(group: Group,msg:MessageChain) { // 通过 /manage mute <target> <duration> 调用

    }

    @SubCommand
    suspend fun CommandSender.list() { // 执行 "/manage list" 时调用这个函数
        sendMessage("/manage list 被调用了")
    }

    // 支持 Image 类型, 需在聊天中执行此指令.
    @SubCommand
    suspend fun CommandSender.test(image: Image) { // 执行 "/manage test <一张图片>" 时调用这个函数
        sendMessage("/manage image 被调用了, 图片是 ${image.imageId}")
    }
}
private fun MutableList<String>.startWith(contentToString: String): String? {
    this.forEach {
        if (contentToString.startsWith(it)) {
            return contentToString.replace(it, "").replace(" ", "")
        }
    }
    return null
}
