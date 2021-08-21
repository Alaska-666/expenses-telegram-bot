package states;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

import java.util.Arrays;
import java.util.List;

public class ConfirmAlignExpensesState implements State{
    private List<String> users;
    private Database database;

    public ConfirmAlignExpensesState(List<String> users, Database database) {
        this.users = users;
        this.database = database;
    }


    @Override
    public State getNextState(Update update, AbsSender sender) {
        try {
            sender.execute(Util.sendInlineKeyBoardMessage(Util.getChatID(update),
                    String.format("Вы уверены, что хотите выравнять траты между %s?", String.join(", ", users)),
                    Arrays.asList("Да", "Нет")
            ));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        return new HandleConfirmAlignExpensesState(users, database);
    }
}
