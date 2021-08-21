package commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.List;

public class SafeCommand extends BotCommand {
    private final Command command;
    private final List<String> allowedUsers = Arrays.asList("Isaeva_Ann", "arovesto");

    public SafeCommand(String identifier, String description, Command command) {
        super(identifier, description);
        this.command = command;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        if (allowedUsers.contains(userName)) {
            command.execute(absSender, user, chat, strings);
        }
    }
}
