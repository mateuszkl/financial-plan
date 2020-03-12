package com.example.financialplan.controller;


import com.example.financialplan.common.YearMonthProvider;
import com.example.financialplan.entity.Budget;
import com.example.financialplan.repository.BudgetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetRepository budgetRepository;

    private final YearMonthProvider yearMonthProvider;

    public BudgetController(BudgetRepository budgetRepository, YearMonthProvider yearMonthProvider) {
        this.budgetRepository = budgetRepository;
        this.yearMonthProvider = yearMonthProvider;
    }

    @GetMapping("/list")
    public String show(@ModelAttribute Budget budget) {
        return "budgetList";
    }

    @PostMapping("/add")
    public String add(@Valid Budget budget, BindingResult result) {
        if (result.hasErrors()) {
            return "budgetList";
        }

        budgetRepository.save(budget);

        return "budgetList";
    }

    @ModelAttribute
    public void initializeViewData(Model model) {
        model.addAttribute("budgets", budgetRepository.getAllByYearAndMonth(yearMonthProvider.getYear(), yearMonthProvider.getMonth()));
        model.addAttribute("months", yearMonthProvider.getMonths());
        model.addAttribute("years", yearMonthProvider.getYears());
    }

}
