package expense;

import java.util.List;

public class ExpenseBuilder {
    private String name;
    private String payer;
    private Integer cost;
    private List<String> people;

    public ExpenseBuilder() {}

    public Expense getExpense() {
        return new Expense(name, payer, cost, people);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }
}
