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
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            sendMessage(update, "You don't have that item " + fighter.getName());
            return pendingResponse.complete();
        }

        if (item.getDamage() < 0) {
            fighter.damage(item.getDamage());
            fighterDAO.persistFighter(fighter);
            fighterDAO.remove(item);
            String userName = update.getUser().getFirstName();
            if (item.getAttackText() == null) {
                sendMessage(update, userName + " uses the " + item.getName() + " and heals " + Math.abs(item.getDamage()) + " points.");
            } else {
                sendMessage(update, item.format(userName, userName));
            }
            return pendingResponse.complete();
        }

        state.setItemId(itemID);
        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        List<Fighter> fighters = new ArrayList<>();
        for (Fighter fighter1 : fightersInRoom) {
            if (!fighter1.isDead()) {
                fighters.add(fighter1);
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(getButtons(fighters));
        getBot().sendMessage(update.getChatId(), "Upon who shall ye inflict your " + item.getName() + "?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);


        return pendingResponse.handled();
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
