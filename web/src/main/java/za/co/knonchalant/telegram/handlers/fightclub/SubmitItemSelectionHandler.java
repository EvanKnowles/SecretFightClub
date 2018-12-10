package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.details.SubmitDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.SubmitItemCommand;

import java.util.List;

public class SubmitItemSelectionHandler extends FightClubMessage implements IResponseHandler<SubmitDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, SubmitDetails state, PendingResponse pendingResponse) {
        // we can't have gotten here if there wasn't a fighter, right?
        Item item = determineItemToUse(update, FighterDAO.get());
        if (item == null) {
            sendMessage(update, update.getUser().getFirstName() + ", you don't have that item");
            return pendingResponse.complete();
        }

        execute(new SubmitItemCommand(update, item));

        return pendingResponse.complete();
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
    public Class<SubmitDetails> getDetailsClass() {
        return SubmitDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "submit";
    }
}
