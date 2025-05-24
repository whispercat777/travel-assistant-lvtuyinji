package org.example.tmplan.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.example.tmplan.service.ZhipuAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 智谱 AI 接口服务实现类。
 * 负责通过调用 GLM-4-Flash 模型获取旅行推荐等智能回复。
 */
@Service
public class ZhipuAIServiceImpl implements ZhipuAIService {

    private final ClientV4 client;
    private final ObjectMapper objectMapper;

    /**
     * 构造函数，初始化智谱 API 客户端和 JSON 解析器。
     *
     * @param apiKey 智谱开放平台 API Key（通过配置文件注入）
     */
    public ZhipuAIServiceImpl(@Value("${zhipuai.api.key}") String apiKey) {
        this.client = new ClientV4.Builder(apiKey).build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 向智谱大模型发送用户消息，获取 AI 返回的推荐文本。
     *
     * @param userMessage 用户输入的行程请求内容
     * @return 模型回复内容（推荐计划），若失败返回错误信息
     */
    @Override
    public String invokeChatCompletion(String userMessage) {
        // 构造对话消息
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", userMessage));

        // 构建模型请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("glm-4-flash")  // 指定模型名称
                .stream(Boolean.FALSE)  // 非流式返回
                .invokeMethod(Constants.invokeMethod)  // 默认调用方式
                .messages(messages)  // 消息列表
                .requestId("request-id-" + System.currentTimeMillis())  // 请求 ID
                .build();

        // 调用模型接口
        ModelApiResponse response = client.invokeModelApi(chatCompletionRequest);

        try {
            // 将 response 的 data 字段转为 JSON 再解析为 Map
            String jsonString = objectMapper.writeValueAsString(response.getData());
            Map<String, Object> dataMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});

            // 提取 content 字段作为 AI 回复内容
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
