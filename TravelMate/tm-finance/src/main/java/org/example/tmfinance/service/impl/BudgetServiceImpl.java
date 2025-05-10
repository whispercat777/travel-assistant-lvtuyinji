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

@Service
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements BudgetService {
    @Autowired
    private BudgetMapper budgetMapper;
    public Integer addBudget(Budget budget) {
        boolean isInsert =this.save(budget);
        if (!isInsert) {
            return null;
        }
        return budget.getId();
    }
    public Integer modifyBudget(Budget budget) {
        // 创建 LambdaUpdateWrapper 来构建动态的更新条件
        LambdaUpdateWrapper<Budget> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Budget::getId, budget.getId()) // 根据 ID 匹配
                .set(budget.getEveID() != null, Budget::getEveID, budget.getEveID()) // 如果 eveID 不为空，更新 eveID
                .set(budget.getMoney() != null, Budget::getMoney, budget.getMoney()); // 如果 money 不为空，更新 money

        // 执行更新操作
        int rows = budgetMapper.update(null, updateWrapper);
        if (rows > 0) {
            return budget.getId(); // 如果更新成功，返回记录 ID
        }
        return null; // 如果更新失败，返回 null
    }

    public List<Budget> getEventBudget(Integer eveID) {
        // 创建 LambdaQueryWrapper 来构建查询条件
        LambdaQueryWrapper<Budget> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Budget::getEveID, eveID); // 根据 eveID 查询

        // 查询记录并返回结果
        return this.list(queryWrapper);
    }
    public Boolean deleteBudgetByEveID(Integer eveID) {
        // 创建 LambdaQueryWrapper 构建删除条件
        LambdaQueryWrapper<Budget> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(Budget::getEveID, eveID); // 根据 eveID 构建删除条件

        // 执行删除操作
        budgetMapper.delete(deleteWrapper);

        // 返回删除是否成功
        return true;
    }
}
