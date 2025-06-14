package org.example.tmplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.domain.po.Event;
import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.po.Result;
import org.example.tmplan.domain.vo.ItineraryVo;
import org.example.tmplan.service.EventService;
import org.example.tmplan.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ItineraryController
 * <p>
 * 行程与事件管理接口控制器，负责处理前端请求并调用服务层进行业务处理。
 * 包含行程的增删改查及事件的管理功能。
 */
@Slf4j
@RestController
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private EventService eventService;

    /**
     * 添加一个新的行程
     *
     * @param itinerary 行程对象（通过 JSON 请求体传入）
     * @return 添加成功的行程 ID
     */
    @PostMapping("/itinerary/add")
    public Result addItinerary(@RequestBody Itinerary itinerary) {
        Integer id = itineraryService.addItinerary(itinerary);
        return Result.success(id);
    }

    /**
     * 根据用户 ID 获取其所有行程
     *
     * @param userID 用户 ID
     * @return 当前用户的所有行程列表
     */
    @GetMapping("/itinerary/getall")
    public Result getAllItinerary(Integer userID) {
        List<Itinerary> itineraries = itineraryService.getItinerariesByUserID(userID);
        return Result.success(itineraries);
    }

    /**
     * 获取单个行程的详细信息（包含其关联事件）
     *
     * @param ID 行程 ID
     * @return 行程视图对象（含事件列表）
     */
    @GetMapping("/itinerary/getone")
    public Result getOneItinerary(Integer ID) {
        ItineraryVo vo = itineraryService.getItineraryByID(ID);
        return Result.success(vo);
    }

    /**
     * 添加一个新的事件
     *
     * @param event 事件对象（通过 JSON 请求体传入）
     * @return 添加成功的事件 ID
     */
    @PostMapping("/event/add")
    public Result addEvent(@RequestBody Event event) {
        log.info("add event");
        Integer id = eventService.addEvent(event);
        log.info("id={}", id);
        return Result.success(id);
    }

    /**
     * 修改一个事件的信息
     *
     * @param event 事件对象（包含要更新的字段）
     * @return 修改后的事件 ID
     */
    @PutMapping("/event/modify")
    public Result modifyEvent(@RequestBody Event event) {
        Integer id = eventService.modifyEvent(event);
        return Result.success(id);
    }

    /**
     * 根据事件 ID 删除事件
     *
     * @param id 事件 ID
     * @return 是否成功删除
     */
    @DeleteMapping("/event/delete")
    public Result deleteEvent(Integer id) {
        boolean result = eventService.deleteEvent(id);
        return Result.success(result);
    }

    /**
     * 修改行程信息
     *
     * @param itinerary 行程对象（包含要更新的字段）
     * @return 修改后的行程 ID
     */
    @PutMapping("/itinerary/modify")
    public Result modifyItinerary(@RequestBody Itinerary itinerary) {
        Integer id = itineraryService.modifyItinerary(itinerary);
        return Result.success(id);
    }

    /**
     * 根据行程 ID 删除行程及其关联事件
     *
     * @param id 行程 ID
     * @return 是否成功删除
     */
    @DeleteMapping("/itinerary/delete")
    public Result deleteItinerary(Integer id) {
        boolean result = itineraryService.deleteItinerary(id);
        return Result.success(result);
    }

    /**
     * 根据用户 ID 获取其所有事件 ID（跨多个行程）
     *
     * @param userID 用户 ID
     * @return 事件 ID 列表
     */
    @GetMapping("/event/getall")
    public Result getAllEvent(Integer userID) {
        List<Integer> ids = itineraryService.getEventByUserID(userID);
        return Result.success(ids);
    }

    /**
     * 根据多个行程 ID 查询对应的所有事件 ID
     *
     * @param itiIDs 行程 ID 列表
     * @return 所有对应事件的 ID 列表
     */
    @GetMapping("/event/getbyitiIDs")
    public Result getItiIDsEvent(@RequestParam List<Integer> itiIDs) {
        List<Integer> ids = eventService.getEventByItiIDs(itiIDs);
        return Result.success(ids);
    }
}
