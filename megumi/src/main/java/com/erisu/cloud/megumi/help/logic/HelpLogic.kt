package com.erisu.cloud.megumi.help.logic

import cn.hutool.core.collection.CollUtil
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

/**
 *@Description help logic
 *@Author alice
 *@Date 2021/10/11 10:43
 **/
@Component
class HelpLogic {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    fun getModelList(): PlainText {
        val beans = applicationContext.getBeansWithAnnotation(Model::class.java)
        if (CollUtil.isEmpty(beans)) {
            return PlainText("我还什么都不会呢>.<")
        }
        var helpInfo = "alice搭载服务有这些~\n"
        beans.forEach { (_, v) ->
            val model = AnnotationUtils.findAnnotation(v.javaClass, Model::class.java)!!
            val name = if (model.name != "") {
                model.name
            } else v.javaClass.name

            helpInfo += if (model.isEnabled) {
                "\uD83D\uDFE2${name}\n"
            } else {
                "\uD83D\uDD34${name}\n"
            }
        }
        helpInfo += "发送\"帮助 [服务]\"可以查看对应服务指令哦"
        return PlainText(helpInfo)
    }

    fun getHelpInfo(modelName: String): PlainText {
        val beans = applicationContext.getBeansWithAnnotation(Model::class.java)
        val filter = beans.filterValues {
            val model = AnnotationUtils.findAnnotation(it.javaClass, Model::class.java)!!
            model.name == modelName || it.javaClass.name == modelName
        }
        return if (CollUtil.isEmpty(filter)) {
            PlainText("没有找到该模块，再检查下看看吧~")
        } else if (filter.size >= 2) {
            PlainText("存在相同模块，怎么回事呢\uD83E\uDD15")
        } else {
            val modelClass = filter.values.toList()[0]
            val modelAnnotation = AnnotationUtils.findAnnotation(modelClass.javaClass, Model::class.java)!!
            if (modelAnnotation.help == "") {
                PlainText("该用户很懒，什么都没有写")
            } else {
                PlainText(modelAnnotation.help.trim())
            }
        }

    }
}