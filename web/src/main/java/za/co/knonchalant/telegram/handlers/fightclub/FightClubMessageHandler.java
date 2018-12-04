package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

public abstract class FightClubMessageHandler extends BaseMessageHandler {
    FightClubMessageHandler(String botName, String command, IBotAPI bot, boolean noargs) {
        super(botName, command, bot, noargs);
    }

    FightClubMessageHandler(String botName, String command, IBotAPI bot) {
        super(botName, command, bot, false);
    }

    public abstract void verifyFighter(FighterDAO fighterDAO, Fighter fighter, long chatId) throws HandlerActionNotAllowedException;

    public abstract PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) throws HandlerActionNotAllowedException;

        @Override
    public final PendingResponse handle(IUpdate update) {
//        synchronized (getFighterLock(update)) {
        FighterDAO fighterDAO = FighterDAO.get();
        long userId = update.getUser().getId();
        Fighter fighter = fighterDAO.getFighter(userId, update.getChatId());

        try {
            verifyFighter(fighterDAO, fighter, update.getChatId());
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
//        }
    }

    @Override
    public void sendMessage(IUpdate message, String text) {
        super.sendMessage(message, text);
    }
}
