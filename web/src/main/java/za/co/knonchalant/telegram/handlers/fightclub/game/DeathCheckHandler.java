package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.RestartHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeathCheckHandler {
    public static List<String> doDeathCheck(IUpdate update, FighterDAO fighterDAO, Fighter victim, String damageCauser) {
        List<String> messages = new ArrayList<>();
        if (victim.getHealth() <= 0) {
            if (damageCauser.equalsIgnoreCase(victim.getName())) {
                messages.add("It's all too much for " + update.getUser().getFirstName() + "; goodbye, cruel world");
            } else {
                messages.add("Like OMG! " + damageCauser + " killed " + victim.getName());
            }
            messages.addAll(checkForEndGame(fighterDAO, update.getChatId()));
        }
        return messages;
    }

    private static List<String> checkForEndGame(FighterDAO fighterDAO, long chatId) {
        List<String> messages = new ArrayList<>();

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(chatId);
        List<Fighter> collect = fightersInRoom.stream().filter(fighter -> !fighter.isDead()).collect(Collectors.toList());
        if (collect.size() == 1) {
            Fighter fighter = collect.get(0);
            messages.add("THAT'S A WRAP LADIES AND GENTS! " + fighter.getName() + " wins!");
            fighter.win();
            fighterDAO.persistFighter(fighter);

            RestartHandler.scheduleRestart(chatId);
        } else if (collect.isEmpty()) {
            messages.add("Not to alarm anyone, but somehow you're all dead. That's odd. Try not to muck it up again eh?");
            RestartHandler.scheduleRestart(chatId);
        }
        return messages;
    }
}
