package com.lin.mydream.service.dto;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;
import java.util.Map;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc Markdown钉消息
 *
 */
@NoArgsConstructor
@Data
public class MarkdownDingDTO extends BaseDingMsgDTO{

    private Map<String, String> markdown;
    private Map<String, Object> at;

    /*
    目前仅支持Markdown部分语法, 支持的元素如下:
        标题
        # 一级标题
        ## 二级标题
        ### 三级标题
        #### 四级标题
        ##### 五级标题
        ###### 六级标题

        引用
        > A man who stands for nothing will fall for anything.

        文字加粗、斜体
        **bold**
        *italic*

        链接
        [this is a link](http://name.com)

        图片
        ![](http://name.com/pic.jpg)

        无序列表
        - item1
        - item2

        有序列表
        1. item1
        2. item2

     */
    @Builder
    public MarkdownDingDTO(String title, String markdownText, Boolean atAll, String atMobiles) {
        super.setMsgtype("markdown");
        this.markdown = ImmutableMap.of("title", title, "text", markdownText);

        List<String> mobileList = Splitter.on(",").omitEmptyStrings().splitToList(atMobiles);
        this.at = ImmutableMap.of("isAtAll", BooleanUtils.isTrue(atAll), "atMobiles", mobileList);
    }
}
