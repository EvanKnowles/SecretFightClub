package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.RestartHandler;

import java.util.ArrayList;
import java.util.List;

public class DeathCheckCommand extends FightClubCommand implements MessageOutputCommand {

    private final IUpdate update;
    private final FighterDAO fighterDAO;
    private final Fighter victim;
    private final String damageCauser;
    private final List<String> messages;

    public DeathCheckCommand(IUpdate update, FighterDAO fighterDAO, Fighter victim, String damageCauser) {
        this.update = update;
        this.fighterDAO = fighterDAO;
        this.victim = victim;
        this.damageCauser = damageCauser;

        messages = new ArrayList<>();
    }

    @Override
    void execute() {
        if (victim.getHealth() <= 0) {
            if (damageCauser.equalsIgnoreCase(victim.getName())) {
                messages.add("It's all too much for " + update.getUser().getFirstName() + "; goodbye, cruel world.");
            } else {
                messages.add("Like OMG! " + damageCauser + " killed " + victim.getName() + "!");
            }
            List<String> endGameMessages = checkForEndGame(fighterDAO, update.getChatId(), messages);
            messages.addAll(endGameMessages);
        }
    }

    private static List<String> checkForEndGame(FighterDAO fighterDAO, long chatId, List<String> messages) {
        List<Fighter> liveFighters = fighterDAO.findAliveFightersInRoom(chatId);
        if (liveFighters.size() == 1) {
            Fighter fighter = liveFighters.get(0);
            messages.add("THAT'S A WRAP LADIES AND GENTS! " + fighter.getName() + " wins!");
            fighter.win();
            messages.add("1 point for " + fighter.getName() + ", who now has " + fighter.getWins() + " points");
            fighterDAO.persistFighter(fighter);

            RestartHandler.scheduleRestart(chatId);
        } else if (liveFighters.isEmpty()) {
            messages.add("Not to alarm anyone, but somehow you're all dead. That's odd. Try not to muck it up again eh?");
            RestartHandler.scheduleRestart(chatId);
        }
        return messages;
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }

    @Override
    public IUpdate getUpdate() {
        return update;
    }
}
