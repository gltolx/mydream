package com.lin.mydream.model;

import com.lin.mydream.service.dto.BaseDingMsgDTO;
import com.lin.mydream.service.dto.LinkDingDTO;
import com.lin.mydream.service.dto.MarkdownDingDTO;
import com.lin.mydream.service.dto.TextDingDTO;
import com.lin.mydream.util.SignUtil;
import com.lin.mydream.util.SpringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc "robotx"为加长版"robot",
 * 寓意存放robot及其扩展关联信息和动作, 这个机器人具有发信息的能力
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class Robotx {

    /**
     * 机器人自身信息
     */
    private Robot self;

    public Robotx(Robot robot) {
        this.self = robot;
    }

    public Robot self() {
        return getSelf();
    }

    /**
     * 获取机器人webhook地址
     */
    public String makeWebhookUrl() {
        Pair<Long, String> makeSign = SignUtil.makeSign(self().getSign());
        Long timestamp = makeSign.getLeft();
        String sign = makeSign.getRight();
        return String.format("https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%d&sign=%s",
                self.getAccessToken(), timestamp, sign);
    }

    /**
     * 发送Text
     */
    public void sendText(TextDingDTO textDingDTO) {
        send(textDingDTO);
    }

    /**
     * 发送Link
     */
    public void sendLink(LinkDingDTO linkDingDTO) {
        send(linkDingDTO);
    }

    /**
     * 发送Markdown
     */
    public void sendMarkdown(MarkdownDingDTO markdownDingDTO) {
        send(markdownDingDTO);
    }

    /**
     * 机器人发送钉钉消息
     *
     * @param msgDTO 消息body
     */
    public <T extends BaseDingMsgDTO> void send(T msgDTO) {
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        String url = this.makeWebhookUrl();
        try {
            restTemplate.postForEntity(url, msgDTO, msgDTO.getClass());
        } catch (RestClientException e) {
            log.error("RestClient error, url:{}, msgDTO:{}", url, msgDTO);
        }
    }
}
