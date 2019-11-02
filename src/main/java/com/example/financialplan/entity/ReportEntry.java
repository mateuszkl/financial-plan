package com.example.financialplan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntry {

    private BigDecimal amountPlanned;

    private BigDecimal amountSpent;

    private BigDecimal amountLeft;

    private ExpenseCategory category;

    private Integer month;

    private Integer year;

    private boolean summaryRow;
}
