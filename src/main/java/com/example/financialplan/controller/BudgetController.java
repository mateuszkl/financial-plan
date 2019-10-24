package com.example.financialplan.controller;


import com.example.financialplan.entity.Budget;
import com.example.financialplan.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private static final String BUDGET_ATRIBUTE = "budget";

    @Autowired
    private BudgetRepository budgetRepository;


    @GetMapping("/list")
    public String show (Model model){
        model.addAttribute("budget", new Budget());
        model.addAttribute(BUDGET_ATRIBUTE, budgetRepository.findAll());


        return "budgetList";
    }

}
