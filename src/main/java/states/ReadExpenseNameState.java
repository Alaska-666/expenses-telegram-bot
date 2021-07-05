package states;

import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

public class ReadExpenseNameState implements State {
    private final ExpenseBuilder expenseBuilder;

    public ReadExpenseNameState(ExpenseBuilder expenseBuilder) {
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
        expenseBuilder.setName(expenseName);
        Util.setAnswer(sender, update.getMessage().getChatId(), "Введите, сколько Вы потратили");
        return new ReadExpenseCostState(expenseBuilder);
    }
}