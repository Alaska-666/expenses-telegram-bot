package expense;

import java.util.List;

public class Expense {
    private String name;
    private String payer;
    private Integer cost;
    private List<String> whoseExpense;

    public Expense(String name, String payer, Integer cost, List<String> whoseExpense) {
        this.name = name;
        this.payer = payer;
        this.cost = cost;
        this.whoseExpense = whoseExpense;
    }
}
