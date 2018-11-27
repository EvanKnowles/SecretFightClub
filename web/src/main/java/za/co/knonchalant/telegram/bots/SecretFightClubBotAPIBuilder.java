package za.co.knonchalant.telegram.bots;

import za.co.knonchalant.candogram.Bots;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.TelegramBotAPI;
import za.co.knonchalant.candogram.handlers.IMessageHandler;
import za.co.knonchalant.telegram.handlers.AbuseHandler;
import za.co.knonchalant.telegram.handlers.fightclub.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by evan on 2016/04/08.
 */
public class SecretFightClubBotAPIBuilder {
    public static final String NAME = "SecretFightClub";
    private static final Logger LOGGER = Logger.getLogger(SecretFightClubBotAPIBuilder.class.getName());

    private static final String TEST_OPS_BOT = "759225216:AAFLlv-_5eEygZ90RUABqQj9B-ilWJuOEAE";

    private static final String TEST_OPS_NAME = "testSecretFightClub";
    private static final String OPS_BOT = "19LyU";
    private static final String OPS_NAME = "SecretFightClub";
    private List<IMessageHandler> handlers;

    public Bots buildFightClubBot(boolean test) {
        handlers = new ArrayList<>();
        IBotAPI botAPI;
        if (!test) {
            botAPI = new TelegramBotAPI(NAME, OPS_BOT);
        } else {
            botAPI = new TelegramBotAPI(NAME, TEST_OPS_BOT);
        }
        String botName = test ? TEST_OPS_NAME : OPS_NAME;

        addHandler(new RegisterHandler(botName, botAPI));
        addHandler(new NewItemHandler(botName, botAPI));
        addHandler(new RollHandler(botName, botAPI));
        addHandler(new UseItemHandler(botName, botAPI));
        addHandler(new RankingsHandler(botName, botAPI));
        addHandler(new OptionsHandler(botName, botAPI));
        addHandler(new AbuseHandler(botName, botAPI));


        LOGGER.info("Built Secret Fight Club with commands: ");
        StringBuilder stringBuilder = new StringBuilder();
        for (IMessageHandler handler : handlers) {
            if (handler.getDescription() == null) {
                continue;
            }

            stringBuilder.append(handler.getCommand()).append(" - ").append(handler.getDescription()).append("\n");
        }
        LOGGER.info("\n" + stringBuilder.toString());

        return new Bots(NAME, Collections.singletonList(botAPI), handlers);
    }

    private void addHandler(IMessageHandler searchHandler) {
        this.handlers.add(searchHandler);
    }

}
