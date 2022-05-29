package za.co.knonchalant.telegram.scheduled;

import com.pengrad.telegrambot.model.Update;
import za.co.knonchalant.candogram.handlers.update.TelegramUpdate;

public class TargettedUpdate extends TelegramUpdate {
    private long userId;

    public TargettedUpdate(Update update) {
        super(update);
    }

    public TargettedUpdate(long userId) {
        super(null);
        this.userId = userId;
    }

    @Override
    public long getChatId() {
        return userId;
    }
}
