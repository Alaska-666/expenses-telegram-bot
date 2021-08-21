import commands.*;
import database.Database;
import expense.ExpenseBuilder;
import states.State;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingCommandBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final Database database;
    private final Map<Long,State> states = new HashMap<>();
    private final ExpenseBuilder expenseBuilder = new ExpenseBuilder();
    private final Map<String, List<String>> expenseCategories;

    public Bot(String botName, String botToken) {
        super();
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
        database = new Database("jdbc:sqlite:db/expense.db");
        database.createExpenseCategoriesTable();
        database.createExpensesTable();
        database.createUsersTable();
        expenseCategories = database.readExpenseCategories();
        System.out.println(expenseCategories);
        System.out.println(database.readUsers());

        //регистрируем команды
        register(new SafeCommand("start", "Старт", new StartCommand(database)));
        register(new HelpCommand());
        register(new SafeCommand("add_expense", "Добавить трату", new AddExpenseCommand(states, expenseBuilder, expenseCategories, database)));
        register(new SafeCommand("add_expense_category", "Добавить категорию расходов", new AddExpenseCategoryCommand(states, database, expenseCategories)));
        register(new SafeCommand("show_all_expenses_as_excel", "Показать все внесенные траты", new ShowAllExpensesAsExcelCommand(database)));
        register(new SafeCommand("align_expenses", "Выровнять расходы", new AlignExpensesCommand(states, database)));
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Long chatID = Util.getChatID(update);
        State currentState = states.get(chatID);
        State newState = currentState.getNextState(update, this);
        if (newState != null) {
            states.put(chatID, newState);
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}


