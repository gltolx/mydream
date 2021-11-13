package com.lin.mydream.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 基础钉消息-消息类型
 */
@Getter
@Setter
public abstract class BaseDingMsgDTO {

    /**
     * 消息类型 text/link/markdown
     */
    private String msgtype;
}
