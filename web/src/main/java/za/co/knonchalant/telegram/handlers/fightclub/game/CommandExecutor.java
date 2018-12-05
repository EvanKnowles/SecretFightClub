package za.co.knonchalant.telegram.handlers.fightclub.game;

public class CommandExecutor {
    private CommandExecutor() {}

    public static void execute(FightClubCommand command, MessageSender handler) {
        command.execute(handler);
    }
}
