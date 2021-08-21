package commands;

import database.Database;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

public class StartCommand implements Command{
    private final Database database;

    public StartCommand(Database database) {
        this.database = database;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        if (!database.readUsers().contains(userName)) {
            database.addUser(userName);
        }
        String answer = String.format("Привет, %s! Если Вам нужна помощь, нажмите /help", userName);
        Util.sendAnswer(absSender, chat.getId(), answer);
    }
}
