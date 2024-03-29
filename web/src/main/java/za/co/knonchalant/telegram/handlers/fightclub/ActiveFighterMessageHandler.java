package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.DeadFighterCannotFightException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.FighterDoesNotExistException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.NoGameInProgressException;

import java.util.HashMap;
import java.util.Map;

abstract class ActiveFighterMessageHandler extends FightClubMessageHandler {
    private static final Map<Long, Object> FIGHTER_LOCK = new HashMap<>();

    ActiveFighterMessageHandler(String botName, String command, IBotAPI bot, boolean noargs) {
        super(botName, command, bot, noargs);
    }

    @Override
    public void verifyFighter(FighterDAO fighterDAO, Fighter fighter, IUpdate update) throws HandlerActionNotAllowedException {
        if (fighter == null) {
            throw new FighterDoesNotExistException();
        }

        if (fighter.isDead()) {
            throw new DeadFighterCannotFightException(fighter);
        }

        if (fighterDAO.findAliveFightersInRoom(update.getChatId()).size() <= 1) {
            throw new NoGameInProgressException(fighter);
        }
    }


    private Object getFighterLock(IUpdate update) {
        // sync-ing on just the user is a little odd yes, given that we're user/chat based, but they really shouldn't be sending
        // simultaneous calls on more than one chat at a time
        long userId = update.getUser().getId();

        if (!FIGHTER_LOCK.containsKey(userId)) {
            FIGHTER_LOCK.put(userId, new Object());
        }

        return FIGHTER_LOCK.get(userId);
    }
}
