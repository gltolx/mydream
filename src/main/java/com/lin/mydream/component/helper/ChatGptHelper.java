package com.lin.mydream.component.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.dto.chatgpt.CReplyDTO;
import com.lin.mydream.util.CommonUtil;
import com.lin.mydream.util.LogUtil;
import com.lin.mydream.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ChatGPT 请求帮助类
 * <a href="https://beta.openai.com/docs/introduction/overview">ChatGPT 开发文档</a>
 */
@Slf4j
@Service
public class ChatGptHelper implements InitializingBean {

    public static final String API_1 = "https://api.openai.com/v1/completions";
    private static final Pattern TEXT_PATTERN = Pattern.compile("\"text\": \"(.*?)\"");
    private static final int GROUP_INDEX = 1;

    private static final String[] PAUSES;
    static {
        String[] p = {"。", ";", "；", ":", "：", "?", "？", "!", "！", "\n"};

        PAUSES = ArrayUtils.toStringArray(
                Arrays.stream(p)
                        .map(CommonUtil::stringToUnicode)
                        .toArray()
        );
    }
    private String sysApiKey;

    /**
     * apiKey别名，在系统环境变量中查看
     * 1、vim .bash_profile
     * 2、export OPENAI_API_KEY_1='sk-************'
     * 3、:wq
     * 4、source .bash_profile
     */
    @Value("${chat-gpt.api-key-alias}")
    private String apiKeyAlias;
    /**
     * 最大传输token数（数据量长度大小）
     */
    @Value("#{T(java.lang.Integer).parseInt('${chat-gpt.max-tokens:1000}')}")
    private Integer maxTokens;
    /**
     * 默认值为 1，取值 0-2。该值越大每次返回的结果越随机，即相似度越小。
     */
    @Value("#{T(java.lang.Double).parseDouble('${chat-gpt.temperature:0.5}')}")
    private Double temperature;

    @Value("#{T(java.lang.Integer).parseInt('${chat-gpt.segment-length:128}')}")
    private Integer segmentLength;


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            sysApiKey = System.getenv(apiKeyAlias);
        } finally {
            log.info("Load Env Variable [{}], value is:{}", apiKeyAlias, sysApiKey);
        }
    }

    /**
     * davinci模型 ai对话
     * <a href="https://beta.openai.com/docs/api-reference/completions">davinci模型参数文档</a>
     *
     * @param input 输入文本
     * @return 回复内容
     */
    public CReplyDTO davinci(String input) {

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", input);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", temperature);
        requestBody.put("top_p", 1);
        requestBody.put("n", 1);
        requestBody.put("stream", false);
        String body = requestBody.toJSONString();
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Authorization", "Bearer " + sysApiKey);
        headers.put("Content-Type", "application/json");
        String result = null;
        try {
            result = OkHttpUtil.post(API_1, body, headers);
            LogUtil.info("input:{}, output:{}", input, result);
            JSONArray choices = JSONObject.parseObject(result).getJSONArray("choices");
            if (CollectionUtils.isEmpty(choices)) {
                return CReplyDTO.fail();
            }
            JSONObject choice = choices.getJSONObject(0);
            if (choice == null) {
                return CReplyDTO.fail();
            }
            String text = StringUtils.strip(choice.getString("text"), "\n");
            return CommonUtil.or(() -> CReplyDTO.success(text), CReplyDTO.fail());

        } catch (Exception e) {
            LogUtil.error("input:{}, output:{}", input, result, e);
            return CReplyDTO.fail();
        }

    }


    public void davinciStream(String input, Robotx robotx) {

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", input);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", temperature);
        requestBody.put("top_p", 1);
        requestBody.put("n", 1);
        // stream = "true"
        requestBody.put("stream", true);
        String body = requestBody.toJSONString();
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Authorization", "Bearer " + sysApiKey);
        headers.put("Content-Type", "application/json");
        try {
            final StringBuilder[] res = {new StringBuilder()};
            OkHttpUtil.post(API_1, body, headers
                    , line -> {
                        // 解析每个响应的 text 字段并拼接到 StringBuilder 对象中
                        String text = parseText(line);
                        if (text == null) {
                            return;
                        }
                        res[0].append(StringUtils.strip(text, "\n"));

                        // 语意暂停时，大于等于n个字符则输出
                        if (res[0].length() >= segmentLength && isPause(text)) {
                            String segment = CommonUtil.unicodeToString(res[0].toString());
                            robotx.send(segment);
                            res[0] = new StringBuilder();
                        }
                    }
            );
            if (res[0].length() > 0) {
                // 输出剩下的结果
                String last = CommonUtil.unicodeToString(res[0].toString());
                robotx.send(last);
            }

        } catch (Exception e) {
            LogUtil.error("input:{} ", input, e);
        }

    }

    /**
     * 解析line中的text文本
     *
     * @return text文本
     */
    private static String parseText(String line) {
        String text = null;
        Matcher matcher = TEXT_PATTERN.matcher(line);
        if (matcher.find()) {
            text = matcher.group(GROUP_INDEX);
        }
        return text;
    }

    private static boolean isPause(String text) {
        return StringUtils.isNotBlank(text) && StringUtils.containsAny(text, PAUSES);
    }

}
