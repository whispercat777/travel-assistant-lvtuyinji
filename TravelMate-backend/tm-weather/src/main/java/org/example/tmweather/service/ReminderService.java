package org.example.tmweather.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmweather.domain.po.Reminder;

import java.util.List;

/**
 * 提醒服务接口。
 * 提供对提醒信息（Reminder）表的业务操作方法定义，包括新增、修改和查询。
 * 继承 MyBatis-Plus 的 IService，具备通用 CRUD 方法。
 */
public interface ReminderService extends IService<Reminder> {

    /**
     * 添加新的提醒记录。
     *
     * @param reminder 提醒对象，包含时间、地点、用户等信息
     * @return 成功插入后返回提醒 ID，失败返回 null
     */
    Integer addReminder(Reminder reminder);

    /**
     * 修改已有的提醒记录（仅更新非空字段）。
     *
     * @param reminder 要更新的提醒对象
     * @return 成功更新后返回提醒 ID，失败返回 null
     */
    Integer modifyReminder(Reminder reminder);

    /**
     * 根据用户 ID 查询该用户的所有提醒记录。
     *
     * @param userID 用户主键 ID
     * @return 提醒记录列表
     */
    List<Reminder> getReminderByUserID(Integer userID);
}
