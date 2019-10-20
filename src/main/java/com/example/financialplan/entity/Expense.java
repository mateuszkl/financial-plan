package com.example.financialplan.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Expense {

    public Expense() {
        this.timestamp = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String name;

    private BigDecimal amount;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

}
