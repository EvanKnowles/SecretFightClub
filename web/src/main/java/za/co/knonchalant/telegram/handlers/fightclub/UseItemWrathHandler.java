package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.List;
import java.util.stream.Collectors;

public class UseItemWrathHandler extends BaseMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 1;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {
        int fighterId = Integer.parseInt(update.getText());
        FighterDAO fighterDAO = FighterDAO.get();
        Fighter fighter = fighterDAO.getFighter(fighterId);

        if (fighter == null) {
            sendMessage(update, update.getUser().getFirstName() + ", ask for a fighter ID, you give me " + update.getText() + " - probably tapping on an old message.");
            return pendingResponse.complete();
        }

        Item item = fighterDAO.findItem(state.getItemId());

        if (item == null) {
            sendMessage(update, "You aren't carrying that.");
            return pendingResponse.complete();
        }

        fighter.damage(item.getDamage());
        fighterDAO.persistFighter(fighter);
        fighterDAO.remove(item);

        if (item.getDamage() > 0) {
            if (item.getAttackText() == null) {
                sendMessage(update, update.getUser().getFirstName() + " uses " + StringPrettifier.prettify(item.getName()) + " on " + fighter.getName());
            } else {
                sendMessage(update, item.format(update.getUser().getFirstName(), fighter.getName()));
            }
            sendMessage(update, update.getUser().getFirstName() + " damages " + fighter.getName() + " for " + item.getDamage() + " points. " + describe(item.getDamage(), fighter.getHealth()));
        } else {
            if (item.getAttackText() == null) {
                sendMessage(update, update.getUser().getFirstName() + " uses " + StringPrettifier.prettify(item.getName()) + " and heals " + Math.abs(item.getDamage()) + " points on " + fighter.getName());
            } else {
                sendMessage(update, item.format(update.getUser().getFirstName(), fighter.getName()));
            }
        }

        checkForDeathAndConsequences(getBot(), update, fighterDAO, fighter, update.getUser().getFirstName());

        return pendingResponse.complete();
    }

    /**
     * This will most definitely be moved somewhere better, as soon as we've worked out how we're handling
     * common functionality in this system.
     * @param bot
     * @param update
     * @param fighterDAO
     * @param fighter
     * @param damageCauser
     */
    public static void checkForDeathAndConsequences(IBotAPI bot, IUpdate update, FighterDAO fighterDAO, Fighter fighter, String damageCauser) {
        if (fighter.getHealth() <= 0) {
            bot.sendMessage(update, "Like OMG! " + damageCauser + " killed " + fighter.getName());
            checkForEndGame(bot, fighterDAO, update);
        }
    }

    /**
     * Describe just how mean it was to take X points from someone who (now) has Y health.
     */
    private String describe(double damage, double health) {
        double previousHealth = health + damage;

        double ratio = damage / previousHealth;

        if (ratio < 0.1) {
            return "That's a tiny bit not so nice.";
        }

        if (ratio < 0.2) {
            return "That'll make high-fives a little awkward going forward.";
        }

        if (ratio < 0.4) {
            return "Kinda mean.";
        }

        if (ratio < 0.8) {
            return "Inigo Montoya would have something cutting to say about damage like that.";
        }

        return "Wowser. Someone owes someone else a drink and possibly a hug.";
    }


    private static void checkForEndGame(IBotAPI bot, FighterDAO fighterDAO, IUpdate update) {
        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        List<Fighter> collect = fightersInRoom.stream().filter(fighter -> !fighter.isDead()).collect(Collectors.toList());
        if (collect.size() == 1) {
            Fighter fighter = collect.get(0);
            bot.sendMessage(update, "THAT'S A WRAP LADIES AND GENTS! " + fighter.getName() + " wins!");
            fighter.win();
            fighterDAO.persistFighter(fighter);

            restartGame(bot, fighterDAO, fightersInRoom, update);
        } else if (collect.isEmpty()) {
            bot.sendMessage(update, "Not to alarm anyone, but somehow you're all dead. That's odd. Try not to muck it up again eh?");
            restartGame(bot, fighterDAO, fightersInRoom, update);
        }
    }

    private static void restartGame(IBotAPI bot, FighterDAO fighterDAO, List<Fighter> fightersInRoom, IUpdate update) {
        fightersInRoom.stream().filter(Fighter::isDead).forEach(deadFighter -> {
            deadFighter.revive();
            fighterDAO.persistFighter(deadFighter);
            bot.sendMessage(update, deadFighter.getName() + " returns to life!");
        });
    }

    @Override
    public Class<ItemDetails> getDetailsClass() {
        return ItemDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "use";
    }
}
