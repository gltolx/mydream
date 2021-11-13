package com.lin.mydream.service.helper;

import com.lin.mydream.config.RobotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/4.
 * @desc 钉钉机器人Helper
 */
@Slf4j
@Component
public class DingHelper {
    @Autowired
    private RobotProperties robotProperties;
    @Autowired
    private RestTemplate restTemplate;


}
