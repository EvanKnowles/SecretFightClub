package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class RankingsHandler extends FightClubMessageHandler {
    private static final String SKULL = "\uD83D\uDC80";

    public RankingsHandler(String botName, IBotAPI bot) {
        super(botName, "rankings", bot, true);
    }

    @Override
    public String getDescription() {
        return "See rankings for the current room.";
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        List<Fighter> fightersInRoom = FighterDAO.get().findFightersInRoom(update.getChatId());
        fightersInRoom.sort(Comparator.comparing(Fighter::getHealth).reversed());

        StringBuilder stringBuilder = new StringBuilder("*Top fighters in Secret Fight Club*\n");
        for (Fighter fighter : fightersInRoom) {
            if (fighter.isDead()) {
                stringBuilder.append(SKULL + " ");
            }
            stringBuilder.append(fighter.getName());
            if (!fighter.isDead()) {
                stringBuilder.append(" - ").append(fighter.getHealth()).append(" health");
            }

            if (fighter.getWins() > 0) {
                stringBuilder.append(" - ").append(fighter.getWins()).append(" win(s)");
            }
            stringBuilder.append("\n");
        }

        sendMessage(update, stringBuilder.toString());

        return null;
    }

}
