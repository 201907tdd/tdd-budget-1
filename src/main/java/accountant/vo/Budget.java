package accountant.vo;

import accountant.Period;

import java.time.LocalDate;
import java.time.YearMonth;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Budget {
    public String yearMonth;
    public int amount;

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public int getAmount() {
        return amount;
    }

    public int dayCount() {
        YearMonth month = getMonth();
        return month.lengthOfMonth();
    }

    public LocalDate firstDay() {
        return getMonth().atDay(1);
    }

    public double dailyAmount() {
        return amount / dayCount();
    }

    public LocalDate lastDay() {
        return getMonth().atEndOfMonth();
    }

    public Period getPeriod() {
        return new Period(firstDay(), lastDay());
    }

    private YearMonth getMonth() {
        return YearMonth.parse(this.yearMonth, ofPattern("uuuuMM"));
    }
}
