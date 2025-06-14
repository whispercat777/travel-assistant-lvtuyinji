package org.example.tmfinance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.domain.po.TimeReport;
import org.example.tmfinance.domain.po.TypeReport;

import java.util.List;

/**
 * 支出服务接口。
 * 提供支出记录的增删改查功能，以及按时间和类型维度的支出分析能力。
 * 继承 MyBatis-Plus 的 IService 接口，自动具备通用 CRUD 能力。
 */
public interface ExpenseService extends IService<Expense> {

    /**
     * 添加一条支出记录。
     *
     * @param expense 支出实体对象
     * @return 返回新插入记录的主键 ID，若失败返回 null
     */
    Integer addExpense(Expense expense);

    /**
     * 查询某事件下支出记录的数量。
     *
     * @param eveID 事件 ID
     * @return 该事件下的支出记录数量
     */
    Integer countRecordsByEveID(Integer eveID);

    /**
     * 修改支出记录，仅更新非空字段。
     *
     * @param expense 包含 ID 与待更新字段的支出对象
     * @return 修改成功返回记录 ID，失败返回 null
     */
    Integer modifyExpense(Expense expense);

    /**
     * 查询某事件下的所有支出记录。
     *
     * @param eveID 事件 ID
     * @return 支出记录列表
     */
    List<Expense> getEventExpense(Integer eveID);

    /**
     * 获取用户在指定时间范围内的支出报告。
     *
     * @param startTime 起始时间（yyyy-MM-dd 格式）
     * @param endTime 结束时间（yyyy-MM-dd 格式）
     * @param userID 用户 ID
     * @return 时间维度的支出统计报告
     */
    TimeReport getTimeExpense(String startTime, String endTime, Integer userID);

    /**
     * 删除某事件下的所有支出记录。
     *
     * @param eveID 事件 ID
     * @return 删除是否成功（固定返回 true）
     */
    Boolean deleteExpenseByEveID(Integer eveID);

    /**
     * 获取用户所有支出记录及总支出金额。
     *
     * @param userID 用户 ID
     * @return 时间维度的支出统计报告
     */
    TimeReport getAllExpense(Integer userID);

    /**
     * 获取用户在指定支出类型下的统计数据。
     *
     * @param types 支出类型 ID 列表
     * @param userID 用户 ID
     * @return 类型维度的支出统计报告
     */
    TypeReport getTypeExpense(List<Integer> types, Integer userID);
}
