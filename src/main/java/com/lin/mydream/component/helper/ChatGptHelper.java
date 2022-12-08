package com.lin.mydream.component.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lin.mydream.service.dto.chatgpt.CReplyDTO;
import com.lin.mydream.util.CommonUtil;
import com.lin.mydream.util.LogUtil;
import com.lin.mydream.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.TreeMap;

import static com.lin.mydream.component.helper.TencentChatBotHelper.CT_JSON;

/**
 * ChatGPT 请求帮助类
 * <a href="https://beta.openai.com/docs/introduction/overview">ChatGPT 开发文档</a>
 */
@Slf4j
@Component
public class ChatGptHelper {

    public static final String API_1 = "https://api.openai.com/v1/completions";

    @Value("${chat-gpt.api-key}")
    private String apiKey;
    @Value("${chat-gpt.max-tokens}")
    private Integer maxTokens;
    @Value("${chat-gpt.temperature}")
    private Double temperature;

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
        headers.put("Authorization", "Bearer " + apiKey);
        headers.put("Content-Type", CT_JSON);
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
            return CommonUtil.or(() -> CReplyDTO.success(choice.getString("text")), CReplyDTO.fail());

        } catch (Exception e) {
            LogUtil.error("input:{}, output:{}", input, result, e);
            return CReplyDTO.fail();
        }

    }

}
