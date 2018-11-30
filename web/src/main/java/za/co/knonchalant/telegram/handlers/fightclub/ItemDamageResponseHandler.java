package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.Arrays;

public class ItemDamageResponseHandler extends BaseMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {

        String text = update.getText();
        double v;
        try {
            v = Double.parseDouble(text);
        }
        catch (NumberFormatException ex) {
            sendMessage(update, "You done buggered up, that's not a real number. Try again.");
            return pendingResponse.retry();
        }

        if (v > 100) {
            sendMessage(update, "I'm just gonna cap that at 100, you monster.");
            v = 100;
        } else if (v < -100) {
            sendMessage(update, "I'm just gonna cap that at -100, you saint.");
            v = -100;
        }
        state.setDamage(v);

        StringBuilder b = new StringBuilder("Item damage type? Use one of these:\n");
        for (EDamageType d : EDamageType.values()) {
            b.append(" - *").append(d.getName()).append("*\n");
            b.append("       ").append(d.getDescription());
        }
        sendMessage(update, b.toString());

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
