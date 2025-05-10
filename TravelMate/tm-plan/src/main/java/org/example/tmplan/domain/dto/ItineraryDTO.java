package org.example.tmplan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
}
