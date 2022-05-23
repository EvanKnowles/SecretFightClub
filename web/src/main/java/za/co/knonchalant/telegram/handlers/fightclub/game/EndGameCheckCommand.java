package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.RestartHandler;

import java.util.List;

public class EndGameCheckCommand extends FightClubCommand {
    private final IUpdate update;
    private final FighterDAO fighterDAO;

    public EndGameCheckCommand(IUpdate update, FighterDAO fighterDAO) {
        this.update = update;
        this.fighterDAO = fighterDAO;
    }

    @Override
    void execute(MessageSender handler) {
        Fighter fighterByUserId = fighterDAO.getFighterByUserId(update.getUser().getId());
        List<Fighter> liveFighters = fighterByUserId.getClub().getActiveFighters();

        if (liveFighters.size() == 1) {
            Fighter fighter = liveFighters.get(0);
            handler.sendMessage(update, "THAT'S A WRAP LADIES AND GENTS! " + fighter.getName() + " wins!");
            fighter.win();
            handler.sendMessage(update, "1 point for " + fighter.getName() + ", who now has " + fighter.getWins() + " points");
            fighterDAO.persistFighter(fighter);

            RestartHandler.scheduleRestart(fighterByUserId.getClub());
        } else if (liveFighters.isEmpty()) {
            handler.sendMessage(update, "Not to alarm anyone, but somehow you're all dead. That's odd. Try not to muck it up again eh?");
            RestartHandler.scheduleRestart(fighterByUserId.getClub());
        }
    }

}
