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

    public double getOverlappingAmount(Period period) {
        return dailyAmount() * period.getOverlappingDays(getPeriod());
    }

    private double dailyAmount() {
        return amount / dayCount();
    }

    private int dayCount() {
        return getMonth().lengthOfMonth();
    }

    private LocalDate firstDay() {
        return getMonth().atDay(1);
    }

    private YearMonth getMonth() {
        return YearMonth.parse(this.yearMonth, ofPattern("uuuuMM"));
    }

    private Period getPeriod() {
        return new Period(firstDay(), lastDay());
    }

    private LocalDate lastDay() {
        return getMonth().atEndOfMonth();
    }
}
