package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.telegram.handlers.fightclub.game.CommandExecutor;
import za.co.knonchalant.telegram.handlers.fightclub.game.FightClubCommand;
import za.co.knonchalant.telegram.handlers.fightclub.game.MessageSender;

public class FightClubMessage extends BaseMessage {
    @Override
    public void sendMessage(IUpdate message, String text) {
        super.sendMessage(message, text);
    }

    protected void execute(FightClubCommand command) {
        CommandExecutor.execute(command, MessageSender.forBot(getBot()));
    }
}
