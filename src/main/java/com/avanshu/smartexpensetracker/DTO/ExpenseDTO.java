package com.avanshu.smartexpensetracker.DTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseDTO {
    private Long id;
    private String title;
    private Double amount;
    private String category;
    private LocalDate date;


}


