package org.example.tmplan.client;

import org.example.tmplan.domain.po.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FinanceClient
 *
 * 用于远程调用 tm-finance 微服务，获取/删除事件对应的预算与支出信息。
 * 使用 Spring Cloud OpenFeign 实现服务间通信。
 */
@FeignClient(name = "tm-finance")
public interface FinanceClient {

    /**
     * 根据事件 ID 获取预算信息
     *
     * @param eveID 事件 ID
     * @return 包含预算列表的统一响应结果
     */
    @GetMapping("/budget/event")
    Result getEventBudget(@RequestParam("eveID") Integer eveID);

    /**
     * 根据事件 ID 获取支出信息
     *
     * @param eveID 事件 ID
     * @return 包含支出列表的统一响应结果
     */
    @GetMapping("/expense/event")
    Result getEventExpense(@RequestParam("eveID") Integer eveID);

    /**
     * 根据事件 ID 删除其对应的所有支出记录
     *
     * @param eveID 事件 ID
     * @return 操作结果
     */
    @DeleteMapping("/expense/deletebyeveid")
    Result deleteExpenseByEveID(@RequestParam("eveID") Integer eveID);

    /**
     * 根据事件 ID 删除其对应的所有预算记录
     *
     * @param eveID 事件 ID
     * @return 操作结果
     */
    @DeleteMapping("/budget/deletebyeveid")
    Result deleteBudgetByEveID(@RequestParam("eveID") Integer eveID);
}
