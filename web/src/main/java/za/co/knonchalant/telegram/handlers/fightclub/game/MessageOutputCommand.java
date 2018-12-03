package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;

import java.util.List;

public interface MessageOutputCommand {
    List<String> getMessages();
    IUpdate getUpdate();
}
