package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

public class ItemUsageResponseHandler extends BaseMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 2;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {

        String text = update.getText();

        if (text.isEmpty()) {
            text = null;
        }

        Item item = new Item(state.getName(), state.getDamage(), state.getDamageType(), text, update.getChatId());
        FighterDAO.get().persistItem(item);

        sendMessage(update, "I guess we're done here.");
        return pendingResponse.complete();
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
