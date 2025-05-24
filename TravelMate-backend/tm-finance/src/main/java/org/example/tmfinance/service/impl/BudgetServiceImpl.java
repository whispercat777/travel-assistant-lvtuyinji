package org.example.tmfinance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.tmfinance.mapper.BudgetMapper;
import org.example.tmfinance.domain.po.Budget;
import org.example.tmfinance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预算服务实现类。
 * 实现了预算的新增、修改、查询与按事件 ID 删除的功能。
 * 继承 MyBatis-Plus 的 ServiceImpl，简化基础增删改查操作。
 */
@Service
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements BudgetService {

    @Autowired
    private BudgetMapper budgetMapper;

    /**
     * 添加一条预算记录。
     * @param budget 预算对象
     * @return 返回新增记录的主键 ID，若失败返回 null
     */
    @Override
    public Integer addBudget(Budget budget) {
        boolean isInsert = this.save(budget);
        if (!isInsert) {
            return null;
        }
        return budget.getId();
    }

    /**
     * 修改预算信息，仅更新非空字段。
     * @param budget 包含要更新的字段和主键 ID 的预算对象
     * @return 更新成功则返回记录 ID，失败返回 null
     */
    @Override
    public Integer modifyBudget(Budget budget) {
        LambdaUpdateWrapper<Budget> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Budget::getId, budget.getId())
                .set(budget.getEveID() != null, Budget::getEveID, budget.getEveID())
                .set(budget.getMoney() != null, Budget::getMoney, budget.getMoney());

        int rows = budgetMapper.update(null, updateWrapper);
        return rows > 0 ? budget.getId() : null;
    }

    /**
     * 查询指定事件 ID 下的所有预算记录。
     * @param eveID 事件 ID
     * @return 预算记录列表
     */
    @Override
    public List<Budget> getEventBudget(Integer eveID) {
        LambdaQueryWrapper<Budget> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Budget::getEveID, eveID);
        return this.list(queryWrapper);
    }

    /**
     * 删除指定事件 ID 下的所有预算记录。
     * @param eveID 事件 ID
     * @return 操作是否成功，固定返回 true
     */
    @Override
    public Boolean deleteBudgetByEveID(Integer eveID) {
        LambdaQueryWrapper<Budget> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(Budget::getEveID, eveID);
        budgetMapper.delete(deleteWrapper);
        return true;
    }
}
