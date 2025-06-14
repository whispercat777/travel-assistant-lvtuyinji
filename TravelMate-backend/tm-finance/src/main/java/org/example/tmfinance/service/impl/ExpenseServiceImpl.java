package org.example.tmfinance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.tmfinance.client.PlanClient;
import org.example.tmfinance.domain.po.TypeReport;
import org.example.tmfinance.mapper.ExpenseMapper;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.domain.po.TimeReport;
import org.example.tmfinance.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 支出服务实现类，提供支出记录的增删改查、
 * 以及按时间和类型维度的分析统计功能。
 */
@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper, Expense> implements ExpenseService {

    @Autowired
    private ExpenseMapper expenseMapper;

    @Autowired
    private PlanClient planClient;

    /**
     * 新增支出记录。
     * @param expense 支出实体
     * @return 返回新插入记录的 ID，若失败返回 null
     */
    @Override
    public Integer addExpense(Expense expense) {
        boolean isInsert = this.save(expense);
        return isInsert ? expense.getId() : null;
    }

    /**
     * 根据事件 ID 统计支出记录数量。
     * @param eveID 事件 ID
     * @return 支出记录条数
     */
    @Override
    public Integer countRecordsByEveID(Integer eveID) {
        return expenseMapper.countByEveID(eveID);
    }

    /**
     * 修改支出记录，仅更新非空字段。
     * @param expense 支出对象，需包含主键 ID
     * @return 返回修改记录的 ID，若失败返回 null
     */
    @Override
    public Integer modifyExpense(Expense expense) {
        LambdaUpdateWrapper<Expense> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Expense::getId, expense.getId())
                .set(expense.getEveID() != null, Expense::getEveID, expense.getEveID())
                .set(expense.getType() != null, Expense::getType, expense.getType())
                .set(expense.getTime() != null, Expense::getTime, expense.getTime())
                .set(expense.getMoney() != null, Expense::getMoney, expense.getMoney())
                .set(expense.getName() != null, Expense::getName, expense.getName());

        int rows = expenseMapper.update(null, updateWrapper);
        return rows > 0 ? expense.getId() : null;
    }

    /**
     * 查询某事件下的所有支出记录。
     * @param eveID 事件 ID
     * @return 支出记录列表
     */
    @Override
    public List<Expense> getEventExpense(Integer eveID) {
        LambdaQueryWrapper<Expense> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Expense::getEveID, eveID);
        return this.list(queryWrapper);
    }

    /**
     * 删除指定事件 ID 下的所有支出记录。
     * @param eveID 事件 ID
     * @return 固定返回 true
     */
    @Override
    public Boolean deleteExpenseByEveID(Integer eveID) {
        LambdaQueryWrapper<Expense> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(Expense::getEveID, eveID);
        expenseMapper.delete(deleteWrapper);
        return true;
    }

    /**
     * 获取某用户在时间范围内的支出统计报告。
     * @param startTime 起始时间（yyyy-MM-dd 格式）
     * @param endTime 结束时间（yyyy-MM-dd 格式）
     * @param userID 用户 ID
     * @return 包含支出记录和总金额的时间报告对象
     */
    @Override
    public TimeReport getTimeExpense(String startTime, String endTime, Integer userID) {
        Result result = planClient.getAllEvent(userID);
        List<Integer> eveIDs = (List<Integer>) result.getData();

        if (eveIDs == null || eveIDs.isEmpty()) {
            return new TimeReport(Collections.emptyList(), 0);
        }

        LambdaQueryWrapper<Expense> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Expense::getTime, LocalDateTime.parse(startTime), LocalDateTime.parse(endTime))
                .in(Expense::getEveID, eveIDs);

        List<Expense> expenses = expenseMapper.selectList(queryWrapper);
        int totalExpense = expenses.stream().mapToInt(e -> Math.round(e.getMoney())).sum();
        return new TimeReport(expenses, totalExpense);
    }

    /**
     * 获取用户所有支出记录及其总金额。
     * @param userID 用户 ID
     * @return 时间报告对象（含记录与金额总和）
     */
    @Override
    public TimeReport getAllExpense(Integer userID) {
        Result result = planClient.getAllEvent(userID);
        List<Integer> eveIDs = (List<Integer>) result.getData();

        if (eveIDs == null || eveIDs.isEmpty()) {
            return new TimeReport(Collections.emptyList(), 0);
        }

        LambdaQueryWrapper<Expense> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Expense::getEveID, eveIDs);

        List<Expense> expenses = expenseMapper.selectList(queryWrapper);
        int totalExpense = expenses.stream().mapToInt(e -> Math.round(e.getMoney())).sum();
        return new TimeReport(expenses, totalExpense);
    }

    /**
     * 获取某用户在指定支出类型下的统计报告。
     * @param types 支出类型列表
     * @param userID 用户 ID
     * @return 类型报告对象（含记录与金额总和）
     */
    @Override
    public TypeReport getTypeExpense(List<Integer> types, Integer userID) {
        Result result = planClient.getAllEvent(userID);
        List<Integer> eveIDs = (List<Integer>) result.getData();

        if (eveIDs == null || eveIDs.isEmpty()) {
            return new TypeReport(Collections.emptyList(), 0);
        }

        LambdaQueryWrapper<Expense> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Expense::getEveID, eveIDs).in(Expense::getType, types);

        List<Expense> expenses = expenseMapper.selectList(queryWrapper);
        int totalExpense = expenses.stream().mapToInt(e -> Math.round(e.getMoney())).sum();
        return new TypeReport(expenses, totalExpense);
    }
}
