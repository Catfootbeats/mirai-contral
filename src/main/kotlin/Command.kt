package xyz.catfootbeats

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.findDuplicate
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.toMessageChain
import xyz.catfootbeats.Control.checkPermission
import xyz.catfootbeats.config.Setting

object Command : CompositeCommand(Control,"ctrl",description = "控制插件") {
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
    suspend fun CommandSenderOnMessage<*>.help(){
        val subject = fromEvent.subject
        val text =
            """
            控制插件
            版本：v0.2.0
            由Catfootbeats开发
            食用方法：
            /ctrl 主命令(唱跳rap篮球?)
            /ctrl help 获取帮助(其实你早就知道了吧，不然你咋进来的)
            /ctrl send <消息> <目标群(空时为本群)>
            /recall <引用> 让Bot撤回自己的消息
            """.trimIndent()
        if (user == null){
            Control.logger.info(text)
        }else{
            subject.sendMessage(text)
        }
    }
    @SubCommand("send")
    suspend fun CommandSenderOnMessage<*>.send(msg: Message?,target: Group? = null){
        val subject = fromEvent.subject
        if (user == null){
            Control.logger.info("不要在控制台发啊喂(σ｀д′)σ")
        }else{
            if (target == null){
                if (checkPermission(user!!)){
                    if (msg == null){
                        subject.sendMessage(Setting.nullTip)
                    }else{
                        subject.sendMessage(msg)
                    }
                }else{
                    subject.sendMessage(Setting.tip)
                }
            }else{
                try {
                    if (checkPermission(user!!)){
                        if (msg == null){
                            subject.sendMessage(Setting.nullTip)
                        }else{
                            target.sendMessage(msg)
                        }
                    }else{
                        target.sendMessage(Setting.tip)
                    }
                }catch (e:Exception){
                    subject.sendMessage(Setting.noGroup)
                }
            }
        }

    }
}