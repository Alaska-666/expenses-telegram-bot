package states;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.List;

public class ReadExpenseCategoryState implements State {
    private final Database database;
    private final List<String> expenseCategories;

    public ReadExpenseCategoryState(Database database, List<String> expenseCategories) {
        this.database = database;
        this.expenseCategories = expenseCategories;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String category = update.getMessage().getText();
        if (expenseCategories.contains(category)) {
            Util.setAnswer(sender, update.getMessage().getChatId(), "Категория уже существует");
        } else {
            database.addExpenseCategory(category);
            expenseCategories.add(category);
        }
        return null;
    }
}
