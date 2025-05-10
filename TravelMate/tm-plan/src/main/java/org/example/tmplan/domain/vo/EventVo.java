package org.example.tmplan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventVo {
    private Integer ID;
    private Integer itiID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String description;
    private String name;
    private Integer type;
    private List<Expense> expenses;
    private List<Budget> budgets;
}
