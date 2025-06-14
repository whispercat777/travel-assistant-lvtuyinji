package org.example.tmplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.dto.ItineraryDTO;
import org.example.tmplan.domain.vo.ItineraryVo;

import java.util.List;

/**
 * 行程服务接口。
 * 提供行程的创建、查询、修改、删除及相关事件的聚合查询功能。
 * 继承 MyBatis-Plus 的 IService 接口，具备基础 CRUD 能力。
 */
public interface ItineraryService extends IService<Itinerary> {

    /**
     * 添加新行程。
     *
     * @param itinerary 行程实体对象
     * @return 新增行程的主键 ID，失败返回 null
     */
    Integer addItinerary(Itinerary itinerary);

    /**
     * 根据用户 ID 获取其所有行程列表。
     *
     * @param userID 用户主键 ID
     * @return 行程列表
     */
    List<Itinerary> getItinerariesByUserID(Integer userID);

    /**
     * 根据行程 ID 获取完整行程信息（含事件列表）。
     *
     * @param ID 行程主键 ID
     * @return 行程视图对象，包含事件信息
     */
    ItineraryVo getItineraryByID(Integer ID);

    /**
     * 修改行程信息，仅更新非空字段。
     *
     * @param itinerary 行程修改内容
     * @return 修改成功返回行程 ID，失败返回 null
     */
    Integer modifyItinerary(Itinerary itinerary);

    /**
     * 删除指定行程及其所有关联事件。
     *
     * @param id 行程主键 ID
     * @return 删除是否成功
     */
    boolean deleteItinerary(Integer id);

    /**
     * 获取指定行程的简要信息（用于推荐或展示）。
     *
     * @param id 行程主键 ID
     * @return 行程 DTO（起止时间 + 地点）
     */
    ItineraryDTO getItineraryDetails(Integer id);

    /**
     * 获取用户所有事件 ID（聚合自其所有行程）。
     *
     * @param userID 用户主键 ID
     * @return 所有关联事件的 ID 列表
     */
    List<Integer> getEventByUserID(Integer userID);
}
