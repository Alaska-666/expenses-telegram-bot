package states;

import database.Database;
import expense.ExpenseBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

public class ReadExpensePeopleState implements State {
    private final ExpenseBuilder expenseBuilder;
    private final Database database;
    private List<String> selectedUsers;

    public ReadExpensePeopleState(ExpenseBuilder expenseBuilder, Database database, List<String> selectedUsers) {
        this.expenseBuilder = expenseBuilder;
        this.database = database;
        this.selectedUsers = selectedUsers;
    }

    @Override
    public State getNextState(Update update, AbsSender sender) {
        if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            System.out.println(call_data);
            if (call_data.equals("ДОБАВИТЬ")) {
                expenseBuilder.setPeople(this.selectedUsers);
                return new AddToDatabaseState(expenseBuilder, database);
            } else if (call_data.equals("СБРОСИТЬ")) {
                selectedUsers = new ArrayList<>();
            }
            // TODO edit message

            return new ReadExpensePeopleState(expenseBuilder, database, selectedUsers);
//                String answer = "Updated message text";
//                EditMessageText new_message = new EditMessageText()
//                        .setChatId(chat_id)
//                        .setMessageId(toIntExact(message_id))
//                        .setText(answer);
//                try {
//                    execute(new_message);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
        } else {
            return ReadPeopleStatePreprocessing.execute(update, sender, database, expenseBuilder, selectedUsers);
        }
    }
}
