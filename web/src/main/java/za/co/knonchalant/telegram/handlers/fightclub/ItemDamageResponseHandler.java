package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

public class ItemDamageResponseHandler extends BaseMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {

        String text = update.getText();
        try {
            double v = Double.parseDouble(text);
            if (v > 100) {
                sendMessage(update, "I'm just gonna cap that at 100, you monster.");
                v = 100;
            } else if (v < -100) {
                sendMessage(update, "I'm just gonna cap that at -100, you saint.");
                v = -100;
            }
            state.setDamage(v);

            Item item = new Item(state.getName(), state.getDamage());
            FighterDAO.get().persistItem(item);
            sendMessage(update, "I guess we're done here.");

            return pendingResponse.complete();
        } catch (NumberFormatException ex) {
            sendMessage(update, "You done buggered up, that's not a real number");
            return pendingResponse.retry();
        }
    }

    @Override
    public Class<ItemDetails> getDetailsClass() {
        return ItemDetails.class;
    }

    @Override
    public String getIdentifier() {
        return NewItemHandler.COMMAND;
    }
}
