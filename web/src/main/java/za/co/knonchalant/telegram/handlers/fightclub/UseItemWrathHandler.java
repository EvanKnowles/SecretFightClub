package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.telegram.handlers.fightclub.game.AttackCommand;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;
import za.co.knonchalant.telegram.handlers.fightclub.game.CommandExecutor;
import za.co.knonchalant.telegram.handlers.fightclub.game.DeathCheckCommand;

import java.util.List;

public class UseItemWrathHandler extends FightClubMessage implements IResponseHandler<ItemDetails> {
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

        List<String> message = AttackCommand.doAttack(fighterDAO, update.getUser().getFirstName(), item, fighter);
        message.forEach(m -> sendMessage(update, m));

        DeathCheckCommand c = new DeathCheckCommand(update, fighterDAO, fighter, update.getUser().getFirstName());
        CommandExecutor.execute(c, this);

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
