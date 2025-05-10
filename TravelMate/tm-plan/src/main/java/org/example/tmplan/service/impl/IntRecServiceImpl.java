package org.example.tmplan.service.impl;

import org.example.tmplan.domain.dto.ItineraryDTO;
import org.example.tmplan.service.IntRecService;
import org.example.tmplan.service.ItineraryService;
import org.example.tmplan.service.ZhipuAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IntRecServiceImpl implements IntRecService {

    @Autowired
    private ZhipuAIService zhipuAIService;
    @Autowired
    private ItineraryService itineraryService;
    @Override
    public String getIntRec(Integer id) {
        ItineraryDTO itineraryDetails = itineraryService.getItineraryDetails(id);
        String location = itineraryDetails.getLocation();
        LocalDate startDate = itineraryDetails.getStartDate();
        LocalDate endDate = itineraryDetails.getEndDate();
        try {
            // 构造用户消息
            String userMessage = String.format(
                    "我要去%s旅行，从%s到%s，请根据我的预算、季节、目的地和天数推荐旅行计划，包括每一天的详细行程，不要说多余的话，直接给我计划，不要说废话，要条理一点，有序号。！",
                    location, startDate, endDate
            );

            String response = zhipuAIService.invokeChatCompletion(userMessage);
            return response;


        } catch (Exception e) {
            return "调用 GLM-4-Flash API 出错：" + e.getMessage();
        }
    }


}
