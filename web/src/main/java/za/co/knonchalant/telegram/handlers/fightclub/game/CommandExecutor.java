package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.telegram.handlers.fightclub.FightClubMessage;
import za.co.knonchalant.telegram.handlers.fightclub.FightClubMessageHandler;

public class CommandExecutor {
    private CommandExecutor() {}

    public static void execute(FightClubCommand command, FightClubMessage handler) {
        command.execute();

        if (command instanceof MessageOutputCommand) {
            sendMessages((MessageOutputCommand) command, handler);
        }
    }

    private static void sendMessages(MessageOutputCommand command, FightClubMessage handler) {
        for (String m : command.getMessages()) {
            handler.sendMessage(command.getUpdate(), m);
        }
    }




    public static void execute(FightClubCommand command, FightClubMessageHandler handler) {
        command.execute();

        if (command instanceof MessageOutputCommand) {
            sendMessages((MessageOutputCommand) command, handler);
        }
    }

    private static void sendMessages(MessageOutputCommand command, FightClubMessageHandler handler) {
        for (String m : command.getMessages()) {
            handler.sendMessage(command.getUpdate(), m);
        }
    }
}
