package com.lin.mydream.service.dto;

import com.google.common.base.Splitter;
import com.lin.mydream.util.CommonUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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
    /**
     * 命令体数组
     */
    private List<String> bodies;
    /**
     * 消息上下文
     */
    private MsgContext msgContext;
    /**
     * 机器人ID
     */
    private Long robotId;

    public String originString() {
        return CommonUtil.format("{} - {}", head, body);
    }


    public String ogt() {
        return getOgt();
    }

    public String head() {
        return getHead();
    }

    public String body() {
        return getBody();
    }

    public Command newMsgContext(Map<String, Object> ctxMap) {
        MsgContext c = new MsgContext();
        if (ctxMap != null) {
            c.setMsgId((String) ctxMap.get("msgId"));
            c.setAdmin((Boolean) ctxMap.get("isAdmin"));
            c.setSessionWebhookExpiredTime((Long) ctxMap.get("sessionWebhookExpiredTime"));
            c.setCreateAt((Long) ctxMap.get("createAt"));
            c.setConversationType((String) ctxMap.get("conversationType"));
            c.setConversationTitle((String) ctxMap.get("conversationTitle"));
            c.setSessionWebhook((String) ctxMap.get("sessionWebhook"));
            c.setSenderId((String) ctxMap.get("senderId"));
            c.setSenderNick((String) ctxMap.get("senderNick"));
        }
        this.setMsgContext(c);
        return this;
    }


    public List<String> extractKeysFromBody() {
        return CommonUtil.orEmpty(() -> Splitter
                .on(body.contains("'") ? "'" : "\"")
                .omitEmptyStrings()
                .trimResults()
                .splitToList(body));
    }

    public static void main(String[] args) {
//        Command command = Command.builder().build();
//        command.setBody("'feY测试' '2022-02-14' '17826833386,13639853155'");
//        List<String> strings = command.extractKeysFromBody();
//        System.out.println(strings);
    }

    ///////////////////////
    // 消息上下文信息
    ///////////////////////
    @Data
    public static class MsgContext {
        private String msgId;
        private boolean admin;
        private long sessionWebhookExpiredTime;
        private long createAt;
        /**
         * 群类型
         */
        private String conversationType;
        /**
         * 群名
         */
        private String conversationTitle;
        /**
         * 当前消息会话
         */
        private String sessionWebhook;
        private String senderId;
        private String senderNick;
    }
}
