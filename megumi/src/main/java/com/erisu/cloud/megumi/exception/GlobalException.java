package com.erisu.cloud.megumi.exception;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalException {
    @Value("${qq.username}")
    private long username;

    @ExceptionHandler(MegumiException.class)
    public void megumiExceptionHandler(MegumiException e) {
        Bot bot = Bot.getInstance(username);
        Contact subject = e.getSubject();
        if (subject instanceof Group) {
            Objects.requireNonNull(bot.getGroup(subject.getId())).sendMessage(e.getReturnMsg());
        }
    }

    @ExceptionHandler(Exception.class)
    public void defaultErrorHandler(Exception e) {
        try {
            log.error("全局异常处理：", e);
        } catch (Exception ex) {
            log.error("全局异常处理异常：", ex);
        }
    }

}
