package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.DeadFighterCannotFightException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.FighterDoesNotExistException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

abstract class ActiveFighterMessageHandler extends BaseMessageHandler
{
  ActiveFighterMessageHandler(String botName, String command, IBotAPI bot, boolean noargs)
  {
    super(botName, command, bot, noargs);
  }

  void verifyFighter(Fighter fighter) throws HandlerActionNotAllowedException
  {
    if (fighter == null) {
      throw new FighterDoesNotExistException();
    }

    if (fighter.isDead()) {
      throw new DeadFighterCannotFightException(fighter);
    }
  }
}
