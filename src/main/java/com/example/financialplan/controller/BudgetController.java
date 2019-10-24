package com.example.financialplan.controller;


import com.example.financialplan.entity.Budget;
import com.example.financialplan.entity.Expense;
import com.example.financialplan.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private static final String BUDGET_ATRIBUTE = "budgets";

    @Autowired
    private BudgetRepository budgetRepository;

    @GetMapping("/list")
    public String show (Model model){
        model.addAttribute("budget", new Budget());
        model.addAttribute(BUDGET_ATRIBUTE, budgetRepository.findAll());
        model.addAttribute("months", Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12));
        model.addAttribute("years", Arrays.asList(2019,2020));


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
        model.addAttribute("months", Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12));
        model.addAttribute("years", Arrays.asList(2019,2020));

        return "budgetList";
    }

}
