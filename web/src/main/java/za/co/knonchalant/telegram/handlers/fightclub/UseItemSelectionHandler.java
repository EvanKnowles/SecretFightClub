package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.AttackHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UseItemSelectionHandler extends BaseMessage implements IResponseHandler<ItemDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, ItemDetails state, PendingResponse pendingResponse) {
        int itemID = Integer.parseInt(update.getText());
        FighterDAO fighterDAO = FighterDAO.get();
        Fighter fighter = fighterDAO.getFighter(update.getUser().getId(), update.getChatId());

        // we can't have gotten here if there wasn't a fighter, right?
        Item item = fighterDAO.findItem(itemID);
        if (item == null || item.getFighterId() != fighter.getId()) {
            sendMessage(update, fighter.getName() + ", you don't have " + (item == null ? "that item" : StringPrettifier.prettify(item.getName())));
            return pendingResponse.complete();
        }

        // Healing items:
        if (item.getDamage() < 0) {
            useHealingItem(update, fighterDAO, fighter, item);
            return pendingResponse.complete();
        }

        // Damaging items:
        if (item.getDamageType() == EDamageType.ATTACK) {
            promptForAttackItemVictim(update, state, itemID, fighterDAO, item);
        }

        if (item.getDamageType() == EDamageType.SPLASH_ATTACK) {
            useSplashAttackItem(update, fighterDAO, fighter, item);
            return pendingResponse.complete();
        }
        return pendingResponse.handled();
    }

    private void promptForAttackItemVictim(IUpdate update, ItemDetails state, int itemID, FighterDAO fighterDAO, Item item) {
        state.setItemId(itemID);
        List<Fighter> fighters = findLivingOpponents(update, fighterDAO);

        InlineKeyboardButton[] buttons = getButtons(fighters);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(VerticalButtonBuilder.createVerticalButtons(buttons));
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
        List<Fighter> opponents = findLivingOpponents(update, fighterDAO);
        opponents.removeIf(f -> f.getUserId() == fighter.getUserId());

        List<String> messages = AttackHandler.doAttack(fighterDAO, fighter.getName(), item, opponents.toArray(new Fighter[0]));
        messages.forEach(m -> sendMessage(update, m));
    }

    private List<Fighter> findLivingOpponents(IUpdate update, FighterDAO fighterDAO) {
        List<Fighter> fighters = fighterDAO.findAliveFightersInRoom(update.getChatId());
        fighters.sort(Comparator.comparing(Fighter::getName));
        return fighters;
    }

    private InlineKeyboardButton[] getButtons(List<Fighter> fighters) {
        InlineKeyboardButton[] array = new InlineKeyboardButton[fighters.size()];
        for (int i = 0; i < fighters.size(); i++) {
            Fighter fighter = fighters.get(i);
            array[i] = new InlineKeyboardButton(fighter.getName()).callbackData(String.valueOf(fighter.getId()));
        }
        return array;
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
