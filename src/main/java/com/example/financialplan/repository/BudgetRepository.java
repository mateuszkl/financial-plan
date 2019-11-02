package com.example.financialplan.repository;


import com.example.financialplan.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository <Budget, Long> {

}
