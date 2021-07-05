package states;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface State {
    State getNextState(Update update, AbsSender sender);
}