package expense;

import expense.Expense;

public class ExpenseBuilder {
    private String name;
    private String payer;
    private Integer cost;

    public ExpenseBuilder() {}

    public Expense getExpense() {
        return new Expense(name, payer, cost);
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
}
