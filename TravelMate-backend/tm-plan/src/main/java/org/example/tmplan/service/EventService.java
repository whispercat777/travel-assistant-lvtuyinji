package org.example.tmplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmplan.domain.po.Event;
import org.example.tmplan.domain.vo.EventVo;

import java.util.List;

/**
 * 事件服务接口。
 * 提供事件的增删改查功能，并支持按行程 ID 查询、批量查询等业务逻辑。
 * 继承 MyBatis-Plus 的 IService 接口，具备基础 CRUD 能力。
 */
public interface EventService extends IService<Event> {

    /**
     * 根据行程 ID 查询该行程下的所有事件信息（带预算/支出）。
     *
     * @param itiID 行程 ID
     * @return 事件视图对象列表
     */
    List<EventVo> findByItiID(Integer itiID);

    /**
     * 添加新的事件记录。
     *
     * @param event 事件实体
     * @return 新增记录的主键 ID，失败返回 null
     */
    Integer addEvent(Event event);

    /**
     * 修改已有事件记录，仅更新非空字段。
     *
     * @param event 修改内容
     * @return 修改成功返回事件 ID，失败返回 null
     */
    Integer modifyEvent(Event event);

    /**
     * 删除指定事件。
     *
     * @param id 事件 ID
     * @return 删除是否成功
     */
    boolean deleteEvent(Integer id);

    /**
     * 根据多个行程 ID 查询所有相关事件的 ID。
     *
     * @param itiIDs 行程 ID 列表
     * @return 事件 ID 列表
     */
    List<Integer> getEventByItiIDs(List<Integer> itiIDs);
}
