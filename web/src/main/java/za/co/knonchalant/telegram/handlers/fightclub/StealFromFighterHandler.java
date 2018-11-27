package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.details.StealDetails;

import java.util.List;

public class StealFromFighterHandler extends BaseMessage implements IResponseHandler<StealDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, StealDetails state, PendingResponse pendingResponse) {
        long fighterId = Long.parseLong(update.getText());

        FighterDAO fighterDAO = FighterDAO.get();

        List<Item> itemsCarriedBy = fighterDAO.getItemsCarriedBy(fighterId);
        if (itemsCarriedBy == null || itemsCarriedBy.isEmpty()) {
            sendMessage(update, "Robbing from the poor are we " + update.getUser().getFirstName() + "?");
            return pendingResponse.complete();
        }

        Fighter stealingFighter = fighterDAO.getFighter(update.getUser().getId(), update.getChatId());
        Fighter victimFighter = fighterDAO.getFighter(fighterId);

        int index = (int) Math.floor(Math.random() * itemsCarriedBy.size());
        Item item = itemsCarriedBy.get(index);
        item.setFighterId(stealingFighter.getId());

        fighterDAO.persistItem(item);

        sendMessage(update, String.format("%s steals a %s from %s - what is this world coming to?", update.getUser().getFirstName(), item.getName(), victimFighter.getName()));

        return pendingResponse.complete();
    }

    @Override
    public Class<StealDetails> getDetailsClass() {
        return StealDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "steal";
    }
}
