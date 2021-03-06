package com.example.financialplan.repository;

import com.example.financialplan.entity.Expense;
import com.example.financialplan.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> getAllByYearAndMonthAndCategory(int year, int month, ExpenseCategory category);
    List<Expense> getAllByYearAndMonth(int year, int month);

}
