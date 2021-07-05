package states;

import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Util;

public class StateReadExpenseName implements State {
    private final ExpenseBuilder expenseBuilder;

    public StateReadExpenseName(ExpenseBuilder expenseBuilder) {
        this.expenseBuilder = expenseBuilder;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String expenseName;
        if (update.hasCallbackQuery()) {
            expenseName = update.getCallbackQuery().getData();
        } else {
            expenseName = update.getMessage().getText();
        }
        System.out.println(expenseName);
        expenseBuilder.setName(expenseName);
        Util.setAnswer(sender, update.getMessage().getChatId(), "Введите, сколько Вы потратили");
        return new StateReadExpenseCost(expenseBuilder);
    }
}