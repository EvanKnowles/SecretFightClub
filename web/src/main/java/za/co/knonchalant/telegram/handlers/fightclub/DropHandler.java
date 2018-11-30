package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;

import java.util.List;

public class DropHandler extends ActiveFighterMessageHandler {
    public DropHandler(String botName, IBotAPI bot) {
        super(botName, "drop", bot, true);
    }

    @Override
    public String getDescription() {
        return "Spring cleaning! Get rid of the junk you've been holding on to";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        List<Item> items = fighterDAO.getItemsCarriedBy(fighter.getId());
        StringBuilder b = new StringBuilder("Spring cleaning!\n");
        b.append(fighter.getName()).append(" drops a bunch of stuff. Yes, just there on the floor.\n");
        b.append("(and no, you can't have it)\n");
        for (Item item : items)
        {
            b.append(" * ").append(StringPrettifier.prettify(item.getName())).append("\n");
            fighterDAO.remove(item);
        }
        sendMessage(update, b.toString());
        return null;
    }

}
