package za.co.knonchalant.telegram.scheduled;

import za.co.knonchalant.candogram.Bots;
import za.co.knonchalant.candogram.IBot;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.bots.SecretFightClubBotAPIBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.RestartHandler;
import za.co.knonchalant.telegram.handlers.fightclub.UseItemWrathHandler;
import za.co.knonchalant.telegram.scheduled.info.RestartGameInfo;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.Timer;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

@Singleton
@Startup
public class RestartGameTimerService {
    private static final long SIXTY_SECONDS = 1000 * 60;

    private static Set<Long> queuedGames = new HashSet<>();

    @Resource
    TimerService timerService;

    // wow it's refreshing to just inject something
    @EJB
    FighterDAO fighterDAO;

    /**
     * Method sync is probably overkill, but easier and probably as effective
     * as more complicated stuff
     */
    public synchronized void scheduleRestart(long chatId) {
        if (queuedGames.contains(chatId)) {
            return;
        }

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(chatId);
        Set<String> vote = RestartHandler.getVote(chatId);
        IBot pollBot = findPollBot();
        Bots bots = pollBot.find(SecretFightClubBotAPIBuilder.NAME);

        if (vote != null) {
            for (Fighter fighter : fightersInRoom) {
                fighter.setInGame(vote.contains(fighter.getName()));
                fighterDAO.persistFighter(fighter);
            }
        }

        Date date = new Date();
        date.setTime(date.getTime() + SIXTY_SECONDS);
        timerService.createSingleActionTimer(date, new TimerConfig(new RestartGameInfo(chatId), false));

        for (IBotAPI api : bots.getApis()) {
            String text = "\uD83D\uDEA8 New game starting in 60s - don't forget to /optin \uD83D\uDEA8\n";

            if (vote != null) {
                String except = "(except for " + join(new ArrayList<>(vote)) + " - you're in.)";
                text += except;
            }
            text += "\n\nPaging @TheEvan @NOTtheDUCK @BergenLarsen @AtJohn";
            api.sendMessage(new AwfulMockUpdate(chatId), text);
        }
    }

    private String join(List<String> vote) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < vote.size(); i++) {
            if (i != 0) {
                if (i != vote.size() - 1) {
                    stringBuilder.append(", ");
                } else {
                    stringBuilder.append(" and ");
                }
            }

            stringBuilder.append(vote.get(i));
        }

        return stringBuilder.toString();
    }

    @Timeout
    public void freakingGameOn(Timer timer) {
        RestartGameInfo restartGameInfo = (RestartGameInfo) timer.getInfo();

        RestartHandler.resetVote(restartGameInfo.getChatId());

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(restartGameInfo.getChatId());
        long totesIn = fightersInRoom.stream().filter(Fighter::isInGame).count();

        IBot pollBot = findPollBot();
        Bots bots = pollBot.find(SecretFightClubBotAPIBuilder.NAME);
        AwfulMockUpdate awfulMockUpdate = new AwfulMockUpdate(restartGameInfo.getChatId());

        // DB config for these numbers?
        if (totesIn >= 2) {
            for (IBotAPI api : bots.getApis()) {
                UseItemWrathHandler.restartGame(api, fighterDAO, fightersInRoom, awfulMockUpdate);
            }
        } else {
            for (IBotAPI api : bots.getApis()) {
                api.sendMessage(awfulMockUpdate, "*Not enough people opted-in - no game!* Try /restart to try again.");
            }
        }

        for (Fighter fighter : fightersInRoom) {
            fighter.setInGame(false);
            fighterDAO.persistFighter(fighter);
        }
    }

    private IBot findPollBot() {
        try {
            return InitialContext.doLookup("java:app/fightclub-web-1.0-SNAPSHOT/PollBot!za.co.knonchalant.candogram.IBot");
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
