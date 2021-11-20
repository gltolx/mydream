package com.lin.mydream.controller;

import com.lin.mydream.config.RobotProperties;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.model.Robot;
import com.lin.mydream.service.helper.DingHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private DingHelper dingHelper;
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

    @RequestMapping(value = "out-channel", method = RequestMethod.POST)
    public String outChannel(@RequestBody Map<String, Object> map) {
        map.forEach((k, v) -> log.info("key:{}, value:{}", k, v));
        log.info("outChannel ok~~");

        return "OK~~~";
    }
}
