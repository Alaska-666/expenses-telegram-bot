package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ReadPeopleStatePreprocessing {
    public static ReadExpensePeopleState execute(Update update, AbsSender sender, Database database, ExpenseBuilder expenseBuilder, List<String> users, boolean edit) {
        String message = "Выберете, чья это покупка. Внимание, возможен множественный выбор. \n" +
                "Если хотите завершить выбирать, нажмите \"Добавить\".";
        try {
            List<String> buttons = new ArrayList<>(users);
            buttons.add("СБРОСИТЬ");
            buttons.add("ДОБАВИТЬ");
            Long chatId = Util.getChatID(update);
            if (edit) {
                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                sender.execute(Util.editMessage(chatId, messageId, message, buttons));
            } else {
                sender.execute(Util.sendInlineKeyBoardMessage(chatId, message, buttons));
            }
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        return new ReadExpensePeopleState(expenseBuilder, database, users);
    }
}
