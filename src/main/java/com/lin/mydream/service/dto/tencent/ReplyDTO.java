package com.lin.mydream.service.dto.tencent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/12/25.
 * @desc 腾讯闲聊回复数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    /**
     * 闲聊回复
     */
    private String reply;
    /**
     * 自信度
     */
    private Float confidence;
}
