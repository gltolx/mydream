package com.lin.mydream.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 验证签名工具类
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUtil {

    private static final String hmacSHA256 = "HmacSHA256";

    /**
     * 制作签名
     *
     * @param secret 验签secret
     * @return timestamp & sign
     * @throws Exception
     */
    public static Pair<Long, String> makeSign(String secret) {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance(hmacSHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), hmacSHA256));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData), StandardCharsets.UTF_8));
            log.info("make sign: {}", sign);
            return Pair.of(timestamp, sign);

        } catch (Exception e) {
            log.error("make sign error.", e);
            return null;
        }
    }


}
