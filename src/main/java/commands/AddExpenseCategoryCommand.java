package commands;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import states.ReadExpenseCategoryState;
import states.State;
import utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddExpenseCategoryCommand extends ServiceCommand {
    private final Map<Long, State> states;
    private final Database database;
    private final Map<String, List<String>> expenseCategories;


    public AddExpenseCategoryCommand(String identifier, String description, Map<Long, State> states, Database database, Map<String, List<String>> expenseCategories) {
        super(identifier, description);
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
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, "Введите название категории");
            states.put(chat.getId(), new ReadExpenseCategoryState(database, expenseCategories));
        } else {
            String category = String.join(" ", strings);
            System.out.println(category);
            System.out.println(expenseCategories.get(userName));
            if (expenseCategories.containsKey(userName) && expenseCategories.get(userName).contains(category)) {
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, "Категория уже существует");
            } else {
                Util.updateExpenseCategories(expenseCategories, database, category, userName);
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, "Категория добавлена");
            }
        }
    }
}
