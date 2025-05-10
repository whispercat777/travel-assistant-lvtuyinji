package org.example.tmfinance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmfinance.domain.po.Budget;

import java.util.List;

public interface BudgetService extends IService<Budget> {
    Integer addBudget(Budget budget);
    Integer modifyBudget(Budget budget);
    List<Budget> getEventBudget(Integer eveID);
    Boolean deleteBudgetByEveID(Integer eveID);

}
