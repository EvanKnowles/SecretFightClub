package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.List;

public class UseItemSelectionHandler2 extends UseItemSelectionHandler implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    protected Item determineItemToUse(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        StringBuilder debug = new StringBuilder();

        String desiredItemDescription = update.getText();
        if (desiredItemDescription == null) {
            desiredItemDescription = "";
        } else {
            desiredItemDescription = desiredItemDescription.trim();
        }

        debug.append("Looking for: '" ).append(desiredItemDescription).append("'\n");

        List<Item> carried = fighterDAO.getItemsCarriedBy(fighter.getId());
        for (Item item : carried) {
            String thisItemDescription = UseItemHandler2.makeItemButtonText(item);
            if (thisItemDescription.trim().equals(desiredItemDescription)) {
                return item;
            }
            debug.append("Not: '").append(thisItemDescription).append("'\n");

        }
        debug.append("DOH! Couldn't find your item...");
        sendMessage(update, debug.toString());
        return null;
    }

    @Override
    protected void promptForAttackItemVictim(IUpdate update, ItemDetails state, int itemID, FighterDAO fighterDAO, Item item) {
        state.setItemId(itemID);
        List<Fighter> fighters = findLivingOpponents(update, fighterDAO);

        String[][] buttons = VerticalButtonBuilder.createVerticalButtons(getButtons(fighters));
        ReplyKeyboardMarkup inlineKeyboardMarkup = new ReplyKeyboardMarkup(buttons, true, true, true);
        getBot().sendMessage(update.getChatId(), "Upon who shall ye inflict your " + item.getName() + "?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);
    }

    private String[] getButtons(List<Fighter> itemsCarriedBy) {
        String[] array = new String[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            Fighter item = itemsCarriedBy.get(i);
            array[i] = item.getName();
        }
        return array;
    }

    @Override
    public String getIdentifier() {
        return "use";
    }
}
