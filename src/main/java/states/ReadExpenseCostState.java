package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

class ReadExpenseCostState implements State {
    private final ExpenseBuilder expenseBuilder;
    private final Database database;

    public ReadExpenseCostState(ExpenseBuilder expenseBuilder, Database database) {
        this.expenseBuilder = expenseBuilder;
        this.database = database;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        try {
            Integer cost = Integer.parseInt(update.getMessage().getText());
            expenseBuilder.setCost(cost);
        } catch (NumberFormatException ignored) {
            Util.setAnswer(sender, Util.getChatID(update), "Введите целое число!");
            return new ReadExpenseCostState(expenseBuilder, database);
        }
        String message = "Выберете, кто заплатил";
        try {
            sender.execute(Util.sendInlineKeyBoardMessage(Util.getChatID(update), message, database.readUsers()));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        return new ReadExpensePayerState(expenseBuilder, database);
    }
}
