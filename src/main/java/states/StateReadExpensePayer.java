package states;

import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

public class StateReadExpensePayer implements State{
    private final ExpenseBuilder expenseBuilder;

    public StateReadExpensePayer(ExpenseBuilder expenseBuilder) {
        this.expenseBuilder = expenseBuilder;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        String payer = update.getMessage().getText();
        expenseBuilder.setPayer(payer);
        Util.setAnswer(sender, update.getMessage().getChatId(), "Введите");
        return null;
    }
}
