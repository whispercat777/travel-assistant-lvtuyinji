package org.example.tmplan.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.example.tmplan.service.ZhipuAIService;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ZhipuAIServiceImpl implements ZhipuAIService {
    private final ClientV4 client;
    private final ObjectMapper objectMapper;
    public ZhipuAIServiceImpl(@Value("${zhipuai.api.key}") String apiKey) {
        this.client = new ClientV4.Builder(apiKey).build();
        this.objectMapper = new ObjectMapper();
    }
    //构建信息
    public String invokeChatCompletion(String userMessage) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage("user", userMessage);
        messages.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                //表明使用的模型
                .model("glm-4-flash")
                //表示不使用流式响应
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId("request-id-" + System.currentTimeMillis())
                .build();
        ModelApiResponse response = client.invokeModelApi(chatCompletionRequest);
        try {
            // 将response的数据转换为Map
            String jsonString = objectMapper.writeValueAsString(response.getData());
            Map<String, Object> dataMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            // 提取内容
            if (dataMap.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) dataMap.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    if (message != null && message.containsKey("content")) {
                        return (String) message.get("content");
                    }
                }
            }
            return "找不到内容";
        } catch (Exception e) {
            return "响应错误";
        }
    }
}