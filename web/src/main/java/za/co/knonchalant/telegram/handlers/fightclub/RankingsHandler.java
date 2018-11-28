package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class RankingsHandler extends BaseMessageHandler {
    private static final String SKULL = "\uD83D\uDC80";
    private static final String TROPHY = "\uD83C\uDFC6";

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

        long mostWins = determineMostWins(fightersInRoom);

        StringBuilder stringBuilder = new StringBuilder("*Top fighters in Secret Fight Club*\n");
        for (Fighter fighter : fightersInRoom) {
            stringBuilder.append(fighter.getName());
            if (fighter.getHealth() <= 0) {
                stringBuilder.append(" " + SKULL);
            } else {
                stringBuilder.append(" - ").append(fighter.getHealth()).append(" health");
            }

            if (fighter.getWins() == mostWins) {
                stringBuilder.append(" ").append(TROPHY);
            }

            if (fighter.getWins() > 0) {
                stringBuilder.append(" - ").append(fighter.getWins()).append(" win(s)");
            }
            stringBuilder.append("\n");
        }

        sendMessage(update, stringBuilder.toString());

        return null;
    }

    private long determineMostWins(List<Fighter> fightersInRoom) {
        long mostWins = 0;
        for (Fighter fighter : fightersInRoom) {
            if (fighter.getWins() > mostWins) {
                mostWins = fighter.getWins();
            }
        }
        return mostWins;
    }
}
