package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import za.co.knonchalant.candogram.TelegramBotAPI;
import za.co.knonchalant.candogram.handlers.IUpdate;

public class MockBotAPI extends TelegramBotAPI {
    public MockBotAPI() {
        super("test", null);
    }

    @Override
    public int sendMessage(IUpdate message, String text) {
        System.out.println("MOCK: Message sent: " + text);
        return 0;
    }
}
