package com.lin.mydream.service.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 链接钉消息
 */
@Data
@NoArgsConstructor
public class LinkDingDTO extends BaseDingMsgDTO{

    private Map<String, String> link;

    @Builder
    public LinkDingDTO(String text, String title, String picUrl, String msgUrl) {
        super.setMsgtype("link");
        link = ImmutableMap.of(
                "text", text
                , "title", title
                , "picUrl", picUrl
                , "msgUrl", msgUrl
        );
    }
}
