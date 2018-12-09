package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EApprovalStatus;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.SubmitDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.SubmitItemCommand;

import java.util.List;

public class ReviewItemSelectionHandler extends FightClubMessage implements IResponseHandler<SubmitDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, SubmitDetails state, PendingResponse pendingResponse) {
        // we can't have gotten here if there wasn't a fighter, right?
        Item item = determineItemToUse(update, FighterDAO.get());
        if (item == null) {
            sendMessage(update, update.getUser().getFirstName() + ", we don't have that item awaiting review");
            return pendingResponse.complete();
        }

        String text = StringPrettifier.itemIcon(item) + " " + item.getName() + "\nDamage: " + item.getDamage() + "\n" + item.getAttackText();
        String[][] buttons = VerticalButtonBuilder.createVerticalButtons(new String[]{EApprovalStatus.APPROVED.getText(), EApprovalStatus.DENIED.getText()});
        ReplyKeyboardMarkup inlineKeyboardMarkup = new ReplyKeyboardMarkup(buttons, true, true, true);

        getBot().sendMessage(update.getChatId(), text, ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        state.setItemId(item.getId());

        return pendingResponse.handled();
    }

    private Item determineItemToUse(IUpdate update, FighterDAO fighterDAO) {
        StringBuilder debug = new StringBuilder();

        String desiredItemDescription = update.getText();
        if (desiredItemDescription == null) {
            desiredItemDescription = "";
        } else {
            desiredItemDescription = desiredItemDescription.trim();
        }

        debug.append("Looking for: '" ).append(desiredItemDescription).append("'\n");

        List<Item> carried = fighterDAO.getAllUncarriedItemsFrom(update.getChatId());
        for (Item item : carried) {
            if (item.getName().trim().equals(desiredItemDescription)) {
                return item;
            }

            debug.append("Not: '").append(item.getName()).append("'\n");

        }
        debug.append("DOH! Couldn't find your item...");
        sendMessage(update, debug.toString());
        return null;
    }


    @Override
    public Class<SubmitDetails> getDetailsClass() {
        return SubmitDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "review";
    }
}
