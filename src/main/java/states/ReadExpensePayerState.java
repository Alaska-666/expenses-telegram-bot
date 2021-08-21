package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.List;

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
        List<String> users = database.readUsers();
        return ReadPeopleStatePreprocessing.execute(Util.getChatID(update), null, sender, new ReadExpensePeopleState(expenseBuilder, database, users), users);
    }
}
