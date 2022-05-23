package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.liketosee.util.StringPrettifier;

import java.util.Comparator;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class RankingsHandler extends ValidFighterMessageHandler {

  public RankingsHandler(String botName, IBotAPI bot) {
        super(botName, "rankings", bot, true);
    }

    @Override
    public String getDescription() {
        return "See rankings for the current room.";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter userFighter) {
        List<Fighter> fightersInRoom = userFighter.getClub().getFighters();
        fightersInRoom.sort(Comparator.comparing(Fighter::getHealth).reversed());

        StringBuilder stringBuilder = new StringBuilder("*Top fighters in Secret Fight Club*\n");
        for (Fighter fighter : fightersInRoom) {
            stringBuilder.append(StringPrettifier.describePlayer(fighter, fighterDAO));
            if (!fighter.isDead()) {
                stringBuilder.append(" - ").append(fighter.getHealth()).append(" health");
            }

            if (fighter.getWins() > 0) {
                stringBuilder.append(" - ").append(fighter.getWins()).append(" ").append(StringPrettifier.pluralize(fighter.getWins(), "win", "wins"));
            }
            stringBuilder.append("\n");
        }

        sendMessage(update, stringBuilder.toString());

        return null;
    }

}
