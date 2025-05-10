package org.example.tmplan.domain.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    Integer id;
    Integer eveID;
    Integer type;
    LocalDateTime time;
    Float money;
    String name;
}
