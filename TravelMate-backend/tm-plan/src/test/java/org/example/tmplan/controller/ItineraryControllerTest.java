package org.example.tmplan.controller;

import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.po.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItineraryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAddItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setUserID(681295877);
        itinerary.setName("旅行测试");
        itinerary.setStartDate(LocalDate.of(2025, 6, 1));
        itinerary.setEndDate(LocalDate.of(2025, 6, 10));
        itinerary.setLocation("上海");

        ResponseEntity<Result> response = restTemplate.postForEntity("/itinerary/add", itinerary, Result.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Result result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    void testGetAllItinerary() {
        ResponseEntity<Result> response = restTemplate.getForEntity("/itinerary/getall?userID=681295877", Result.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Result result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isNotNull(); // data应该是List<Itinerary>

    }

    @Test
    void testGetOneItinerary() throws InterruptedException{
        ResponseEntity<Result> response = restTemplate.getForEntity("/itinerary/getone?ID=9", Result.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Result result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isNotNull(); // data应该是ItineraryVo

    }

    @Test
    void testModifyItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setID(9); // 测试时确保ID=1存在
        itinerary.setUserID(681295882);
        itinerary.setName("修改后的行程名称");
        itinerary.setStartDate(LocalDate.of(2025, 7, 1));
        itinerary.setEndDate(LocalDate.of(2025, 7, 5));
        itinerary.setLocation("北京");

        HttpEntity<Itinerary> requestEntity = new HttpEntity<>(itinerary);
        ResponseEntity<Result> response = restTemplate.exchange("/itinerary/modify", HttpMethod.PUT, requestEntity, Result.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Result result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(1);
    }

    @Test
    void testDeleteItinerary() {
        ResponseEntity<Result> response = restTemplate.exchange("/itinerary/delete?id=9", HttpMethod.DELETE, null, Result.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Result result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(1);
    }
}
