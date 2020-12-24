package com.erisu.cloud.megumi.command;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @Description request scope
 * @Author alice
 * @Date 2020/12/24 17:12
 **/
@Data
@RequestScope
@Service
public class CommandService {
    private ICommandService commandService;
}
