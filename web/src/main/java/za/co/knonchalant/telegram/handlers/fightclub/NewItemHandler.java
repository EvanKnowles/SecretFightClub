package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.*;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class NewItemHandler extends ValidFighterMessageHandler implements IResponseMessageHandler<ItemDetails> {
    public static final String COMMAND = "newitem";

    public NewItemHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot);
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        String text = getKeywords(update.getText(), COMMAND);
        sendMessage(update, "Creating item named: " + text);
        sendMessage(update, "Damage?");

        return new PendingResponse(update.getChatId(), update.getUser().getId(), COMMAND, new ItemDetails(text));
    }

    @Override
    public List<IResponseHandler<ItemDetails>> getHandlers() {
        return Arrays.asList(new ItemDamageResponseHandler(), new ItemDamageTypeResponseHandler(), new ItemUsageResponseHandler());
    }
}
