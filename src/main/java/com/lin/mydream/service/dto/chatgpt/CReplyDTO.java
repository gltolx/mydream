package com.lin.mydream.service.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CReplyDTO {
    private static final String ERR_MSG = "sorry, my mind is blank.";

    private boolean success;
    private String content;

    public static CReplyDTO fail() {
        return new CReplyDTO(false, ERR_MSG);
    }

    public static CReplyDTO success(String content) {
        return new CReplyDTO(true, content);
    }
}
