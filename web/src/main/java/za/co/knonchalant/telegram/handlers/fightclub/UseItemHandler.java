package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class UseItemHandler extends BaseMessageHandler implements IResponseMessageHandler<ItemDetails> {
    public static final String COMMAND = "use";

    public UseItemHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot, true);
    }

    @Override
    public String getDescription() {
        return "Use one of your treasures.";
    }


    @Override
    public PendingResponse handle(IUpdate update) {
        FighterDAO fighterDAO = FighterDAO.get();

        long userId = update.getUser().getId();
        Fighter fighter = fighterDAO.getFighter(userId, update.getChatId());
        if (fighter == null) {
            sendMessage(update, "Uh, you don't exist. Go away ghosty.");
            return null;
        }

        if (fighter.getHealth() < 0) {
            sendMessage(update, "Lie down - you're dead.");
            return null;
        }

        List<Item> itemsCarriedBy = fighterDAO.getItemsCarriedBy(fighter.getId());
        if (itemsCarriedBy.isEmpty()) {
            sendMessage(update, "You're not carrying anything. You could roll for something.");
            return null;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(getButtons(itemsCarriedBy));
        getBot().sendMessage(update.getChatId(), "What do you want to use?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        return new PendingResponse(update.getChatId(), update.getUser().getId(), "use", new ItemDetails());
    }

    private InlineKeyboardButton[] getButtons(List<Item> itemsCarriedBy) {
        InlineKeyboardButton[] array = new InlineKeyboardButton[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            Item item = itemsCarriedBy.get(i);
            array[i] = new InlineKeyboardButton(item.getName()).callbackData(String.valueOf(item.getId()));
        }
        return array;
    }

    @Override
    public List<IResponseHandler<ItemDetails>> getHandlers() {
        return Arrays.asList(new UseItemSelectionHandler(), new UseItemWrathHandler());
    }
}
