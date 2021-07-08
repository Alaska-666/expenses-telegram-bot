package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

import java.util.List;

public class ReadPeopleStatePreprocessing {
    public static ReadExpensePeopleState execute(Update update, AbsSender sender, Database database, ExpenseBuilder expenseBuilder, List<String> selectedUsers) {
        String message = "Выберете, чья это покупка. Внимание, возможен множественный выбор. \n" +
                "Если хотите завершить выбирать, нажмите \"Добавить\".";
        try {
            List<String> buttons = database.readUsers();
            buttons.add("СБРОСИТЬ");
            buttons.add("ДОБАВИТЬ");
            sender.execute(Util.sendInlineKeyBoardMessage(Util.getChatID(update), message, buttons));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        return new ReadExpensePeopleState(expenseBuilder, database, selectedUsers);
    }
}
