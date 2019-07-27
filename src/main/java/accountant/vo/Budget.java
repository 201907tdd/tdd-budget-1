package accountant.vo;

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

    private YearMonth getMonth() {
        return YearMonth.parse(this.yearMonth, ofPattern("uuuuMM"));
    }

    public double dailyAmount() {
        return amount / dayCount();
    }

    public LocalDate lastDay() {
        return getMonth().atEndOfMonth();
    }
}
