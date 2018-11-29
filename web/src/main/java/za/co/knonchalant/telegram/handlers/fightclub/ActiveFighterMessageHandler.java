package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.DeadFighterCannotFightException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.FighterDoesNotExistException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

abstract class ActiveFighterMessageHandler extends FightclubMessageHandler
{
  ActiveFighterMessageHandler(String botName, String command, IBotAPI bot, boolean noargs)
  {
    super(botName, command, bot, noargs);
  }

  void verifyFighter(FighterDAO fighterDAO, Fighter fighter) throws HandlerActionNotAllowedException
  {
    if (fighter == null) {
      throw new FighterDoesNotExistException();
    }

    if (fighter.isDead()) {
      throw new DeadFighterCannotFightException(fighter);
    }
  }

  @Override
  public final PendingResponse handle(IUpdate update) {
    FighterDAO fighterDAO = FighterDAO.get();
    long userId = update.getUser().getId();
    Fighter fighter = fighterDAO.getFighter(userId, update.getChatId());

    try {
      verifyFighter(fighterDAO, fighter);
    } catch (HandlerActionNotAllowedException e) {
      sendMessage(update, e.getMessage());
      return null;
    }

    try {
      return handle(update, fighterDAO, fighter);
    } catch (HandlerActionNotAllowedException e) {
      sendMessage(update, e.getMessage());
      return null;
    }
  }

  protected abstract PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) throws HandlerActionNotAllowedException;
}
