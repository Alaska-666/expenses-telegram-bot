package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.*;
import java.util.stream.Collectors;

public class ReadExpensePeopleState implements State {
    private final ExpenseBuilder expenseBuilder;
    private final Database database;
    private List<String> users;
    private final String SELECTED_SYMBOL = "*";

    public ReadExpensePeopleState(ExpenseBuilder expenseBuilder, Database database, List<String> users) {
        this.expenseBuilder = expenseBuilder;
        this.database = database;
        this.users = users;
    }

    private String stringWithoutLastChar(String str) {
        return new StringBuffer(str).deleteCharAt(str.length() - 1).toString();
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            if (call_data.equals("ДОБАВИТЬ")) {
                expenseBuilder.setPeople(
                        this.users.stream()
                                .filter(s -> s.endsWith(SELECTED_SYMBOL))
                                .map(this::stringWithoutLastChar)
                                .collect(Collectors.toList())
                );
                database.addExpense(expenseBuilder.getExpense());
                Util.setAnswer(sender, Util.getChatID(update), "Трата успешно добавлена!");
                return null;
            } else if (call_data.equals("СБРОСИТЬ")) {
                users = database.readUsers();
            } else {
                String newUser = call_data.endsWith(SELECTED_SYMBOL) ? stringWithoutLastChar(call_data): call_data.concat(SELECTED_SYMBOL);
                users.set(users.indexOf(call_data), newUser);
            }
            return ReadPeopleStatePreprocessing.execute(update, sender, database, expenseBuilder, users, true);
        } else {
            return ReadPeopleStatePreprocessing.execute(update, sender, database, expenseBuilder, users, false);
        }
    }
}
