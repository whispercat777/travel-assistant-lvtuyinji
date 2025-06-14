package org.example.tmfinance.client;

import org.example.tmfinance.domain.po.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程调用计划服务（tm-plan）的 Feign 客户端接口。
 * 提供对事件（event）信息的跨服务获取功能。
 */
@FeignClient(name = "tm-plan")
public interface PlanClient {

    /**
     * 根据用户 ID 获取其所有关联的事件 ID 列表。
     *
     * @param userID 用户 ID
     * @return Result 对象，data 为事件 ID 列表（List<Integer>）
     */
    @GetMapping("/event/getall")
    Result getAllEvent(@RequestParam("userID") Integer userID);

    /**
     * 根据多个行程 ID 获取对应的所有事件 ID 列表。
     *
     * @param itiIDs 行程 ID 列表
     * @return Result 对象，data 为事件 ID 列表（List<Integer>）
     */
    @GetMapping("/event/getbyitiIDs")
    Result getItiIDsEvent(@RequestParam("itiIDs") List<Integer> itiIDs);
}
