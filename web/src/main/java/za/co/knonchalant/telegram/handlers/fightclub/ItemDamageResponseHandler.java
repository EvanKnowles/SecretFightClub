package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
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


            sendMessage(update, "Specialized attack text? Use {{a}} and {{d}}, like '{{a}} tweaks {{d}}'s nipple'. Just send a blank message if you don't feel like it.");

            return pendingResponse.handled();
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
