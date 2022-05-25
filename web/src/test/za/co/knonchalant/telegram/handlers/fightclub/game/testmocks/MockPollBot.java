package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import za.co.knonchalant.candogram.Bots;
import za.co.knonchalant.candogram.IBot;
import za.co.knonchalant.candogram.ShutdownNotify;

import java.util.List;

public class MockPollBot implements ShutdownNotify, IBot {
    @Override
    public void start(List<Bots> bots) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public Bots find(String name) {
        return null;
    }

    @Override
    public void notifyShutdown() {

    }
}
