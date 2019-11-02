package com.example.financialplan.controller;

import com.example.financialplan.entity.Budget;
import com.example.financialplan.entity.Expense;
import com.example.financialplan.entity.ExpenseCategory;
import com.example.financialplan.entity.ReportEntry;
import com.example.financialplan.repository.BudgetRepository;
import com.example.financialplan.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @GetMapping
    public String show(Model model) {
        model.addAttribute("reportEntries", generateReport());

        return "report";
    }

    private List<ReportEntry> generateReport() {
        List<ReportEntry> reportEntries = new ArrayList<>();
        YearMonth yearMonth = YearMonth.now();

        for (ExpenseCategory category : ExpenseCategory.values()) {
            List<Budget> budgets = budgetRepository.getAllByYearAndMonthAndCategory(yearMonth.getYear(), yearMonth.getMonthValue(), category);

            LocalDate today = LocalDate.now();
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);
            LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

            List<Expense> expenses = expenseRepository.getAllByTimestampAfterAndTimestampBeforeAndCategory(firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atTime(LocalTime.MAX), category);

            BigDecimal budgetSumForCategory = calculateBudgetSum(yearMonth, category, budgets);
            BigDecimal expenseSumForCategory = calculateExpensesSum(category, firstDayOfMonth, lastDayOfMonth, expenses);

            reportEntries.add(buildReportEntry(yearMonth, category, budgetSumForCategory, expenseSumForCategory));
        }

        addSummaryRow(reportEntries, yearMonth);

        return reportEntries;
    }

    private void addSummaryRow(List<ReportEntry> reportEntries, YearMonth yearMonth) {
        if (!reportEntries.isEmpty()) {
            BigDecimal amountLeftSummary = sum(reportEntries, ReportEntry::getAmountLeft);
            BigDecimal amountSpentSummary = sum(reportEntries, ReportEntry::getAmountSpent);
            BigDecimal amountPlannedSummary = sum(reportEntries, ReportEntry::getAmountPlanned);

            reportEntries.add(
                    ReportEntry.builder()
                            .year(yearMonth.getYear())
                            .month(yearMonth.getMonthValue())
                            .amountLeft(amountLeftSummary)
                            .amountSpent(amountSpentSummary)
                            .amountPlanned(amountPlannedSummary)
                            .summaryRow(true)
                            .build()
            );
        }
    }

    private BigDecimal sum(List<ReportEntry> reportEntries, Function<ReportEntry, BigDecimal> function) {
        return reportEntries.stream()
                .map(function)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ReportEntry buildReportEntry(YearMonth yearMonth, ExpenseCategory category, BigDecimal budgetSumForCategory, BigDecimal expenseSumForCategory) {
        return ReportEntry.builder()
                .amountPlanned(budgetSumForCategory)
                .amountSpent(expenseSumForCategory)
                .amountLeft(budgetSumForCategory.subtract(expenseSumForCategory))
                .category(category)
                .month(yearMonth.getMonthValue())
                .year(yearMonth.getYear())
                .build();
    }

    private BigDecimal calculateExpensesSum(ExpenseCategory category, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth, List<Expense> expenses) {
        return expenses.stream()
                .filter(expense -> expense.getCategory().equals(category))
                .filter(expense -> expense.getTimestamp().isAfter(firstDayOfMonth.atStartOfDay()))
                .filter(expense -> expense.getTimestamp().isBefore(lastDayOfMonth.atTime(LocalTime.MAX)))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBudgetSum(YearMonth yearMonth, ExpenseCategory category, List<Budget> budgets) {
        return budgets.stream()
                .filter(budget -> budget.getCategory().equals(category))
                .filter(budget -> budget.getYear().equals(yearMonth.getYear()))
                .filter(budget -> budget.getMonth().equals(yearMonth.getMonthValue()))
                .map(Budget::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
