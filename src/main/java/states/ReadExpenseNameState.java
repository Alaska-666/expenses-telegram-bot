package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

public class ReadExpenseNameState implements State {
    private final ExpenseBuilder expenseBuilder;
    private final Database database;

    public ReadExpenseNameState(ExpenseBuilder expenseBuilder, Database database) {
        this.expenseBuilder = expenseBuilder;
        this.database = database;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String expenseName;
        if (update.hasCallbackQuery()) {
            expenseName = update.getCallbackQuery().getData();
        } else {
            expenseName = update.getMessage().getText();
        }
        expenseBuilder.setName(expenseName);
        Util.setAnswer(sender, Util.getChatID(update), "Введите, сколько Вы потратили");
        return new ReadExpenseCostState(expenseBuilder, database);
    }
}