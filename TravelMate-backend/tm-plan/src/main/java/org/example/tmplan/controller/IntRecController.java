package org.example.tmplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.domain.po.Result;
import org.example.tmplan.service.IntRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行程推荐控制器。
 * 提供根据行程 ID 获取推荐内容的接口。
 */
@Slf4j
@RestController
public class IntRecController {

    @Autowired
    private IntRecService intRecService;

    /**
     * 获取某个行程的推荐结果。
     *
     * @param id 行程 ID
     * @return 推荐内容（字符串封装在统一的 Result 返回体中）
     */
    @GetMapping("/itinerary/getintrec")
    public Result getIntRec(Integer id) {
        String answer = intRecService.getIntRec(id);
        return Result.success(answer);
    }
}
