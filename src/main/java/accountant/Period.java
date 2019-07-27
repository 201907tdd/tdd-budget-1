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
        long dayCount;
        LocalDate overlappingStart;
        LocalDate overlappingEnd;
        if (YearMonth.from(budget.firstDay()).equals(YearMonth.from(getStart()))) {
            overlappingStart = getStart();
            overlappingEnd = budget.lastDay();
            dayCount = dayCount(overlappingStart, overlappingEnd);
        }
        else if (YearMonth.from(budget.lastDay()).equals(YearMonth.from(getEnd()))) {
            overlappingStart = budget.firstDay();
            overlappingEnd = getEnd();
            dayCount = dayCount(overlappingStart, overlappingEnd);
        }
        else {
            overlappingStart = budget.firstDay();
            overlappingEnd = budget.lastDay();
            dayCount = dayCount(overlappingStart, overlappingEnd);
        }
        return dayCount;
    }
}
