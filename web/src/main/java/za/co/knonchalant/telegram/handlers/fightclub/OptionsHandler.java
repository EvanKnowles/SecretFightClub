package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;

/**
 * Created by evan on 2016/04/08.
 */
public class OptionsHandler extends FightClubMessageHandler {
    public OptionsHandler(String botName, IBotAPI bot) {
        super(botName, "menu", bot, true);
    }

    @Override
    public String getDescription() {
        return "A handy clickable set of commands.";
    }

    @Override
    public PendingResponse handle(IUpdate update) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(getButtons());
        getBot().sendMessage(update.getChatId(), "What do you want to use?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        return null;
    }

    private InlineKeyboardButton[] getButtons() {
        return new InlineKeyboardButton[] {
                new InlineKeyboardButton("Use").callbackData("/use"),
                new InlineKeyboardButton("Player Stats").callbackData("/stats"),
        };
    }
}
