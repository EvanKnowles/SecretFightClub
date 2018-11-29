package za.co.knonchalant.telegram.bots;

import za.co.knonchalant.ServerConfig;
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

    private List<IMessageHandler> handlers;

    public Bots buildFightClubBot() {
        handlers = new ArrayList<>();
        String botName = ServerConfig.getBotName(NAME);
        String botSecret = ServerConfig.getBotSecret(NAME);

        IBotAPI botAPI = new TelegramBotAPI(botName, botSecret);

        addHandler(new RegisterHandler(botName, botAPI));
        addHandler(new NewItemHandler(botName, botAPI));
        addHandler(new RollHandler(botName, botAPI));
        addHandler(new UseItemHandler(botName, botAPI));
        addHandler(new RankingsHandler(botName, botAPI));
        addHandler(new OptionsHandler(botName, botAPI));
        addHandler(new AbuseHandler(botName, botAPI));
        addHandler(new StealItemHandler(botName, botAPI));
        addHandler(new ListItemsHandler(botName, botAPI));
        addHandler(new KamikazeHandler(botName, botAPI));
        addHandler(new DropHandler(botName, botAPI));
        addHandler(new RestartHandler(botName, botAPI));
        addHandler(new OptInHandler(botName, botAPI));

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
