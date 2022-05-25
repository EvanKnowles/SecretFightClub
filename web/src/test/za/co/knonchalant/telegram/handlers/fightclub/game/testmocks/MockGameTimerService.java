package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.telegram.scheduled.RestartGameTimerService;

public class MockGameTimerService extends RestartGameTimerService {

    @Override
    public synchronized void scheduleRestart(Club club) {
        System.out.println("Schedule restart for: " + club.getName());
    }
}
