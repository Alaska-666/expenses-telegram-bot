package commands;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import states.State;
import states.ReadExpenseNameState;
import utils.Util;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AddExpenseCommand implements Command {
    private final Map<Long, State> states;
    private final ExpenseBuilder expenseBuilder;
    private final Map<String, List<String>> expenseCategories;
    private final Database database;

    public AddExpenseCommand(Map<Long, State> states, ExpenseBuilder expenseBuilder, Map<String, List<String>> expenseCategories, Database database) {
        this.states = states;
        this.expenseBuilder = expenseBuilder;
        this.expenseCategories = expenseCategories;
        this.database = database;
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        states.clear();
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        String message = "Введите название траты:";
        try {
            absSender.execute(Util.sendInlineKeyBoardMessage(chat.getId(), message, expenseCategories.getOrDefault(userName, Collections.emptyList())));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        states.put(chat.getId(), new ReadExpenseNameState(expenseBuilder, database));
    }
}
