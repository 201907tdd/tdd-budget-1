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

        Period period = new Period(start, end);
        double totalAmount = 0;
        for (Budget budget : budgetRepo.getAll()) {

            totalAmount += budget.getOverlappingAmount(period);
        }
        return totalAmount;
    }
}
