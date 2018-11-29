package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;

abstract class FightClubMessageHandler extends BaseMessageHandler {
    FightClubMessageHandler(String botName, String command, IBotAPI bot, boolean noargs) {
        super(botName, command, bot, noargs);
    }

    FightClubMessageHandler(String botName, String command, IBotAPI bot) {
        super(botName, command, bot, false);
    }
}
