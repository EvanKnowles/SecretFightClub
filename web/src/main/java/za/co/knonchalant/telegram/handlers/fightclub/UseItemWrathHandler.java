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
                                                                                                                    if ("Evan".equalsIgnoreCase(fighter.getName())) { if (Math.random() * 10 > 3) { sendMessage(update, "I'm sorry, Evan - I can't do that."); return null; } }
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
     *
     * @param bot
     * @param update
     * @param fighterDAO
     * @param fighter
     * @param damageCauser
     */
    static void checkForDeathAndConsequences(IBotAPI bot, IUpdate update, FighterDAO fighterDAO, Fighter fighter, String damageCauser) {
        if (fighter.getHealth() <= 0) {
            if (damageCauser.equalsIgnoreCase(fighter.getName())) {
                bot.sendMessage(update, "It's all too much for " + update.getUser().getFirstName() + "; goodbye, cruel world");
            } else {
                bot.sendMessage(update, "Like OMG! " + damageCauser + " killed " + fighter.getName());
            }
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

            RestartHandler.scheduleRestart(update.getChatId());
        } else if (collect.isEmpty()) {
            bot.sendMessage(update, "Not to alarm anyone, but somehow you're all dead. That's odd. Try not to muck it up again eh?");
            RestartHandler.scheduleRestart(update.getChatId());
        }
    }

    public static void restartGame(IBotAPI bot, FighterDAO fighterDAO, List<Fighter> fightersInRoom, IUpdate update) {
        fightersInRoom.forEach(fighter -> {
            // little bit of awkward looping to support previous functionality
            // and opt-in
            if (fighter.isDead()) {
                for (Item item : fighterDAO.getItemsCarriedBy(fighter.getId())) {
                    fighterDAO.remove(item);
                }
            }

            // juuuuust to make sure
            fighter.kill();

            if (fighter.isInGame()) {
                bot.sendMessage(update, fighter.getName() + " returns to life!");
                fighter.revive();
            }

            fighterDAO.persistFighter(fighter);
        });

        RestartHandler.resetVote(update.getChatId());
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
