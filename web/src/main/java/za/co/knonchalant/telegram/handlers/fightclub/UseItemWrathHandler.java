package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.business.AttackHandler;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.List;

import static za.co.knonchalant.telegram.handlers.fightclub.game.DeathCheckHandler.doDeathCheck;

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

        List<String> message = AttackHandler.doAttack(fighterDAO, update.getUser().getFirstName(), item, fighter);
        message.forEach(m -> sendMessage(update, m));

        doDeathCheck(update, fighterDAO, fighter, update.getUser().getFirstName());

        return pendingResponse.complete();
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
