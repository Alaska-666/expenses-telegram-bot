package expense;

public class Expense {
    private String name;
    private String payer;
    private Integer cost;

    public Expense(String name, String payer, Integer cost) {
        this.name = name;
        this.payer = payer;
        this.cost = cost;
    }
}
