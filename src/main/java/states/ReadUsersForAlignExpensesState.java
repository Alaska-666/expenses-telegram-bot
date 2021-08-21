package states;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.List;
import java.util.stream.Collectors;

public class ReadUsersForAlignExpensesState implements State {
    private final Database database;
    private List<String> users;
    private final String SELECTED_SYMBOL = "✔";

    public ReadUsersForAlignExpensesState(Database database, List<String> users) {
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
                List<String> selectedUsers = this.users.stream()
                        .filter(s -> s.endsWith(SELECTED_SYMBOL))
                        .map(this::stringWithoutLastChar)
                        .collect(Collectors.toList());
                return new AlignExpensesState(selectedUsers, database).getNextState(update, sender);
            } else if (call_data.equals("СБРОСИТЬ")) {
                users = database.readUsers();
            } else {
                String newUser = call_data.endsWith(SELECTED_SYMBOL) ? stringWithoutLastChar(call_data): call_data.concat(SELECTED_SYMBOL);
                users.set(users.indexOf(call_data), newUser);
            }
            return ReadPeopleStatePreprocessing.execute(Util.getChatID(update), update, sender, new ReadUsersForAlignExpensesState(database, users), users);
        } else {
            return ReadPeopleStatePreprocessing.execute(Util.getChatID(update), null, sender, new ReadUsersForAlignExpensesState(database, users), users);
        }
    }
}
