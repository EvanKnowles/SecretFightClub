package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.IUpdate;

public class MessageSender {
    private static MessageSender instance;

    private final IBotAPI bot;

    private MessageSender(IBotAPI bot) {
        this.bot = bot;
    }

    public static MessageSender forBot(IBotAPI bot) {
        if (null == instance) {
            createInstance(bot);
        }

        if (instance.bot != bot) {
            throw new RuntimeException("Uh...... we have multiple bots?");
        }

        return instance;
    }

    private synchronized static void createInstance(IBotAPI bot) {
        if (null != instance) {
            return;
        }
        instance = new MessageSender(bot);
    }

    public void sendMessage(IUpdate update, Iterable<String> messages) {
        for (String message : messages) {
            sendMessage(update, message);
        }
    }

    public void sendMessage(IUpdate update, String message) {
        bot.sendMessage(update, message);
    }
}
