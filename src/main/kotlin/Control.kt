package xyz.catfootbeats

import io.ktor.client.request.*
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.utils.info
import xyz.catfootbeats.Listener.SayListenerRegister
import xyz.catfootbeats.Net.closeClient
import xyz.catfootbeats.Net.normalClient
import xyz.catfootbeats.config.CommandConfig
import xyz.catfootbeats.config.Setting

object Control : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.catfootbeats.control",
        name = "Control",
        version = "0.1.1",
    ) {
        author("Catfootbeats")
        info("""控制你的Bot""")
    }
) {
    private lateinit var adminPermission: Permission
    override fun onEnable() {
        launch{
            checkUpdate()
        }
        CommandConfig.reload()
        Setting.reload()
        adminPermission = PermissionService.INSTANCE.register(
            PermissionId(name, "admin"),
            "管理员权限"
        )
        logger.info { "Plugin loaded!(～￣▽￣)～" }
        SayListenerRegister()
    }

    override fun onDisable() {
        closeClient()
    }
    fun checkPermission(sender:Member):Boolean{
        when(Setting.mode){
            0 -> {
                if (Setting.masterID.contains(sender.id))
                    return true
            }
            1 -> {
                if (Setting.masterID.contains(sender.id))
                    return true
                return sender.isOperator()
            }
            2 -> {
                return sender.permitteeId.hasPermission(adminPermission)
            }
            else -> {
                Control.logger.warning("权限模式设置错误w(ﾟДﾟ)w")
            }
        }
        return false
    }
    suspend fun checkUpdate() {
        val lastVersion:String =
            normalClient.get("https://cdn.jsdelivr.net/gh/Catfootbeats/mirai-contral@main/version.txt")
        if (lastVersion.equals(version.toString() + "\n")) {
            logger.info("当前版本为:${version}(￣︶￣)")
        }else{
            logger.info("当前版本为:${version},最新版本为:${lastVersion}w(ﾟДﾟ)w")
        }
    }
}