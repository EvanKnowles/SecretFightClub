package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.CommandExecutor;
import za.co.knonchalant.telegram.handlers.fightclub.game.FightClubCommand;
import za.co.knonchalant.telegram.handlers.fightclub.game.MessageSender;
import za.co.knonchalant.telegram.handlers.fightclub.game.UseItemCommand;

import java.util.Comparator;
import java.util.List;

public class UseItemSelectionHandler extends FightClubMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {
        FighterDAO fighterDAO = FighterDAO.get();
        Fighter fighter = fighterDAO.getFighterByUserId(update.getUser().getId());

        // we can't have gotten here if there wasn't a fighter, right?
        Item item = determineItemToUse(update, fighterDAO, fighter);
        if (item == null || item.getFighterId() != fighter.getId()) {
            sendMessage(update, StringPrettifier.describePlayer(fighter, fighterDAO) + ", you don't have " + (item == null ? "that item" : StringPrettifier.prettify(item.getName())));
            return pendingResponse.complete();
        }

        // Healing items:
        if (item.getDamage() < 0) {
            useHealingItem(update, fighterDAO, fighter, item);
            return pendingResponse.complete();
        }

        // Damaging items which require that we ask for a victim:
        if (item.getDamageType() == EDamageType.ATTACK || item.getDamageType() == EDamageType.SILENCE) {
            promptForAttackItemVictim(update, state, item.getId(), fighterDAO, item);
        }

        // Damage items which don't require us to ask for a victim:
        if (item.getDamageType() == EDamageType.SPLASH_ATTACK) {
            useSplashAttackItem(update, fighterDAO, fighter, item);
            return pendingResponse.complete();
        }
        if (item.getDamageType() == EDamageType.ATTACK_ALL) {
            useAttackAllItem(update, fighterDAO, fighter, item);
            return pendingResponse.complete();
        }

        return pendingResponse.handled();
    }

    private Item determineItemToUse(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        StringBuilder debug = new StringBuilder();

        String desiredItemDescription = update.getText();
        if (desiredItemDescription == null) {
            desiredItemDescription = "";
        } else {
            desiredItemDescription = desiredItemDescription.trim();
        }

        debug.append("Looking for: '" ).append(desiredItemDescription).append("'\n");

        List<Item> carried = fighterDAO.getItemsCarriedBy(fighter.getId());
        for (Item item : carried) {
            String thisItemDescription = UseItemHandler.makeItemButtonText(item);
            if (thisItemDescription.trim().equals(desiredItemDescription)) {
                return item;
            }
            debug.append("Not: '").append(thisItemDescription).append("'\n");

        }
        debug.append("DOH! Couldn't find your item...");
        sendMessage(update, debug.toString());
        return null;
    }

    private void promptForAttackItemVictim(IUpdate update, ItemDetails state, int itemID, FighterDAO fighterDAO, Item item) {
        state.setItemId(itemID);
        List<Fighter> fighters = findLivingOpponents(update, fighterDAO);

        String[][] buttons = VerticalButtonBuilder.createVerticalButtons(getButtons(fighters));
        ReplyKeyboardMarkup inlineKeyboardMarkup = new ReplyKeyboardMarkup(buttons, true, true, true);
        getBot().sendMessage(update.getChatId(), "Upon who shall ye inflict your " + item.getName() + "?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);
    }

    private void useHealingItem(IUpdate update, FighterDAO fighterDAO, Fighter fighter, Item item) {
        fighter.damage(item.getDamage());
        fighterDAO.persistFighter(fighter);
        fighterDAO.remove(item);
        String userName = update.getUser().getFirstName();
        if (item.getAttackText() == null) {
            sendMessage(update, userName + " uses " + StringPrettifier.prettify(item.getName()) + " and heals " + Math.abs(item.getDamage()) + " points.");
        } else {
            sendMessage(update, item.format(userName, userName));
        }
    }

    private void useSplashAttackItem(IUpdate update, FighterDAO fighterDAO, Fighter fighter, Item item) {
        List<Fighter> opponents = findLivingOpponents(update, fighterDAO, fighter, false);
        useItem(update, fighterDAO, fighter, item, opponents);
    }

    private void useAttackAllItem(IUpdate update, FighterDAO fighterDAO, Fighter fighter, Item item) {
        List<Fighter> opponents = findLivingOpponents(update, fighterDAO, fighter, true);
        useItem(update, fighterDAO, fighter, item, opponents);
    }

    private List<Fighter> findLivingOpponents(IUpdate update, FighterDAO fighterDAO)
    {
        return findLivingOpponents(update, fighterDAO, null, false);
    }

    private List<Fighter> findLivingOpponents(IUpdate update, FighterDAO fighterDAO, Fighter attacker, boolean includeAttacker)
    {
        List<Fighter> fighters = fighterDAO.findAliveFightersInRoom(update.getChatId());

        if (!includeAttacker && attacker != null) {
            fighters.removeIf(f -> f.getUserId() == attacker.getUserId());
        }

        fighters.sort(Comparator.comparing(Fighter::getName));
        return fighters;
    }

    private String[] getButtons(List<Fighter> itemsCarriedBy) {
        String[] array = new String[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            Fighter fighter = itemsCarriedBy.get(i);
            array[i] = fighter.getName();
        }
        return array;
    }

    private void useItem(IUpdate update, FighterDAO fighterDAO, Fighter fighter, Item item, List<Fighter> opponents)
    {
        Fighter[] victims = opponents.toArray(new Fighter[0]);
        FightClubCommand c = new UseItemCommand(update, fighterDAO, fighter, item, victims);
        CommandExecutor.execute(c, MessageSender.forBot(getBot()));
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
