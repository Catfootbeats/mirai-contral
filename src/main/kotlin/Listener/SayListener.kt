package xyz.catfootbeats.Listener

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.Image
import xyz.catfootbeats.Control.checkPermission
import xyz.catfootbeats.config.CommandConfig
import xyz.catfootbeats.config.Setting
//弃用了，还是注册指令好(￣_,￣ )
fun SayListenerRegister() {
    GlobalEventChannel.subscribeGroupMessages{
        always {
            CommandConfig.say.startWith(message.contentToString()).let {
                if (it != null){
                    if(checkPermission(sender)){
                        //虽然不会发出来[图片]，但是也只能发文字和图片了，而且图片位置始终在最后，而且。。我还没测试过(x
                        val msg = message.contentToString().drop(CommandConfig.say.toString().length+1)
                        val image = message[Image]
                        group.sendMessage(msg+image)
                        /*
                        有缺陷的办法，会发出来[图片]
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
                         */
                    }else{
                        group.sendMessage(Setting.tip)
                    }
                }
            }
        }
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
