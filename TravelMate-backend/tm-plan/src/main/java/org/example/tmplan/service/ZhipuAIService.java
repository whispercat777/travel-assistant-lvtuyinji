package org.example.tmplan.service;

/**
 * 智谱 AI 服务接口。
 * 用于调用大模型（如 GLM-4）生成聊天或推荐内容。
 */
public interface ZhipuAIService {

    /**
     * 向智谱大模型发送用户消息，获取 AI 生成的回复内容。
     *
     * @param userMessage 用户输入的自然语言请求（如行程推荐）
     * @return 模型生成的回复文本内容
     */
    String invokeChatCompletion(String userMessage);
}
