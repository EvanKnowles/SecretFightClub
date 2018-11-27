package za.co.knonchalant.telegram;

import za.co.knonchalant.ServerConfig;
import za.co.knonchalant.candogram.IBot;
import za.co.knonchalant.telegram.bots.SecretFightClubBotAPIBuilder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by evan on 2016/03/03.
 */
@Startup
@Singleton
public class LaunchCanDoGram {
    private static final Logger LOGGER = Logger.getLogger(LaunchCanDoGram.class.getName());

    @EJB
    IBot bot;

    @PostConstruct
    public void start() {
        LOGGER.info("Launching bot...");

        boolean testMode = ServerConfig.isTest();

        SecretFightClubBotAPIBuilder secretFightClubBotAPIBuilder = new SecretFightClubBotAPIBuilder();

        bot.start(Collections.singletonList(secretFightClubBotAPIBuilder.buildFightClubBot(testMode)));

        LOGGER.info("Bot launched.");
    }

}
