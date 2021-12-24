package com.lin.mydream.service.dto.tencent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.Optional;

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

    /**
     * 脱敏
     */
    public ReplyDTO desensitization() {
        Optional.ofNullable(this.reply)
                .ifPresent(
                        x -> {
                            String desReply = x.replaceAll("腾讯", Strings.EMPTY);
                            this.setReply(desReply);
                        }
                );
        return this;
    }
}
