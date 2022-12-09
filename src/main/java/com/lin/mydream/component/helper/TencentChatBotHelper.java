package com.lin.mydream.component.helper;

import com.alibaba.fastjson.JSONObject;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.service.dto.tencent.ReplyDTO;
import com.lin.mydream.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/12/24.
 * @desc 腾讯闲聊接口helper
 */
@Slf4j
@Component
public class TencentChatBotHelper {

    private final static Charset UTF8 = StandardCharsets.UTF_8;
//    private final static String SECRET_ID = "AKIDnQmsiHZaMr52jALFsFK8gz1mFSO3SaR2";
//    private final static String SECRET_KEY = "MRGo55pIUP3RYOxGaeRxCAqFOGrBF1Wl";
    private final static String SIGN_ALGORITHM = "TC3-HMAC-SHA256";

    @Value("${tencent.secret-id}")
    private String secretId;
    @Value("${tencent.secret-key}")
    private String secretKey;


    /**
     * 调用腾讯闲聊接口
     *
     * @param msg 入参消息
     * @return 返回闲聊消息
     * @throws Exception
     */
    public ReplyDTO chat(String msg) throws Exception {
        String payload = "{\"Query\": \"" + msg + "\"}";

        String host = "nlp.tencentcloudapi.com";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String authorization = this.makeSign("nlp", host, payload, timestamp);

        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Authorization", authorization);
        headers.put("Content-Type", Mydreams.CT_JSON);
        headers.put("Host", host);
        headers.put("X-TC-Action", "ChatBot");
        headers.put("X-TC-Timestamp", timestamp);
        headers.put("X-TC-Version", "2019-04-08");
        headers.put("X-TC-Region", "ap-guangzhou");

        String result = OkHttpUtil.post("https://" + host, payload, headers);
        if (log.isInfoEnabled()) {
            log.info("input:{}, output:{}", msg, result);
        }
        JSONObject respJson = JSONObject.parseObject(result).getJSONObject("Response");

        String reply = respJson.getString("Reply");
        Float confidence = respJson.getFloat("Confidence");
        return new ReplyDTO(reply, confidence).desensitization(); // 脱敏
    }

    public static void main(String[] args) throws Exception {
        String re = "{\"Response\":{\"Reply\":\"好的，那晚安啦\",\"Confidence\":1,\"RequestId\":\"067c0dbc-9132-4f52-93e7-8b38a8186187\"}}";
//        ObjectMapper mapper = new ObjectMapper();
//        TcResponse tcResponse = mapper.readValue(re, TcResponse.class);
//        String reply = String.valueOf(tcResponse.fetchReply());
//        Float confidence = (Float) tcResponse.fetchConfidence();
        JSONObject jsonObject = JSONObject.parseObject(re);
        JSONObject responseJson = jsonObject.getJSONObject("Response");
        String reply = responseJson.getString("Reply");
        String confidence = responseJson.getString("Confidence");
        System.out.println();
    }


    /**
     * 计算签名
     *
     * @param service   服务名
     * @param host      调用域名
     * @param payload   请求body
     * @param timestamp 时间戳
     * @return 签名
     * @throws Exception
     */
    private String makeSign(String service, String host, String payload, String timestamp) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(Mydreams.Y_M_D);
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.parseLong(timestamp + "000")));

        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json; charset=utf-8\n" + "host:" + host + "\n";
        String signedHeaders = "content-type;host";

        // payload = request body
        String hashedRequestPayload = sha256Hex(payload);
        String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

        // ************* 步骤 2：拼接待签名字符串 *************
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String hashedCanonicalRequest = sha256Hex(canonicalRequest);
        String stringToSign = SIGN_ALGORITHM + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

        // ************* 步骤 3：计算签名 *************
        byte[] secretDate = hmac256(("TC3" + secretKey).getBytes(UTF8), date);
        byte[] secretService = hmac256(secretDate, service);
        byte[] secretSigning = hmac256(secretService, "tc3_request");
        String signature = DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();

        // ************* 步骤 4：拼接 Authorization *************
        String authorization = SIGN_ALGORITHM + " " + "Credential=" + secretId + "/" + credentialScope + ", "
                + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

        if (log.isDebugEnabled()) {
            log.debug("TencentSignHelper - makeSign, authorization:{}", authorization);
        }
        return authorization;
    }

    public static byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(UTF8));
    }

    public static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(UTF8));
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }
}
