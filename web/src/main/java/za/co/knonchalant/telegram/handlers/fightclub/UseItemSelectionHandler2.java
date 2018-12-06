package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
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
        debug.append("Looking for: '" ).append(desiredItemDescription).append("'\n");

        List<Item> carried = fighterDAO.getItemsCarriedBy(fighter.getId());
        for (Item item : carried) {
            String thisItemDescription = UseItemHandler.makeItemButtonText(item);
            if (thisItemDescription.equals(desiredItemDescription)) {
                return item;
            }
            debug.append("Not: '").append(thisItemDescription).append("'\n");

        }
        debug.append("DOH! Couldn't find your item...");
        sendMessage(update, debug.toString());
        return null;
    }

    @Override
    public String getIdentifier() {
        return "use2";
    }
}
