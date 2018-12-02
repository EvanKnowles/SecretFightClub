package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class OptInHandler extends ValidFighterMessageHandler {
    public OptInHandler(String botName, IBotAPI bot) {
        super(botName, "optin", bot, true);
    }

    @Override
    public String getDescription() {
        return "Opt into the next round.";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {

        fighter.setInGame(true);
        fighterDAO.persistFighter(fighter);

        sendMessage(update, "Righto " + fighter.getName() + ", you're all signed up for the chaos. You mad crazy beautiful fool.");

        return null;
    }
}
