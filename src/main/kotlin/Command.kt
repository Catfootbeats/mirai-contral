package xyz.catfootbeats

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.message.nextMessageOrNull
import xyz.catfootbeats.Control.checkPermission
import xyz.catfootbeats.config.Setting

@Suppress("unused")
object Command : CompositeCommand(Control, "ctrl", description = "控制插件") {
    //    @SubCommand("recall")
//    suspend fun CommandSenderOnMessage<*>.recall(){
//        val subject = fromEvent.subject
//        if (user == null){
//            Control.logger.info("不要在控制台发啊喂(σ｀д′)σ")
//        }else{
//            subject.sendMessage("请引用撤回信息")
//            recallListener()
//        }
//    }
    @SubCommand("help")
    suspend fun CommandSender.help() {
        val text = """
            控制插件
            版本：v0.2.0
            由Catfootbeats开发
            食用方法：
            /ctrl 主命令(唱跳rap篮球?)
            /ctrl help 获取帮助(其实你早就知道了吧，不然你咋进来的)
            /ctrl send <消息> <目标群(空时为本群)>
            /recall <引用> 让Bot撤回自己的消息
            """.trimIndent()
        sendMessage(text)
    }

    @SubCommand("send")
    suspend fun CommandSenderOnMessage<*>.send(msg: String, target: Group? = null) {
        //CommandSenderOnMessage不会在控制台被调用
        Control.logger.debug("msg = [${msg}], target = [${target}]")
        val messageChain = if (msg=="null") null else msg
        if (target == null) {
            if (checkPermission(user!!)) {
                if (messageChain == null) {
                    sendMessage(Setting.waitingNextMessage)
                    fromEvent.nextMessageOrNull(Setting.waitTime, priority = EventPriority.HIGHEST) {
                        if (it.message.content.matches(Setting.cancelWord.toRegex())) throw CancelSendException()
                        true
                    }?.sendTo(subject!!) ?: sendMessage(Setting.nullTip)
                } else {
                    sendMessage(messageChain.deserializeMiraiCode(subject))
                }
            } else {
                sendMessage(Setting.tip)
            }
        } else {
            try {
                if (checkPermission(user!!)) {
                    if (messageChain == null) {
                        sendMessage(Setting.waitingNextMessage)
                        fromEvent.nextMessageOrNull(Setting.waitTime, priority = EventPriority.HIGHEST) {
                            if (it.message.content.matches(Setting.cancelWord.toRegex())) throw CancelSendException()
                            true
                        }?.sendTo(target) ?: sendMessage(Setting.nullTip)
                    } else {
                        target.sendMessage(messageChain.deserializeMiraiCode(subject))
                    }
                } else {
                    target.sendMessage(Setting.tip)
                }
            } catch (e: CancelSendException) {
                sendMessage(Setting.canceled)
            } catch (e: Exception) {
                Control.logger.error(e)
                sendMessage(Setting.noGroup)
            }
        }
    }

    @SubCommand("send")
    suspend fun ConsoleCommandSender.send(msg: String, target: Group? = null) {
        Control.logger.debug("msg = [${msg}], target = [${target}]")
        val messageChain = if (msg=="null") null else msg
        //控制台调用
        if (target == null) {
            if (messageChain == null) {
                sendMessage(Setting.nullTip)
            } else {
                sendMessage(messageChain)
            }

        } else {
            try {
                if (messageChain == null) {
                    sendMessage(Setting.nullTip)
                } else {
                    target.sendMessage(messageChain.deserializeMiraiCode(target))
                }
            } catch (e: Exception) {
                Control.logger.error(e)
                sendMessage(Setting.noGroup)
            }
        }
    }


}