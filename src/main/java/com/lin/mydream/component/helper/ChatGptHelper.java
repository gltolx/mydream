package com.lin.mydream.component.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.service.dto.chatgpt.CReplyDTO;
import com.lin.mydream.util.CommonUtil;
import com.lin.mydream.util.LogUtil;
import com.lin.mydream.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.TreeMap;


/**
 * ChatGPT 请求帮助类
 * <a href="https://beta.openai.com/docs/introduction/overview">ChatGPT 开发文档</a>
 */
@Slf4j
@Service
public class ChatGptHelper {

    public static final String API_1 = "https://api.openai.com/v1/completions";

    @Value("${chat-gpt.api-key}")
    private String apiKey;
    @Value("#{T(java.lang.Integer).parseInt('${chat-gpt.max-tokens:1000}')}")
    private Integer maxTokens;
    @Value("#{T(java.lang.Double).parseDouble('${chat-gpt.temperature:0.5}')}")
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
        requestBody.put("max_tokens", 1000);
        requestBody.put("temperature", 0.5);
        requestBody.put("top_p", 1);
        requestBody.put("n", 1);
        requestBody.put("stream", false);
        String body = requestBody.toJSONString();
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
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

    public static void main(String[] args) {
        ChatGptHelper chatGptHelper = new ChatGptHelper();
        chatGptHelper.apiKey = "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";
        chatGptHelper.maxTokens = 1000;
        chatGptHelper.temperature = 0.5D;
        CReplyDTO content = chatGptHelper.davinci("Say this is a test");
        System.out.println(content);
    }


}
