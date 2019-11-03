package com.example.financialplan.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.YearMonth;

@Controller
public class SelectedYearMonthController {

    @Getter
    @Setter
    private int year;

    @Getter
    @Setter
    private int month;

    @PostConstruct
    public void init() {
        year = YearMonth.now().getYear();
        month = YearMonth.now().getMonthValue();
    }
}
