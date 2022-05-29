package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.SubmitDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.SubmitItemCommand;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by evan on 2016/04/08.
 */
public class SubmitItemHandler extends ValidFighterMessageHandler implements IResponseMessageHandler<SubmitDetails> {
    public static final String COMMAND = "submit";

    public SubmitItemHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot);
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        String text = getKeywords(update.getText(), COMMAND);

        if (text.trim().equalsIgnoreCase("")) {
            sendMessage(update, "Uh, you forgot to specify an item.");
            return null;
        }

        List<Item> allUncarriedItemsFor = FighterDAO.get().getAllUncarriedItemsFrom(update.getChatId());

        if (allUncarriedItemsFor.isEmpty()) {
            sendMessage(update, "Your room hasn't made any new items - you can make items with the /newitem command.");
            return null;
        }

        List<Item> collect = allUncarriedItemsFor.stream()
                .filter(item -> item.getName().toUpperCase().contains(text.toUpperCase()))
                .collect(Collectors.toList());

        if (collect.size() == 0) {
            sendMessage(update, "Yeah, I've got nothing for that. Maybe make it?");
            return null;
        }

        if (collect.size() > 5) {
            sendMessage(update, "Found more than five different items for that - could you be more specific?");
            return null;
        }

        if (collect.size() == 1) {
            Item item = collect.get(0);

            execute(new SubmitItemCommand(update, item));

            return null;
        }

        String[][] buttons = VerticalButtonBuilder.createVerticalButtons(getButtons(collect));
        ReplyKeyboardMarkup inlineKeyboardMarkup = new ReplyKeyboardMarkup(buttons, true, true, true);
        getBot().sendMessage(update.getChatId(), "Which item do you feel needs a greater audience?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        return new PendingResponse(update.getChatId(), update.getUser().getId(), COMMAND, new SubmitDetails());
    }

    private String[] getButtons(List<Item> itemsCarriedBy) {
        String[] array = new String[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            Item item = itemsCarriedBy.get(i);
            array[i] = StringPrettifier.itemIcon(item) + " " + item.getName();
        }
        return array;
    }

    @Override
    public List<IResponseHandler<SubmitDetails>> getHandlers() {
        return Collections.singletonList(new SubmitItemSelectionHandler());
    }
}
