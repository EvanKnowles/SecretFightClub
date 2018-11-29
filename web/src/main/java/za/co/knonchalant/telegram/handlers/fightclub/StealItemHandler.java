package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.BaseDetail;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.StealDetails;

import java.util.Collections;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class StealItemHandler extends ActiveFighterMessageHandler implements IResponseMessageHandler<StealDetails> {

    public StealItemHandler(String botName, IBotAPI bot) {
        super(botName, "steal", bot, true);
    }

    @Override
    public String getDescription() {
        return "Steal an item from another fighter. Bad form!";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        InlineKeyboardButton[] buttons = getButtons(fightersInRoom);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(VerticalButtonBuilder.createVerticalButtons(buttons));
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
