import commands.AddExpenseCommand;
import commands.StartCommand;
import expense.ExpenseBuilder;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import states.State;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Util;

import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingCommandBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String databaseConnectionUrl = "jdbc:sqlite::memory:";
    private final Map<Long,State> states = new HashMap<>();
    private final ExpenseBuilder expenseBuilder = new ExpenseBuilder();

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;

        //регистрируем команды
        register(new StartCommand("start", "Старт"));
        register(new HelpCommand());
        register(new AddExpenseCommand("add_expense", "Добавить трату", states, expenseBuilder));
    }

    public String getDatabaseConnectionUrl() {
        return databaseConnectionUrl;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        // TODO create util function "getChatID(update)"
        Long chatID;
        if (update.getMessage() == null) {
            chatID = update.getCallbackQuery().getMessage().getChatId();
            System.out.println("AAAAAAAA");
        } else {
            System.out.println("processNonCommandUpdate " + update.getMessage().toString());
            chatID = update.getMessage().getChatId();
        }

        State currentState = states.get(chatID);
        State newState = currentState.getNextState(update, this);
        states.put(chatID, newState);
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}


