package xyz.catfootbeats

import io.ktor.client.request.*
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.utils.info
import xyz.catfootbeats.Net.closeClient
import xyz.catfootbeats.Net.normalClient
import xyz.catfootbeats.config.Setting

object Control : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.catfootbeats.control",
        name = "Control",
        version = "0.3.0",
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
        Command.register()
        Setting.reload()
        adminPermission = PermissionService.INSTANCE.register(
            PermissionId(name, "admin"),
            "管理员权限"
        )
        AbstractPermitteeId.AnyUser.permit(Command.permission)
        recallListener()
        logger.info { "Plugin loaded!(～￣▽￣)～" }
    }

    override fun onDisable() {
        closeClient()
    }
    fun checkPermission(sender:User):Boolean{
        when(Setting.mode){
            0 -> {
                if (Setting.masterID.contains(sender.id))
                    return true
            }
            1 -> {
                return sender.permitteeId.hasPermission(adminPermission)
            }
            else -> {
                Control.logger.warning("权限模式设置错误w(ﾟДﾟ)w")
            }
        }
        return false
    }
    suspend fun checkUpdate() {
        try {
            val lastVersion:String =
                normalClient.get("https://cdn.jsdelivr.net/gh/Catfootbeats/mirai-contral@main/version.txt")
            if (lastVersion.equals(version.toString() + "\n")) {
                logger.info("当前版本为:${version}(￣︶￣)")
            }else{
                logger.info("当前版本为:${version},最新版本为:${lastVersion}w(ﾟДﾟ)w")
            }
        }catch (e:Exception){
            logger.warning("获取版本错误，当前版本${version}")
        }
    }
}