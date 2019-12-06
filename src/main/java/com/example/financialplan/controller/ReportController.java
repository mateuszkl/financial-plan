package com.example.financialplan.controller;

import com.example.financialplan.common.YearMonthProvider;
import com.example.financialplan.entity.Budget;
import com.example.financialplan.entity.Expense;
import com.example.financialplan.entity.ExpenseCategory;
import com.example.financialplan.entity.ReportEntry;
import com.example.financialplan.repository.BudgetRepository;
import com.example.financialplan.repository.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ExpenseRepository expenseRepository;

    private final BudgetRepository budgetRepository;

    private final YearMonthProvider yearMonthProvider;

    public ReportController(ExpenseRepository expenseRepository, BudgetRepository budgetRepository, YearMonthProvider yearMonthProvider) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
        this.yearMonthProvider = yearMonthProvider;
    }

    @GetMapping
    public String show(Model model) {
        model.addAttribute("years", yearMonthProvider.getYears());
        model.addAttribute("months", yearMonthProvider.getMonths());
        model.addAttribute("reportEntries", generateReport());

        return "report";
    }

    private List<ReportEntry> generateReport() {
        List<ReportEntry> reportEntries = new ArrayList<>();

        List<Expense> expenses = expenseRepository.getAllByYearAndMonth(yearMonthProvider.getYear(), yearMonthProvider.getMonth());
        List<Budget> budgets = budgetRepository.getAllByYearAndMonth(yearMonthProvider.getYear(), yearMonthProvider.getMonth());

        for (ExpenseCategory category : ExpenseCategory.values()) {

            LocalDate firstDayOfMonth = LocalDate.of(yearMonthProvider.getYear(), yearMonthProvider.getMonth(), 1);
            LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

            List<Budget> budgetsInCategory = budgets.stream()
                    .filter(budget -> category.equals(budget.getCategory()))
                    .collect(Collectors.toList());

            BigDecimal budgetSumForCategory = calculateBudgetSum(budgetsInCategory);

            List<Expense> expensesForCategory = expenses.stream()
                    .filter(expense -> category.equals(expense.getCategory()))
                    .collect(Collectors.toList());

            BigDecimal expenseSumForCategory = calculateExpensesSum(firstDayOfMonth, lastDayOfMonth, expensesForCategory);

            reportEntries.add(buildReportEntry(category, budgetSumForCategory, expenseSumForCategory));
        }

        addSummaryRow(reportEntries);

        return reportEntries;
    }

    private void addSummaryRow(List<ReportEntry> reportEntries) {
        if (!reportEntries.isEmpty()) {
            BigDecimal amountLeftSummary = sum(reportEntries, ReportEntry::getAmountLeft);
            BigDecimal amountSpentSummary = sum(reportEntries, ReportEntry::getAmountSpent);
            BigDecimal amountPlannedSummary = sum(reportEntries, ReportEntry::getAmountPlanned);

            reportEntries.add(
                    ReportEntry.builder()
                            .year(yearMonthProvider.getYear())
                            .month(yearMonthProvider.getMonth())
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

    private ReportEntry buildReportEntry(ExpenseCategory category, BigDecimal budgetSumForCategory, BigDecimal expenseSumForCategory) {
        return ReportEntry.builder()
                .amountPlanned(budgetSumForCategory)
                .amountSpent(expenseSumForCategory)
                .amountLeft(budgetSumForCategory.subtract(expenseSumForCategory))
                .category(category)
                .month(yearMonthProvider.getMonth())
                .year(yearMonthProvider.getYear())
                .build();
    }

    private BigDecimal calculateExpensesSum(LocalDate firstDayOfMonth, LocalDate lastDayOfMonth, List<Expense> expenses) {
        return expenses.stream()
                .filter(expense -> expense.getCreationDateTime().isAfter(firstDayOfMonth.atStartOfDay()))
                .filter(expense -> expense.getCreationDateTime().isBefore(lastDayOfMonth.atTime(LocalTime.MAX)))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBudgetSum(List<Budget> budgets) {
        return budgets.stream()
                .filter(budget -> budget.getYear().equals(yearMonthProvider.getYear()))
                .filter(budget -> budget.getMonth().equals(yearMonthProvider.getMonth()))
                .map(Budget::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
