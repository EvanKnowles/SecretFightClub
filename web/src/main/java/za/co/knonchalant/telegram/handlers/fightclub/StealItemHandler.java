package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.BaseDetail;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.details.StealDetails;

import java.util.Collections;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class StealItemHandler extends BaseMessageHandler implements IResponseMessageHandler<StealDetails> {
    public static final String COMMAND = "steal";

    public StealItemHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot, true);
    }

    @Override
    public String getDescription() {
        return "Steal an item from another fighter. Bad form!";
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

        if (fighter.isDead()) {
            sendMessage(update, "Lie down, " + fighter.getName() + " - you're dead.");
            return null;
        }

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(getButtons(fightersInRoom));
        getBot().sendMessage(update.getChatId(), "From whom shall you pilfer?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        return new PendingResponse(update.getChatId(), update.getUser().getId(), "use", new BaseDetail());
    }

    private InlineKeyboardButton[] getButtons(List<Fighter> itemsCarriedBy) {
        InlineKeyboardButton[] array = new InlineKeyboardButton[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            Fighter item = itemsCarriedBy.get(i);
            array[i] = new InlineKeyboardButton(item.getName()).callbackData(String.valueOf(item.getId()));
        }
        return array;
    }

    @Override
    public List<IResponseHandler<StealDetails>> getHandlers() {
        return Collections.singletonList(new StealFromFighterHandler());
    }
}
