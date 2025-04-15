package org.example.tmplan.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryVo {
    private Integer ID;
    private Integer userID;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private List<EventVo> events;
}
