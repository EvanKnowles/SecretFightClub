package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.FighterDoesNotExistException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

abstract class ValidFighterMessageHandler extends FightClubMessageHandler {
    ValidFighterMessageHandler(String botName, String command, IBotAPI bot, boolean noargs) {
        super(botName, command, bot, noargs);

    }

    ValidFighterMessageHandler(String botName, String command, IBotAPI bot) {
        super(botName, command, bot, false);
    }

    @Override
    public void verifyFighter(FighterDAO fighterDAO, Fighter fighter, IUpdate chatId) throws HandlerActionNotAllowedException {
        if (fighter == null) {
            throw new FighterDoesNotExistException();
        }
    }
}
