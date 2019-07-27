package accountant;

import accountant.vo.Budget;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

public class BudgetService {
    private BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }

        double totalAmount = 0;
        if (diffMonth(start, end) == 0) {

            Optional<Budget> budget = getBudget(start);

            if (budget.isPresent()) {
                return budget.get().dailyAmount() * dayCount(start, end);
            }
        }
        else {

            LocalDate currentDate = start;
            while (currentDate.isBefore(YearMonth.from(end).atDay(1).plusMonths(1))) {

                Optional<Budget> currentBudget = getBudget(currentDate);
                if (currentBudget.isPresent()) {
                    Budget budget = currentBudget.get();

                    totalAmount += budget.dailyAmount() * getOverlappingDays(new Period(start, end), budget);
                }
                currentDate = currentDate.plusMonths(1);
            }
        }
        return totalAmount;
    }

    private double calculateBudgetAverage(LocalDate start) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        for (Budget budget : this.budgetRepo.getAll()) {
            if (budget.getYearMonth().equals(start.format(formatter))) {
                return budget.getAmount() / start.lengthOfMonth();
            }
        }
        return 0D;
    }

    private long dayCount(LocalDate start, LocalDate end) {
        return DAYS.between(start, end) + 1;
    }

    private int diffMonth(LocalDate start, LocalDate end) {
        if (start.getYear() == end.getYear()) {
            return Math.abs(start.getMonth().getValue() - end.getMonth().getValue());
        }
        YearMonth from = YearMonth.from(start);
        return YearMonth.from(end).minusMonths(from.getMonthValue()).getMonthValue();
    }

    private Optional<Budget> getBudget(LocalDate currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        return budgetRepo.getAll()
                .stream()
                .filter(b -> b.getYearMonth().equals(currentDate.format(formatter))).findFirst();
    }

    private long getOverlappingDays(Period period, Budget budget) {
        long dayCount;
        if (YearMonth.from(budget.firstDay()).equals(YearMonth.from(period.getStart()))) {
            dayCount = dayCount(period.getStart(), budget.lastDay());
        }
        else if (YearMonth.from(budget.lastDay()).equals(YearMonth.from(period.getEnd()))) {
            dayCount = dayCount(budget.firstDay(), period.getEnd());
        }
        else {
            dayCount = dayCount(budget.firstDay(), budget.lastDay());
        }
        return dayCount;
    }
}
