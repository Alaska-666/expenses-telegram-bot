package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

import java.util.ArrayList;
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
        return ReadPeopleStatePreprocessing.execute(update, sender, database, expenseBuilder, new ArrayList<>());
    }
}
