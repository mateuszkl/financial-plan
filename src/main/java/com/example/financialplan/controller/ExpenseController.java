package com.example.financialplan.controller;

import com.example.financialplan.common.YearMonthProvider;
import com.example.financialplan.entity.Expense;
import com.example.financialplan.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.YearMonth;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private static final String EXPENSES_ATTRIBUTE = "expenses";

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private YearMonthProvider yearMonthProvider;

    @GetMapping("/list")
    public String show(Model model) {

        model.addAttribute("expense", new Expense());
        model.addAttribute(EXPENSES_ATTRIBUTE, expenseRepository.getAllByYearAndMonth(YearMonth.now().getYear(), YearMonth.now().getMonthValue()));
        model.addAttribute("months", yearMonthProvider.populateMonths());
        model.addAttribute("years", yearMonthProvider.populateYears());

        return "index";
    }

    @PostMapping("/add")
    public String add(@Valid Expense expense, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }

        expenseRepository.save(expense);
        model.addAttribute("expense", new Expense());
        model.addAttribute(EXPENSES_ATTRIBUTE, expenseRepository.getAllByYearAndMonth(YearMonth.now().getYear(), YearMonth.now().getMonthValue()));
        model.addAttribute("months", yearMonthProvider.populateMonths());
        model.addAttribute("years", yearMonthProvider.populateYears());

        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Expense expense = expenseRepository.findById(id)
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

        expenseRepository.save(expense);
        model.addAttribute(EXPENSES_ATTRIBUTE, expenseRepository.findAll());
        return "index";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        expenseRepository.delete(expense);

        model.addAttribute("expense", new Expense());
        model.addAttribute(EXPENSES_ATTRIBUTE, expenseRepository.findAll());

        return "index";
    }
}
