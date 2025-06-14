package org.example.tmplan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.client.FinanceClient;
import org.example.tmplan.mapper.EventMapper;
import org.example.tmplan.domain.po.Event;
import org.example.tmplan.domain.po.Result;
import org.example.tmplan.domain.vo.Budget;
import org.example.tmplan.domain.vo.EventVo;
import org.example.tmplan.domain.vo.Expense;
import org.example.tmplan.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * EventServiceImpl
 *
 * 事件服务实现类，提供事件的增删改查功能，支持跨服务获取预算与支出信息。
 */
@Slf4j
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private FinanceClient financeClient;

    /**
     * 添加事件
     *
     * @param event 事件实体
     * @return 插入成功则返回生成的事件 ID，失败返回 null
     */
    public Integer addEvent(Event event) {
        boolean isSaved = this.save(event);
        if (isSaved) {
            return event.getID(); // 返回自动生成的主键
        }
        return null;
    }

    /**
     * 根据行程 ID 查询其所有关联事件，并附加预算与支出信息
     *
     * @param itiID 行程 ID
     * @return 事件视图对象列表（含预算与支出）
     */
    public List<EventVo> findByItiID(Integer itiID) {
        log.info("查询行程ID为{}的所有事件", itiID);

        List<Event> events = eventMapper.findByItiID(itiID);
        List<EventVo> eventVos = new ArrayList<>();

        for (Event event : events) {
            EventVo vo = new EventVo();
            BeanUtils.copyProperties(event, vo);

            // 查询预算信息
            Result budgetResult = financeClient.getEventBudget(event.getID());
            List<Budget> budgets = budgetResult.getData() != null
                    ? (List<Budget>) budgetResult.getData()
                    : new ArrayList<>();
            vo.setBudgets(budgets);

            // 查询支出信息
            Result expenseResult = financeClient.getEventExpense(event.getID());
            List<Expense> expenses = expenseResult.getData() != null
                    ? (List<Expense>) expenseResult.getData()
                    : new ArrayList<>();
            vo.setExpenses(expenses);

            eventVos.add(vo);
        }

        return eventVos;
    }

    /**
     * 修改事件信息（仅更新不为 null 的字段）
     *
     * @param event 包含更新字段的事件对象
     * @return 更新成功返回事件 ID，失败返回 null
     */
    public Integer modifyEvent(Event event) {
        LambdaUpdateWrapper<Event> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Event::getID, event.getID())
                .set(event.getItiID() != null, Event::getItiID, event.getItiID())
                .set(event.getStartTime() != null, Event::getStartTime, event.getStartTime())
                .set(event.getEndTime() != null, Event::getEndTime, event.getEndTime())
                .set(event.getLocation() != null, Event::getLocation, event.getLocation())
                .set(event.getDescription() != null, Event::getDescription, event.getDescription())
                .set(event.getName() != null, Event::getName, event.getName())
                .set(event.getType() != null, Event::getType, event.getType());

        int rows = eventMapper.update(null, updateWrapper);
        if (rows > 0) {
            log.info("事件ID为{}的记录更新成功", event.getID());
            return event.getID();
        }

        log.warn("事件ID为{}的记录更新失败", event.getID());
        return null;
    }

    /**
     * 删除指定 ID 的事件及其关联的预算、支出信息
     *
     * @param id 事件 ID
     * @return 是否成功删除
     */
    public boolean deleteEvent(Integer id) {
        // 删除预算和支出
        financeClient.deleteBudgetByEveID(id);
        financeClient.deleteExpenseByEveID(id);

        // 删除事件本身
        return this.removeById(id);
    }

    /**
     * 根据多个行程 ID 查询所有关联事件的 ID
     *
     * @param itiIDs 行程 ID 列表
     * @return 所有事件的 ID 列表
     */
    @Override
    public List<Integer> getEventByItiIDs(List<Integer> itiIDs) {
        if (itiIDs == null || itiIDs.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Event::getItiID, itiIDs);

        List<Event> events = this.list(queryWrapper);
        List<Integer> eventIDs = new ArrayList<>();
        for (Event event : events) {
            eventIDs.add(event.getID());
        }

        return eventIDs;
    }
}
