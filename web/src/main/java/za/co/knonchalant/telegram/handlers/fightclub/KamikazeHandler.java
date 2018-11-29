package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import static za.co.knonchalant.telegram.handlers.fightclub.UseItemWrathHandler.checkForDeathAndConsequences;

/**
 * Created by evan on 2016/04/08.
 */
public class KamikazeHandler extends ActiveFighterMessageHandler {
    private static final String KAMIKAZE_ICON = "\uD83E\uDD2F";

    public KamikazeHandler(String botName, IBotAPI bot) {
        super(botName, "kamikaze", bot, true);
    }

    @Override
    public String getDescription() {
        return "Goodbye, cruel world";
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        FighterDAO fighterDAO = FighterDAO.get();

        long userId = update.getUser().getId();
        Fighter fighter = fighterDAO.getFighter(userId, update.getChatId());
        try {
            verifyFighter(fighter);
        } catch (HandlerActionNotAllowedException e) {
            sendMessage(update, e.getMessage());
            return null;
        }

        fighter.damage(fighter.getHealth());
        fighterDAO.persistFighter(fighter);
        sendMessage(update, KAMIKAZE_ICON + fighter.getName());
        checkForDeathAndConsequences(getBot(), update, fighterDAO, fighter, update.getUser().getFirstName());
        return null;
    }

}
