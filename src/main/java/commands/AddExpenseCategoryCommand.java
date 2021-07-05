package commands;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import states.ReadExpenseCategoryState;
import states.State;

import java.util.List;
import java.util.Map;

public class AddExpenseCategoryCommand extends ServiceCommand {
    private final Map<Long, State> states;
    private final Database database;
    private final List<String> expenseCategories;


    public AddExpenseCategoryCommand(String identifier, String description, Map<Long, State> states, Database database, List<String> expenseCategories) {
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

        if (strings.length > 1) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, "Введите ОДНУ категорию!");
        } else if (strings.length < 1) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, "Введите название категории");
            states.put(chat.getId(), new ReadExpenseCategoryState(database, expenseCategories));
        } else {
            String category = strings[0];
            if (expenseCategories.contains(category)) {
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, "Категория уже существует");
            } else {
                database.addExpenseCategory(strings[0]);
                expenseCategories.add(strings[0]);
            }
        }
    }
}
