package accountant.vo;

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
        YearMonth month = YearMonth.parse(this.yearMonth, ofPattern("uuuuMM"));
        return month.lengthOfMonth();
    }

    public double dailyAmount() {
        return amount / dayCount();
    }
}
