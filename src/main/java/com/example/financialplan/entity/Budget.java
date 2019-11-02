package com.example.financialplan.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Locale;


@Entity
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

}
