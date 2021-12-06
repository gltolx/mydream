package com.lin.mydream.controller;

import com.lin.mydream.component.ReplyRouter;
import com.lin.mydream.config.RobotProperties;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.model.Robot;
import com.lin.mydream.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/4.
 * @desc 测试接口类
 */
@Slf4j
@RestController
@RequestMapping(value = Mydreams.BASE_API + "ding")
public class DingController {

    @Autowired
    private ReplyRouter replyRouter;
    @Autowired
    private RobotProperties robotProperties;

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String hello(@RequestParam(value = "text") String text) {
        log.info("text: {}", text);
        return "~~~~~~~~~~~~~~~~~~~~~~~~\nhello my dream \njust received: " + text + "\n~~~~~~~~~~~~~~~~~~~~~~~~";
    }

    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public List<Robot> test() {
        log.info("robotProperties-dingtalk: {}", robotProperties.getDingRobots());

        return robotProperties.getDingRobots();
    }

    @RequestMapping(value = "channel", method = RequestMethod.POST)
    public String channel(@RequestBody Map<String, Object> map, HttpServletRequest request) {
//        map.forEach((k, v) -> log.info("key:{}, value:{}", k, v));
        log.info("ding channel - request:{}, requestURI:{}, requestURL:{}"
                , request.toString(), request.getRequestURI(), request.getRequestURL().toString());
        Long timestamp = CommonUtil.tryParseLong(request.getHeader("timestamp"));
        String sign = request.getHeader("sign");
        log.info("ding channel - header:[timestamp:{}, sign:{}], requestBody:{}", timestamp, sign, map);
//        SignUtil.checkSign(timestamp, sign, )
        String token = request.getParameter("token");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String h = headerNames.nextElement();
            log.info("ding channel - header:[{}], value:{}", h, request.getHeader(h));
        }
        log.info("ding channel - param:[token:{}]", token);
        Map<String, String> contentMap = (Map<String, String>) map.get("text");
        replyRouter.execute(token, contentMap.get("content"));

        return "OK~~~";
    }
}
