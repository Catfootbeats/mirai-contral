package xyz.catfootbeats

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.QuoteReply
import xyz.catfootbeats.config.Setting

fun recallListener() {
    GlobalEventChannel.subscribeMessages {
        always {
            if (message.contentToString().startsWith("/recall")){
                if (Control.checkPermission(sender!!)){
                    val quoteReply = message[QuoteReply]
                    if (quoteReply == null){
                        subject.sendMessage("没有找到引用")
                    }else{
                        if (quoteReply.source.fromId == bot.id){
                            try {
                                quoteReply.source.recall()
                            }catch (e:Exception){
                                subject.sendMessage(Setting.outOffTimeRecall)
                            }
                        }else{
                            subject.sendMessage(Setting.notRecall)
                        }
                    }
                }else{
                    subject.sendMessage(Setting.tip)
                }
            }
        }
    }
}
private fun String.startsWith(msg: MutableList<String>): Boolean {
    msg.forEach {
        if (this.startsWith(it))
            return true
    }
    return false
}