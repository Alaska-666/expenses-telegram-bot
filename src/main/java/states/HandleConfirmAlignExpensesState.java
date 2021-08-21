package states;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

import java.util.List;

public class HandleConfirmAlignExpensesState implements State{
    private List<String> users;
    private Database database;

    public HandleConfirmAlignExpensesState(List<String> users, Database database) {
        this.users = users;
        this.database = database;
    }

    private void sendCancelMessage(Update update, AbsSender sender) {
        Util.setAnswer(sender, Util.getChatID(update), "Выравнивание трат отменено!");
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            if (call_data.equals("Нет")) {
                sendCancelMessage(update, sender);
                return null;
            } else {
                return new FinishedAlignExpensesState(users, database).getNextState(update, sender);
            }
        } else {
            sendCancelMessage(update, sender);
            return null;
        }
    }
}
