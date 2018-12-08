package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.scheduled.RestartGameTimerService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

import static za.co.knonchalant.liketosee.util.StringPrettifier.pluralize;

/**
 * Created by evan on 2016/04/08.
 */
public class RestartHandler extends ValidFighterMessageHandler {

    private static final Map<Long, Set<String>> votesFor = new HashMap<>();

    public RestartHandler(String botName, IBotAPI bot) {
        super(botName, "restart", bot, true);
    }

    @Override
    public String getDescription() {
        return "Vote for a restart. Because it's stuck... or something";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        String fighterName = fighter.getName();

        int votesGiven;
        Set<String> votesFor = getVotesFor(update);

        // local variable, but it's static
        synchronized (votesFor) {
            if (votesFor.contains(fighterName)) {
                sendMessage(update, "Yes " + fighterName + " - you said that");
                return null;
            }
            votesFor.add(fighterName); // this is a set, so even if you've already voted, you only count once
            votesGiven = votesFor.size();
        }

        fighter.setInGame(true);
        fighterDAO.persistFighter(fighter);

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        int fighterCount = fightersInRoom.size();
        double requiredVotes = 0.5 * (double) fighterCount;
        boolean gameIsOver = (countLivingFighters(fightersInRoom)) <= 1;
        int votesStillNeeded = (int) (Math.ceil(requiredVotes) - votesGiven);

        if (gameIsOver) {
            // Sometimes it seems to get stuck with only one fighter left...
            // But also when not enough people opt in.
            votesStillNeeded = 0;
        }
        else {
            sendMessage(update, fighterName + " votes for a restart! Send /restart to agree.\n*" + votesStillNeeded + "* more " + pluralize(votesStillNeeded, "vote") + " needed");
        }

        if (votesStillNeeded <= 0) {
            if (!gameIsOver) {
                sendMessage(update, "\uD83D\uDEA8 Motion carried - we're restarting! All hail Demoncracy! Don't forget to /optin if you didn't vote. \uD83D\uDEA8");
            } else {
                sendMessage(update, "\uD83D\uDEA8 Game is restarting - don't forget to /optin if you didn't vote. \uD83D\uDEA8");
            }

            synchronized (votesFor) {
                scheduleRestart(update.getChatId());
            }
        }

        return null;
    }

    private long countLivingFighters(List<Fighter> fightersInRoom) {
        return fightersInRoom.stream().filter(f -> !f.isDead()).count();
    }

    public static void scheduleRestart(long chatId) {
        RestartGameTimerService gameTimerService = findGameTimerService();
        if (null == gameTimerService) {
            System.err.println("WARNING!!! No game timer service found! Game won't be restarted");
            return;
        }
        gameTimerService.scheduleRestart(chatId);
    }

    private synchronized Set<String> getVotesFor(IUpdate update) {
        if (!votesFor.containsKey(update.getChatId())) {
            votesFor.put(update.getChatId(), new HashSet<>());
        }

        return votesFor.get(update.getChatId());
    }

    public static Set<String> resetVote(long chatId) {
        return votesFor.remove(chatId);
    }

    public static Set<String> getVote(long chatId) {
        return votesFor.get(chatId);
    }

    private static RestartGameTimerService findGameTimerService() {
        try {
            return InitialContext.doLookup("java:app/fightclub-web-1.0-SNAPSHOT/RestartGameTimerService!za.co.knonchalant.telegram.scheduled.RestartGameTimerService");
        } catch (NamingException e) {
            return null; // handled by caller
        }
    }
}
