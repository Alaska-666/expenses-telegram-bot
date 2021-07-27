package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class ReadExpensePayerState implements State {
    private final ExpenseBuilder expenseBuilder;
    private final Database database;

    public ReadExpensePayerState(ExpenseBuilder expenseBuilder, Database database) {
        this.expenseBuilder = expenseBuilder;
        this.database = database;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String payer;
        if (update.hasCallbackQuery()) {
            payer = update.getCallbackQuery().getData();
        } else {
            payer = update.getMessage().getText();
        }
        expenseBuilder.setPayer(payer);
        return ReadPeopleStatePreprocessing.execute(update, sender, database, expenseBuilder, database.readUsers(), false);
    }
}
