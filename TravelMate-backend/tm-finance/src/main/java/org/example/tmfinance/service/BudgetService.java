package org.example.tmfinance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmfinance.domain.po.Budget;

import java.util.List;

/**
 * 预算服务接口。
 * 提供预算的新增、修改、事件级查询与事件级删除功能。
 * 继承 MyBatis-Plus 的 IService 接口，具备基础 CRUD 能力。
 */
public interface BudgetService extends IService<Budget> {

    /**
     * 添加一条预算记录。
     *
     * @param budget 预算对象
     * @return 返回新插入记录的主键 ID，若失败返回 null
     */
    Integer addBudget(Budget budget);

    /**
     * 修改一条预算记录，仅更新非空字段。
     *
     * @param budget 包含 ID 与待更新字段的预算对象
     * @return 更新成功返回记录 ID，失败返回 null
     */
    Integer modifyBudget(Budget budget);

    /**
     * 查询某事件 ID 下的所有预算记录。
     *
     * @param eveID 事件 ID
     * @return 预算记录列表
     */
    List<Budget> getEventBudget(Integer eveID);

    /**
     * 删除某事件下的所有预算记录。
     *
     * @param eveID 事件 ID
     * @return 删除是否成功（固定返回 true）
     */
    Boolean deleteBudgetByEveID(Integer eveID);
}
