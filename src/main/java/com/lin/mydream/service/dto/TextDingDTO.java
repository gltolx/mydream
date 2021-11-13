package com.lin.mydream.service.dto;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;
import java.util.Map;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 文本钉消息
 */
@Getter
@Setter
@NoArgsConstructor
public class TextDingDTO extends BaseDingMsgDTO {

    private Map<String, Object> at;
    private Map<String, Object> text;

    @Builder
    public TextDingDTO(String content, Boolean atAll, String mobiles) {
        super.setMsgtype("text");
        this.text = ImmutableMap.of("content", content);
        List<String> mobileList = Splitter.on(",").omitEmptyStrings().splitToList(mobiles);
        this.at = ImmutableMap.of("isAtAll", BooleanUtils.isTrue(atAll), "atMobiles", mobileList);
    }

}
