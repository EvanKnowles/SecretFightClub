package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.*;

/**
 * Created by evan on 2016/04/08.
 */
public class RestartHandler extends FightClubMessageHandler {

    private static final Map<Long, Set<String>> votesFor = new HashMap<>();

    public RestartHandler(String botName, IBotAPI bot) {
        super(botName, "restart", bot, true);
    }

    @Override
    public String getDescription() {
        return "Vote for a restart. Because it's stuck... or something";
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        FighterDAO fighterDAO = FighterDAO.get();
        long userId = update.getUser().getId();

        Fighter fighter = fighterDAO.getFighter(userId, update.getChatId());
        String fighterName = fighter.getName();

        int votesGiven;
        Set<String> votesFor = getVotesFor(update);

        // local variable, but it's static
        synchronized (votesFor) {
            votesFor.add(fighterName);
            votesGiven = votesFor.size();
        }

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        int fighterCount = fightersInRoom.size();
        double requiredVotes = 0.5 * (double) fighterCount;
        int votesStillNeeded = (int) (Math.ceil(requiredVotes) - votesGiven);
        sendMessage(update, fighterName + " votes for a restart! Send /restart to agree.\n*" + votesStillNeeded + "* more vote(s) needed");

        if (votesStillNeeded <= 0) {
            sendMessage(update, "Motion carried - we're restarting! All hail Demoncracy!");

            synchronized (votesFor) {
                UseItemWrathHandler.restartGame(getBot(), fighterDAO, fightersInRoom, update);
            }
        }

        return null;
    }

    private synchronized Set<String> getVotesFor(IUpdate update) {
        if (!votesFor.containsKey(update.getChatId())) {
            votesFor.put(update.getChatId(), new HashSet<>());
        }

        return votesFor.get(update.getChatId());
    }

    public static void resetVote(long chatId) {
        votesFor.remove(chatId);
    }
}
