package accountant;

import accountant.vo.Budget;

import java.time.LocalDate;
import java.time.YearMonth;

import static java.time.temporal.ChronoUnit.DAYS;

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    static long dayCount(LocalDate start, LocalDate end) {
        return DAYS.between(start, end) + 1;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public long getOverlappingDays(Budget budget) {
        LocalDate overlappingStart = start.isAfter(budget.firstDay()) ? start : budget.firstDay();
        LocalDate overlappingEnd = end.isBefore(budget.lastDay()) ? end : budget.lastDay();

        return dayCount(overlappingStart, overlappingEnd);
    }
}
