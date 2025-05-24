package org.example.tmplan.service.impl;

import org.example.tmplan.service.IntRecService;
import org.example.tmplan.service.ItineraryService;
import org.example.tmplan.service.ZhipuAIService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class IntRecServiceImplTest {

    @Mock
    private ItineraryService itineraryService;

    @Mock
    private ZhipuAIService zhipuAIService;

    @InjectMocks
    private IntRecServiceImpl intRecService;

    public IntRecServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIntRec() {
        // 模拟行程数据
        when(itineraryService.getItineraryDetails(1))
                .thenReturn(new org.example.tmplan.domain.dto.ItineraryDTO(
                        java.time.LocalDate.of(2025, 6, 1),
                        java.time.LocalDate.of(2025, 6, 10),
                        "上海"
                ));

        // 模拟ZhipuAI返回
        when(zhipuAIService.invokeChatCompletion(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn("这里是推荐的旅行计划");

        String result = intRecService.getIntRec(1);

        assertThat(result).isEqualTo("这里是推荐的旅行计划");
    }
}
