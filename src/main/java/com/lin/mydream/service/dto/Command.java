package com.lin.mydream.service.dto;

import lombok.*;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/19.
 * @desc 机器人命令的参数
 */
@Builder
@Getter
@Setter
public class Command {
    /**
     * 用户@的群聊机器人，或者指定的单聊机器人的outgoingToken => ogt
     */
    private String ogt;
    /**
     * 命令头， - 前的内容，例如：acquire token
     */
    private String head;
    /**
     * 命令体， - 后的内容
     */
    private String body;
}
