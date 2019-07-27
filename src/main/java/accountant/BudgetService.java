package accountant;

import accountant.vo.Budget;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BudgetService {
    private BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }

        if (start.isEqual(end)) {
            Optional<Budget> budget = getBudget(start);

            if (budget.isPresent()) {
                return budget.get().dailyAmount();
            }

            return 0;
        }

        if (diffMonth(start, end) == 0) {
            return calculateBudgetAverage(start) * diffDay(start, end);
        }

        int diffMonth = diffMonth(start, end);
        if (diffMonth > 0) {
            double partialBudget = calculateBudgetAverage(start) * (start.lengthOfMonth() - start.getDayOfMonth() + 1) +
                    calculateBudgetAverage(end) * (end.getDayOfMonth());
            for (int i = 1; i < diffMonth; i++) {
                LocalDate middle = start.plusMonths(i);
                partialBudget += calculateBudgetAverage(middle) * middle.lengthOfMonth();
            }
            return partialBudget;
        }
        return 0;
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

    private int diffDay(LocalDate start, LocalDate end) {
        return end.getDayOfMonth() - start.getDayOfMonth() + 1;
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
