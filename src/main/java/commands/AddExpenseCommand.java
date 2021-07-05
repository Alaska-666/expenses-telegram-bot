package commands;

import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import states.State;
import states.StateReadExpenseName;
import utils.Util;

import java.util.Map;


public class AddExpenseCommand extends ServiceCommand {
    private final Map<Long, State> states;
    private final ExpenseBuilder expenseBuilder;

    public AddExpenseCommand(String identifier, String description, Map<Long, State> states, ExpenseBuilder expenseBuilder) {
        super(identifier, description);
        this.states = states;
        this.expenseBuilder = expenseBuilder;
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        String answer = "Введите название траты:";
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, answer);
        try {
            absSender.execute(Util.sendInlineKeyBoardMessage(chat.getId()));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        states.put(chat.getId(), new StateReadExpenseName(expenseBuilder));
    }
}
