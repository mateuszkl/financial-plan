package com.example.financialplan.controller;


import com.example.financialplan.common.YearMonthProvider;
import com.example.financialplan.entity.Budget;
import com.example.financialplan.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.YearMonth;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private static final String BUDGET_ATRIBUTE = "budgets";

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private YearMonthProvider yearMonthProvider;

    @GetMapping("/list")
    public String show(Model model) {
        YearMonth current = YearMonth.now();

        model.addAttribute("budget", new Budget());
        model.addAttribute(BUDGET_ATRIBUTE, budgetRepository.getAllByYearAndMonth(current.getYear(),current.getMonthValue()));
        model.addAttribute("months", yearMonthProvider.populateMonths());
        model.addAttribute("years", yearMonthProvider.populateYears());

        return "budgetList";
    }

    @PostMapping("/add")
    public String add(@Valid Budget budget, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "budgetList";
        }

        budgetRepository.save(budget);
        model.addAttribute("budget", new Budget());
        model.addAttribute(BUDGET_ATRIBUTE, budgetRepository.findAll());
        model.addAttribute("months", yearMonthProvider.populateMonths());
        model.addAttribute("years", yearMonthProvider.populateYears());

        return "budgetList";
    }

}
