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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        for (Fighter fighter : fightersInRoom) {
            fighter.setInGame(false);
        }

        Date date = new Date();
        date.setTime(date.getTime() + SIXTY_SECONDS);
        timerService.createSingleActionTimer(date, new TimerConfig(new RestartGameInfo(chatId), false));
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

                for (Fighter fighter : fightersInRoom) {
                    fighter.setInGame(false);
                    fighterDAO.persistFighter(fighter);
                }
            }
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
