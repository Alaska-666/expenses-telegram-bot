package utils;

import database.Database;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Util {
    public static void updateExpenseCategories(Map<String, List<String>> expenseCategories, Database database, String category, String userName) {
        database.addExpenseCategory(category, userName);
        if (expenseCategories.containsKey(userName)) {
            expenseCategories.get(userName).add(category);
        } else {
            List<String> values = new ArrayList<>();
            values.add(category);
            expenseCategories.put(userName, values);
        }
    }

    public static String getUserName(Update update) {
        User user = update.getMessage().getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public static void setAnswer(AbsSender sender, Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            sender.execute(answer);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Long getChatID(Update update) {
        if (update.getMessage() == null) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    private static void addButtonsInKeyboardButtonsRow(List<List<InlineKeyboardButton>> rowList, List<String> strings) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (String str : strings) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(str);
            inlineKeyboardButton.setCallbackData(str);
            keyboardButtonsRow.add(inlineKeyboardButton);
        }
        rowList.add(keyboardButtonsRow);
    }

    public static SendMessage sendInlineKeyBoardMessage(Long chatId, String textMessage, List<String> categories) {
        InlineKeyboardMarkup markup = createInlineKeyBoardMarkup(categories);
        SendMessage message = new SendMessage();
        message.setText(textMessage);
        message.setChatId(chatId.toString());
        message.setReplyMarkup(markup);
        return message;
    }

    public static EditMessageText editMessage(Long chatId, Integer messageId, String textMessage, List<String> categories) {
        InlineKeyboardMarkup markup = createInlineKeyBoardMarkup(categories);
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(textMessage);
        editMessage.setReplyMarkup(markup);
        return editMessage;
    }

    private static InlineKeyboardMarkup createInlineKeyBoardMarkup(List<String> categories) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(int i = 1; i < categories.size(); i+=2) {
            String firstCategory = categories.get(i-1);
            String secondCategory = categories.get(i);
            addButtonsInKeyboardButtonsRow(rowList, Arrays.asList(firstCategory, secondCategory));
        }
        if (categories.size() % 2 != 0) {
            addButtonsInKeyboardButtonsRow(rowList, Collections.singletonList(categories.get(categories.size() - 1)));
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
