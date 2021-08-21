package states;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.List;
import java.util.Map;

public class ReadExpenseCategoryState implements State {
    private final Database database;
    private final Map<String, List<String>> expenseCategories;

    public ReadExpenseCategoryState(Database database, Map<String, List<String>> expenseCategories) {
        this.database = database;
        this.expenseCategories = expenseCategories;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String category = update.getMessage().getText();
        String userName = Util.getUserName(update);
        if (expenseCategories.containsKey(userName) && expenseCategories.get(userName).contains(category)) {
            Util.setAnswer(sender, Util.getChatID(update), "Категория уже существует");
        } else {
            Util.updateExpenseCategories(expenseCategories, database, category, userName);
            Util.setAnswer(sender, Util.getChatID(update), "Категория добавлена");
        }
        return null;
    }
}
