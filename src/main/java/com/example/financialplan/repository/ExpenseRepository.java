package com.example.financialplan.repository;

import com.example.financialplan.entity.Expense;
import com.example.financialplan.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> getAllByCreationDateTimeAfterAndCreationDateTimeBeforeAndCategory(LocalDateTime after, LocalDateTime before, ExpenseCategory category);
}
