package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class RollHandler extends ActiveFighterMessageHandler {
    public RollHandler(String botName, IBotAPI bot) {
        super(botName, "roll", bot, true);
    }

    @Override
    public String getDescription() {
        return "Game of chance - you may get a new item.";
    }

    @Override
    public void verifyFighter(FighterDAO fighterDAO, Fighter fighter, IUpdate update) throws HandlerActionNotAllowedException {
        super.verifyFighter(fighterDAO, fighter, update);

        List<Item> itemsCarriedBy = fighterDAO.getItemsCarriedBy(fighter.getId());
        if (itemsCarriedBy.size() > 3) {
            throw new HandlerActionNotAllowedException("You have stuff " + fighter.getName() + ", stop being greedy.");
        }
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        List<Item> items = fighterDAO.getAllUncarriedItemsFor(update.getChatId());
        double total = getTotalProbability(items);
        double pick = Math.random() * total;

        double running = 0.0;
        for (Item item : items) {
            double abs = swapProbability(item);
            if (pick > running && pick < running + abs) {
                fighterDAO.give(item, fighter);
                sendMessage(update, StringPrettifier.describePlayer(fighter, fighterDAO) + " gets " + StringPrettifier.itemIcon(item) + " " + StringPrettifier.prettify(item.getName()) + "!");
                return null;
            }
            running += abs;
        }

        return null;
    }

    public static double getTotalProbability(List<Item> items) {
        double sum = 0.0;
        for (Item item : items)
        {
            double v = swapProbability(item);
            sum += v;
        }
        return sum;
    }

    public static double swapProbability(Item i) {
        return Math.max(99 - Math.abs(i.getDamage()), 1);
    }
}
