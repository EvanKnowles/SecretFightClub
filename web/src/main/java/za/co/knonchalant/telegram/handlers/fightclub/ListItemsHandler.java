package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class ListItemsHandler extends BaseMessageHandler implements IResponseMessageHandler<ItemDetails> {

    public ListItemsHandler(String botName, IBotAPI bot) {
        super(botName, "listitems", bot);
    }

    @Override
    public String getDescription() {
        return "See a list of all items";
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        FighterDAO fighterDAO = FighterDAO.get();
        List<Item> items = fighterDAO.findAllItems();
        StringBuilder b = new StringBuilder("*Behold the inventory!*\n");

        for (Item item : items)
        {
            b.append(" - ");
            b.append(item.getName());
            b.append(" (").append(StringPrettifier.itemIcon(item)).append(item.getDamage()).append(")\n");
        }
        sendMessage(update, b.toString());

        return null;
    }

    @Override
    public List<IResponseHandler<ItemDetails>> getHandlers() {
        return Arrays.asList(new ItemDamageResponseHandler(), new ItemUsageResponseHandler());
    }
}
