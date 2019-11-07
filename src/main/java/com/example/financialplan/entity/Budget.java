package com.example.financialplan.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@Entity
public class Budget extends BaseEntity {

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
