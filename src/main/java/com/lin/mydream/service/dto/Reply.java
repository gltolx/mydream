package com.lin.mydream.service.dto;

import com.lin.mydream.model.enumerate.RobotEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class Reply {
    /**
     * 文本内容
     */
    private String content;
    /**
     * markdown，link，text
     */
    private RobotEnum.MsgType msgType;
    /**
     * markdown title（非必需）
     */
    private String mdTitle;

    public Reply content(String content) {
        this.content = content;
        return this;
    }

    public Reply msgType(RobotEnum.MsgType msgType) {
        this.msgType = msgType;
        return this;
    }

    public Reply mdTitle(String mdTitle) {
        this.mdTitle = mdTitle;
        return this;
    }

    public String content() {
        return content;
    }

    public RobotEnum.MsgType msgType() {
        return msgType;
    }

    public String mdTitle() {
        return mdTitle;
    }

    public static Reply of(String content) {
        return Reply.of(content, null);
    }

    public static Reply of(String content, RobotEnum.MsgType msgType) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setMsgType(msgType == null ? RobotEnum.MsgType.text : msgType);
        return reply;
    }
}
