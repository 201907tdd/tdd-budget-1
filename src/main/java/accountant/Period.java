package accountant;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public long getOverlappingDays(Period another) {

        if (hasNoOverlapping(another)) {
            return 0;
        }

        LocalDate overlappingStart = start.isAfter(another.start) ? start : another.start;
        LocalDate overlappingEnd = end.isBefore(another.end) ? end : another.end;

        return dayCount(overlappingStart, overlappingEnd);
    }

    private long dayCount(LocalDate start, LocalDate end) {
        return DAYS.between(start, end) + 1;
    }

    private boolean hasNoOverlapping(Period another) {
        return end.isBefore(another.start) || start.isAfter(another.end);
    }
}
