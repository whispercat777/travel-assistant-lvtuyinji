package org.example.tmplan.service.impl;

import org.example.tmplan.domain.dto.ItineraryDTO;
import org.example.tmplan.service.IntRecService;
import org.example.tmplan.service.ItineraryService;
import org.example.tmplan.service.ZhipuAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 行程推荐服务实现类。
 * 基于用户选择的行程信息调用智谱大模型生成个性化推荐内容。
 */
@Service
public class IntRecServiceImpl implements IntRecService {

    @Autowired
    private ZhipuAIService zhipuAIService;

    @Autowired
    private ItineraryService itineraryService;

    /**
     * 获取指定行程 ID 的推荐文本。
     * 推荐结果基于智谱 AI 生成的旅行计划。
     *
     * @param id 行程 ID
     * @return 推荐文本内容（若失败返回错误信息）
     */
    @Override
    public String getIntRec(Integer id) {
        // 获取行程基本信息（地点 + 起止时间）
        ItineraryDTO itineraryDetails = itineraryService.getItineraryDetails(id);
        String location = itineraryDetails.getLocation();
        LocalDate startDate = itineraryDetails.getStartDate();
        LocalDate endDate = itineraryDetails.getEndDate();

        try {
            // 构造用户输入提示（prompt）
            String userMessage = String.format(
                    "我要去%s旅行，从%s到%s，请根据我的预算、季节、目的地和天数推荐旅行计划，包括每一天的详细行程，不要说多余的话，直接给我计划，不要说废话，要条理一点，有序号！",
                    location, startDate, endDate
            );

            // 调用智谱AI模型生成回复
            String response = zhipuAIService.invokeChatCompletion(userMessage);
            return response;

        } catch (Exception e) {
            // 出错时返回错误提示
            return "调用 GLM-4-Flash API 出错：" + e.getMessage();
        }
    }
}
