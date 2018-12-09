package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.AdminDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.ReviewItem;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.SubmitDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.SubmitItemCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by evan on 2016/04/08.
 */
public class ReviewItemHandler extends ValidFighterMessageHandler implements IResponseMessageHandler<SubmitDetails> {
    public static final String COMMAND = "review";

    public ReviewItemHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot, true);
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        // TODO we probably need a message handler chain for admin commands
        if (!AdminDAO.get().isAdmin(update.getUser().getId())) {
            return null;
        }

        List<ReviewItem> items =  AdminDAO.get().getPendingReviews();

        if (items.isEmpty()) {
            sendMessage(update, "Nothing there, awesome.");
            return null;
        }

        String[][] buttons = VerticalButtonBuilder.createVerticalButtons(getButtons(items));
        ReplyKeyboardMarkup inlineKeyboardMarkup = new ReplyKeyboardMarkup(buttons, true, true, true);
        getBot().sendMessage(update.getChatId(), "Upon what shall ye cast thy mighty and wise eye?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        return new PendingResponse(update.getChatId(), update.getUser().getId(), COMMAND, new SubmitDetails());
    }

    private String[] getButtons(List<ReviewItem> itemsCarriedBy) {
        String[] array = new String[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            ReviewItem item = itemsCarriedBy.get(i);
            array[i] = item.getItemName();
        }
        return array;
    }

    @Override
    public List<IResponseHandler<SubmitDetails>> getHandlers() {
        return Arrays.asList(new ReviewItemSelectionHandler(), new ReviewItemCompleteHandler());
    }
}
