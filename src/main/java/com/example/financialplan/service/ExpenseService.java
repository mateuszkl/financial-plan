package com.example.financialplan.service;

import com.example.financialplan.common.YearMonthProvider;
import com.example.financialplan.entity.Expense;
import com.example.financialplan.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final YearMonthProvider yearMonthProvider;

    public Expense setYearMonthAndSave(Expense expense) {
        expense.setMonth(yearMonthProvider.getMonth());
        expense.setYear(yearMonthProvider.getYear());

        return expenseRepository.save(expense);
    }

    public Expense update(Expense expenseForm) {
        Expense expenseFromDb = expenseRepository.getOne(expenseForm.getId());

        expenseForm.setYear(expenseFromDb.getYear());
        expenseForm.setMonth(expenseFromDb.getMonth());
        expenseForm.setCreationDateTime(expenseFromDb.getCreationDateTime());

        expenseRepository.save(expenseForm);
        return expenseForm;
    }

    public List<Expense> getAllByYearAndMonth() {
        return expenseRepository.getAllByYearAndMonth(yearMonthProvider.getYear(), yearMonthProvider.getMonth());
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

}
