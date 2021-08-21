package states;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ReadPeopleStatePreprocessing {
    public static State execute(Long chatId, Update update, AbsSender sender, State newState, List<String> options) {
        String message = "Выберете, чья это покупка. Внимание, возможен множественный выбор. \n" +
                "Если хотите завершить выбирать, нажмите \"Добавить\".";
        try {
            List<String> buttons = new ArrayList<>(options);
            buttons.add("СБРОСИТЬ");
            buttons.add("ДОБАВИТЬ");
            if (update != null) {
                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                sender.execute(Util.editMessage(chatId, messageId, message, buttons));
            } else {
                sender.execute(Util.sendInlineKeyBoardMessage(chatId, message, buttons));
            }
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        return newState;
    }
}
