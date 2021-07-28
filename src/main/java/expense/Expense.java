package expense;

import java.util.List;
import java.util.stream.Collectors;

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

    public String getName() {
        return name;
    }

    public String getPayer() {
        return payer;
    }

    public Integer getCost() {
        return cost;
    }

    public String getWhoseExpense() {
        return whoseExpense.stream().sorted().collect(Collectors.joining(","));
    }
}
