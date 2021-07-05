package states;

import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Util;

class StateReadExpenseCost implements State {
    private final ExpenseBuilder expenseBuilder;

    public StateReadExpenseCost(ExpenseBuilder expenseBuilder) {
        this.expenseBuilder = expenseBuilder;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        Integer cost = Integer.parseInt(update.getMessage().getText());
        expenseBuilder.setCost(cost);
        Util.setAnswer(sender, update.getMessage().getChatId(), "Введите, кто заплатил");
        return new StateReadExpensePayer(expenseBuilder);
    }
}
