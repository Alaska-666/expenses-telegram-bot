package states;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.List;
import java.util.stream.Collectors;

public class FinishedAlignExpensesState implements State{
    private List<String> users;
    private Database database;

    public FinishedAlignExpensesState(List<String> users, Database database) {
        this.users = users;
        this.database = database;
    }
    @Override
    public State getNextState(Update update, AbsSender sender) {
        String people = users.stream().sorted().collect(Collectors.joining(","));
        database.closedActualExpensesForPeople(people);
        Util.setAnswer(sender, Util.getChatID(update), "Выравнивание успешно завершено!");
        return null;
    }
}
