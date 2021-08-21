package commands;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import states.ReadExpenseCategoryState;
import states.State;
import utils.Util;

import java.util.List;
import java.util.Map;

public class AddExpenseCategoryCommand implements Command {
    private final Map<Long, State> states;
    private final Database database;
    private final Map<String, List<String>> expenseCategories;


    public AddExpenseCategoryCommand(Map<Long, State> states, Database database, Map<String, List<String>> expenseCategories) {
        this.states = states;
        this.database = database;
        this.expenseCategories = expenseCategories;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        states.clear();
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        if (strings.length < 1) {
            Util.sendAnswer(absSender, chat.getId(), "Введите название категории");
            states.put(chat.getId(), new ReadExpenseCategoryState(database, expenseCategories));
        } else {
            String category = String.join(" ", strings);
            if (expenseCategories.containsKey(userName) && expenseCategories.get(userName).contains(category)) {
                Util.sendAnswer(absSender, chat.getId(), "Категория уже существует");
            } else {
                Util.updateExpenseCategories(expenseCategories, database, category, userName);
                Util.sendAnswer(absSender, chat.getId(), "Категория добавлена");
            }
        }
    }
}
