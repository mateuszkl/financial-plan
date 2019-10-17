package com.example.financialplan.controller;

import com.example.financialplan.entity.Expense;
import com.example.financialplan.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private static final String EXPENSES_ATTRIBUTE = "expenses";

    @Autowired
    private ExpenseRepository expenseRepository;


    @GetMapping("/list")
    public String show(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute(EXPENSES_ATTRIBUTE, expenseRepository.findAll());

        return "index";
    }

    @PostMapping("/add")
    public String add(@Valid Expense expense, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }

        expenseRepository.save(expense);
        model.addAttribute(EXPENSES_ATTRIBUTE, expenseRepository.findAll());

        return "index";
    }
}
