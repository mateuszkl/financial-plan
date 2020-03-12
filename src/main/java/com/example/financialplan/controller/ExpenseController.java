package com.example.financialplan.controller;

import com.example.financialplan.common.YearMonthProvider;
import com.example.financialplan.entity.Expense;
import com.example.financialplan.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final YearMonthProvider yearMonthProvider;
    private final ExpenseService expenseService;

    @GetMapping("/list")
    public String show(@ModelAttribute Expense expense) {
        return "index";
    }

    @ModelAttribute
    public void initializeViewData(Model model) {
        model.addAttribute("expenses", expenseService.getAllByYearAndMonth());
        model.addAttribute("months", yearMonthProvider.getMonths());
        model.addAttribute("years", yearMonthProvider.getYears());
        model.addAttribute("year", yearMonthProvider.getYear());
        model.addAttribute("month", yearMonthProvider.getMonth());
    }

    @PostMapping("/add")
    public String add(@Valid Expense expense, BindingResult result) {
        if (result.hasErrors()) {
            return "index";
        }
        expenseService.setYearMonthAndSave(expense);

        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Expense expense = expenseService.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        model.addAttribute("expense", expense);

        return "editExpense";
    }

    @PostMapping("/edit/{id}")
    public String saveEdit(@Valid Expense expense, @PathVariable("id") Long id, BindingResult result, Model model) {
        expense.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("expense", expense);

            return "editExpense";
        }

        expenseService.update(expense);

        return "index";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Expense expense = expenseService.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        expenseService.delete(expense);

        return "index";
    }
}
