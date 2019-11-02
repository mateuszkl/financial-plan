package com.example.financialplan.repository;


import com.example.financialplan.entity.Budget;
import com.example.financialplan.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository <Budget, Long> {

    List<Budget> getAllByYearAndMonthAndCategory(int year, int month, ExpenseCategory category);

}
