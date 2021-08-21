package commands;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import states.ReadPeopleStatePreprocessing;
import states.ReadUsersForAlignExpensesState;
import states.State;

import java.util.List;
import java.util.Map;

public class AlignExpensesCommand implements Command{
    private final Database database;
    private final Map<Long, State> states;

    public AlignExpensesCommand(Map<Long, State> states, Database database) {
        this.database = database;
        this.states = states;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        states.clear();
        List<String> users = database.readUsers();
        ReadPeopleStatePreprocessing.execute(chat.getId(), null, absSender, null, users);
        states.put(chat.getId(), new ReadUsersForAlignExpensesState(database, users));
    }
}
