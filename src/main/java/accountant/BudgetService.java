package accountant;

import accountant.vo.Budget;

import java.time.LocalDate;
import java.time.Period;
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
                    if (YearMonth.from(currentDate).equals(YearMonth.from(start))) {

                        double firstMonthAmount = budget.dailyAmount() * dayCount(start, budget.lastDay());

                        totalAmount += firstMonthAmount;
                    }
                    else if (YearMonth.from(currentDate).equals(YearMonth.from(end))) {

                        double lastMonthAmount = budget.dailyAmount() * dayCount(budget.firstDay(), end);

                        totalAmount += lastMonthAmount;
                    }
                    else {

                        if (currentBudget.isPresent()) {

                            totalAmount += budget.dailyAmount() * dayCount(budget.firstDay(), budget.lastDay());
                        }
                    }
                }
//                if (YearMonth.from(currentDate).equals(YearMonth.from(start))) {
//
//                    double firstMonthAmount = 0;
//                    if (currentBudget.isPresent()) {
////                        Budget budget = currentBudget.get();
//                        firstMonthAmount = budget.dailyAmount() * dayCount(start, budget.lastDay());
//                    }
//
//                    totalAmount += firstMonthAmount;
//                }
//                else if (YearMonth.from(currentDate).equals(YearMonth.from(end))) {
//
//                    double lastMonthAmount = 0;
//                    if (currentBudget.isPresent()) {
////                        Budget budget = currentBudget.get();
//                        lastMonthAmount = budget.dailyAmount() * dayCount(budget.firstDay(), end);
//                    }
//
//                    totalAmount += lastMonthAmount;
//                }
//                else {
//
//                    if (currentBudget.isPresent()) {
//
////                        Budget budget = currentBudget.get();
//                        totalAmount += budget.dailyAmount() * dayCount(budget.firstDay(), budget.lastDay());
//                    }
//                }
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
}
