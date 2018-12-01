package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

public class ItemDamageTypeResponseHandler extends BaseMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 1;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {

        String text = update.getText();

        if (text.isEmpty()) {
            text = null;
        }

        EDamageType v = EDamageType.fromName(text);
        if (v == null) {
            sendMessage(update, "You done buggered up, that's not an option. Try again.");
            return pendingResponse.retry();
        }
        state.setDamageType(v);

        sendMessage(update, "Specialized attack text? Use {{a}} and {{d}}, like '{{a}} tweaks {{d}}'s nipple'. Just send a blank message if you don't feel like it.");
        return pendingResponse.handled();
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
