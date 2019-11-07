package com.example.financialplan.common;

import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class YearMonthProvider {

    public List<Integer> populateYears() {
        YearMonth yearMonth = YearMonth.now();
        List<Integer> years = new ArrayList<>();

        for (int i = yearMonth.getYear() - 2; i <= yearMonth.getYear() + 2; i++) {
            years.add(i);
        }
        return years;
    }

    public List<Integer> populateMonths() {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).collect(Collectors.toList());
    }
}
