package com.example.financialplan.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Component
public class YearMonthProvider {

    private int year;
    private int month;

    private List<Integer> years;
    private List<Integer> months;

    @PostConstruct
    public void init() {
        YearMonth now = YearMonth.now();
        year = now.getYear();
        month = now.getMonthValue();

        years = populateYears();
        months = populateMonths();
    }

    private List<Integer> populateYears() {
        YearMonth yearMonth = YearMonth.now();

        return IntStream.rangeClosed(yearMonth.getYear() - 2, yearMonth.getYear() + 2)
                .boxed()
                .collect(Collectors.toList());
    }

    private List<Integer> populateMonths() {
        return IntStream.rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toList());
    }
}
