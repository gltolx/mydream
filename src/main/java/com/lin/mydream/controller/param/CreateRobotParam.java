package com.lin.mydream.controller.param;

import com.google.common.base.Splitter;
import com.lin.mydream.service.dto.Command;
import lombok.Data;

import java.util.List;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/15.
 * @desc 创建机器人参数
 */
@Data
public class CreateRobotParam {
    private String accessToken;
    private String sign;
    private String outgoingToken;


    public static CreateRobotParam of(Command command) {
        List<String> list = Splitter.on(",").omitEmptyStrings().splitToList(command.getBody());

        CreateRobotParam param = new CreateRobotParam();
        if (list.size() >= 3) {
            param.setAccessToken(list.get(0));
            param.setSign(list.get(1));
            param.setOutgoingToken(list.get(2));
        }
        return param;
    }
}
