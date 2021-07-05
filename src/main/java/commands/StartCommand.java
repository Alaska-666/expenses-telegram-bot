package commands;

import commands.ServiceCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends ServiceCommand {
    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        String answer = String.format("Привет, %s! Если Вам нужна помощь, нажмите /help", userName);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, answer);
    }
}
