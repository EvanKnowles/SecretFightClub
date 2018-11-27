package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.*;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class NewItemHandler extends BaseMessageHandler implements IResponseMessageHandler<ItemDetails> {
    public static final String COMMAND = "newitem";

    public NewItemHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot);
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        String text = getKeywords(update.getText(), COMMAND);
        sendMessage(update, "Creating item named: " + text);
        sendMessage(update, "Damage?");

        return new PendingResponse(update.getChatId(), update.getUser().getId(), COMMAND, new ItemDetails(text));
    }

    @Override
    public List<IResponseHandler<ItemDetails>> getHandlers() {
        return Arrays.asList(new ItemDamageResponseHandler(), new ItemUsageResponseHandler());
    }
}
