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

        double totalAmount = 0;

        Period period = new Period(start, end);
        LocalDate currentDate = start;
        while (currentDate.isBefore(YearMonth.from(end).atDay(1).plusMonths(1))) {

            Optional<Budget> currentBudget = getBudget(currentDate);
            if (currentBudget.isPresent()) {
                Budget budget = currentBudget.get();

                totalAmount += budget.getOverlappingAmount(period);
            }
            currentDate = currentDate.plusMonths(1);
        }
        return totalAmount;
    }

    private Optional<Budget> getBudget(LocalDate currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        return budgetRepo.getAll()
                .stream()
                .filter(b -> b.getYearMonth().equals(currentDate.format(formatter))).findFirst();
    }
}
