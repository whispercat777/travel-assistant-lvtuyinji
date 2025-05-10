package org.example.tmplan.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import okhttp3.*;
import org.example.tmplan.pojo.dto.ItineraryDTO;
import org.example.tmplan.service.IntRecService;
import org.example.tmplan.service.ItineraryService;
import org.example.tmplan.service.ZhipuAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
