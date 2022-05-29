package za.co.knonchalant.telegram.scheduled;

import za.co.knonchalant.candogram.Bots;
import za.co.knonchalant.candogram.IBot;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Club;
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

    @EJB
    FighterDAO fighterDAO;

    @EJB
    ClubDAO clubDAO;

    /**
     * Method sync is probably overkill, but easier and probably as effective
     * as more complicated stuff
     */
    public synchronized void scheduleRestart(Club club) {
        if (queuedGames.contains(club.getId())) {
            return;
        }

        List<Fighter> fightersInRoom = club.getFighters();
        Set<String> vote = RestartHandler.getVote(club.getId());
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
        timerService.createSingleActionTimer(date, new TimerConfig(new RestartGameInfo(club.getId()), false));

        for (IBotAPI api : bots.getApis()) {
            String text = "\uD83D\uDEA8 New game starting in 60s - don't forget to /optin \uD83D\uDEA8\n";

            if (vote != null) {
                String except = "(except for " + join(new ArrayList<>(vote)) + " - you're in.)";
                text += except;
            }

            for (Fighter fighter : fightersInRoom) {
                api.sendMessage(new TargettedUpdate(fighter.getUserId()), text);
            }
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

        RestartHandler.resetVote(restartGameInfo.getClubId());

        List<Fighter> fightersInRoom = clubDAO.getClub(restartGameInfo.getClubId()).getFighters();

        long totesIn = fightersInRoom.stream().filter(Fighter::isInGame).count();

        IBot pollBot = findPollBot();
        Bots bots = pollBot.find(SecretFightClubBotAPIBuilder.NAME);

        // DB config for these numbers?
        if (totesIn >= 2) {
            for (IBotAPI api : bots.getApis()) {
                UseItemWrathHandler.restartGame(api, fighterDAO, fightersInRoom);
            }
        } else {
            for (IBotAPI api : bots.getApis()) {
                for (Fighter fighter : fightersInRoom) {
                    api.sendMessage(new TargettedUpdate(fighter.getUserId()), "*Not enough people opted-in - only " + totesIn + " out of "+fightersInRoom.size()+" - no game!* Try /restart to try again.");
                }
            }
        }

        for (Fighter fighter : fightersInRoom) {
            fighter.setInGame(false);
            fighterDAO.persistFighter(fighter);
        }
    }

    private IBot findPollBot() {
        try {
            return InitialContext.doLookup("java:global/SecretFightClub/PollBot!za.co.knonchalant.candogram.IBot");
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
