//package com.erisu.cloud.megumi.help
//
//import com.erisu.cloud.megumi.command.Command
//import com.erisu.cloud.megumi.command.CommandType
//import com.erisu.cloud.megumi.pattern.Pattern
//import com.erisu.cloud.megumi.plugin.pojo.Model
//import net.mamoe.mirai.contact.Contact
//import net.mamoe.mirai.contact.User
//import net.mamoe.mirai.message.data.Message
//import net.mamoe.mirai.message.data.MessageChain
//import org.springframework.stereotype.Component
//
///**
// * @Description get help info
// * @Author alice
// * @Date 2021/10/11 10:41
// */
//@Model
//@Component
//class HelpService {
//    @Command(commandType = CommandType.GROUP, value = "ll", pattern = Pattern.EQUALS)
//    @Throws(Exception::class)
//    suspend fun ll(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
//        return null
//    }
//}