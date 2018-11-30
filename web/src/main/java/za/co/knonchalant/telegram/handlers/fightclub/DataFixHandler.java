package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.details.RegisterDetails;

import java.util.Collections;
import java.util.List;

public class DataFixHandler extends FightClubMessageHandler implements IResponseMessageHandler<RegisterDetails> {
    public DataFixHandler(String botName, IBotAPI bot) {
        super(botName, "register", bot, true);
    }

    @Override
    public String getDescription() {
        return "Do data fixup stuff";
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        int changesMade = 0;

        FighterDAO fighterDAO = FighterDAO.get();
        List<Item> allItems = fighterDAO.findAllItems();
        for (Item item : allItems) {
            // This handles items which were created prior to the introduction of the damage type
            // by giving them a default value as they're used. Once all items have been fixed, we
            // can nuke this message
            if (item.getDamageType() == null) {
                item.setDamageType(EDamageType.ATTACK); // data-fix the initial set of items on the fly
                fighterDAO.persistItem(item);
                changesMade++;
            }
        }


        sendMessage(update, "All done; made " + changesMade + " " + StringPrettifier.pluralize(changesMade, "change"));
        return null;
    }

    @Override
    public List<IResponseHandler<RegisterDetails>> getHandlers() {
        return Collections.singletonList(new RegisterResponseHandler());
    }
}
