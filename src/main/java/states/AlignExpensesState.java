package states;

import database.Database;
import expense.Expense;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlignExpensesState implements State{
    private List<String> users;
    private Database database;

    public AlignExpensesState(List<String> users, Database database) {
        this.users = users;
        this.database = database;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String people = users.stream().sorted().collect(Collectors.joining(","));
        List<Expense> expenses = database.readActualExpensesForPeople(people);
        Map<String, Long> amounts = new HashMap<>();
        Double total = 0.0;
        for (Expense expense : expenses) {
            total += expense.getCost();
            amounts.put(expense.getPayer(), expense.getCost() + amounts.getOrDefault(expense.getPayer(), 0L));
        }
        total /= users.size();
        StringBuilder message = new StringBuilder();
        if (users.size() == 2) {
            if (amounts.getOrDefault(users.get(0), 0L) > amounts.getOrDefault(users.get(1), 0L)) {
                message.append(String.format("%s -> %s : %.2f", users.get(1), users.get(0), total - amounts.getOrDefault(users.get(1), 0L)));
            } else {
                message.append(String.format("%s -> %s : %.2f", users.get(0), users.get(1), total - amounts.getOrDefault(users.get(0), 0L)));
            }
        } else {
            message.append("Долг:\n");
            for (String user: users) {
                message.append(String.format("%s : %.2f\n", user, total - amounts.getOrDefault(user, 0L)));
            }
        }
        Util.setAnswer(sender, Util.getChatID(update), message.toString());
        return new ConfirmAlignExpensesState(users, database).getNextState(update, sender);
    }
}
