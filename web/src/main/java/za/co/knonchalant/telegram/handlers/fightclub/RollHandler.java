package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class RollHandler extends BaseMessageHandler {
    public RollHandler(String botName, IBotAPI bot) {
        super(botName, "roll", bot, true);
    }

    @Override
    public String getDescription() {
        return "Game of chance - you may get a new item.";
    }


    @Override
    public PendingResponse handle(IUpdate update) {
        FighterDAO fighterDAO = FighterDAO.get();

        long userId = update.getUser().getId();
        Fighter fighter = fighterDAO.getFighter(userId, update.getChatId());
        if (fighter == null) {
            sendMessage(update, "Uh, you don't exist.");
            return null;
        }

        if (fighter.getHealth() < 0) {
            sendMessage(update, "Lie down - you're dead.");
            return null;
        }

        List<Item> itemsCarriedBy = fighterDAO.getItemsCarriedBy(fighter.getId());

        if (itemsCarriedBy.size() > 3) {
            sendMessage(update, "You have stuff " + fighter.getName() + ", stop being greedy.");
            return null;
        }

        List<Item> items = fighterDAO.getAllUncarriedItems();
        double total = items.stream().mapToDouble(this::swapProbability).sum();
        double pick = Math.random() * total;

        double running = 0.0;
        for (Item item : items) {
            double abs = swapProbability(item);
            if (pick > running && pick < running + abs) {
                fighterDAO.give(item, fighter);
                sendMessage(update, fighter.getName() + " gets a " + item.getNameWithPrefix() + "!");
                return null;
            } else {
                running += abs;
            }
        }

        return null;
    }

    private double swapProbability(Item i) {
        return Math.max(99 - Math.abs(i.getDamage()), 1);
    }
}
