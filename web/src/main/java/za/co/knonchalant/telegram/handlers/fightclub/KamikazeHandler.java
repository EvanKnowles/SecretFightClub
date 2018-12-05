package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.game.CommandExecutor;
import za.co.knonchalant.telegram.handlers.fightclub.game.DeathCheckCommand;
import za.co.knonchalant.telegram.handlers.fightclub.game.MessageSender;

/**
 * Created by evan on 2016/04/08.
 */
public class KamikazeHandler extends ActiveFighterMessageHandler {
    private static final String KAMIKAZE_ICON = "\u26B0";

    public KamikazeHandler(String botName, IBotAPI bot) {
        super(botName, "kamikaze", bot, true);
    }

    @Override
    public String getDescription() {
        return "Goodbye, cruel world";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        fighter.damage(fighter.getHealth());
        fighterDAO.persistFighter(fighter);
        sendMessage(update, KAMIKAZE_ICON + fighter.getName());
        DeathCheckCommand c = new DeathCheckCommand(update, fighterDAO, fighter, update.getUser().getFirstName());
        CommandExecutor.execute(c, MessageSender.forBot(getBot()));
        return null;
    }

}
